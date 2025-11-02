*** Settings ***
Library    com.mocktrading.TradingLibrary    WITH NAME    TradingLib
Resource    ../../../../src/main/resources/keywords/TradeKeywords.robot

*** Test Cases ***
Create And Confirm Valid Trade
    [Documentation]    Scenario 1: Create and confirm a valid trade
    ...    Expected: Status transitions CREATED → EXECUTED → CONFIRMED
    ${isin}=    Set Variable    US0378331005
    ${quantity}=    Set Variable    100
    ${tradePrice}=    Get Market Data Mid Price    ${isin}
    
    ${trade}=    Create Trade    ${isin}    ${quantity}    ${tradePrice}
    ${tradeId}=    Get Trade Id    ${trade}
    Verify Trade Status    ${tradeId}    CREATED
    
    ${executedTrade}=    Execute Trade    ${tradeId}
    ${executedTradeId}=    Get Trade Id    ${executedTrade}
    Verify Trade Status    ${executedTradeId}    EXECUTED
    
    ${confirmedTrade}=    Confirm Trade    ${executedTradeId}
    ${confirmedTradeId}=    Get Trade Id    ${confirmedTrade}
    Verify Trade Status    ${confirmedTradeId}    CONFIRMED

Invalid ISIN Trade Rejection
    [Documentation]    Scenario 2: Invalid ISIN trade
    ...    Expected: Trade rejected with error
    ${invalidIsin}=    Set Variable    INVALID123456
    ${quantity}=    Set Variable    100
    ${price}=    Set Variable    100.0
    
    Run Keyword And Expect Error    Trade creation failed: Trade rejected: Invalid ISIN
    ...    Create Trade    ${invalidIsin}    ${quantity}    ${price}

Price Deviation Trade Rejection
    [Documentation]    Scenario 3: Price deviates from market
    ...    Expected: Trade rejected with reason 'Price deviation'
    ${isin}=    Set Variable    US5949181045
    ${quantity}=    Set Variable    50
    ${midPrice}=    Get Market Data Mid Price    ${isin}
    ${deviatedPrice}=    Evaluate    ${midPrice} * 1.10
    
    ${trade}=    Create Trade    ${isin}    ${quantity}    ${deviatedPrice}
    ${tradeId}=    Get Trade Id    ${trade}
    Verify Trade Status    ${tradeId}    REJECTED
    ${rejectionReason}=    Get Trade Rejection Reason    ${trade}
    Should Contain    ${rejectionReason}    Price deviation

Execute Already Confirmed Trade Error Handling
    [Documentation]    Scenario 4: Execute already confirmed trade
    ...    Expected: Error handled gracefully
    ${isin}=    Set Variable    US02079K3059
    ${quantity}=    Set Variable    75
    ${midPrice}=    Get Market Data Mid Price    ${isin}
    
    ${trade}=    Create Trade    ${isin}    ${quantity}    ${midPrice}
    ${tradeId}=    Get Trade Id    ${trade}
    ${executedTrade}=    Execute Trade    ${tradeId}
    ${executedTradeId}=    Get Trade Id    ${executedTrade}
    ${confirmedTrade}=    Confirm Trade    ${executedTradeId}
    ${confirmedTradeId}=    Get Trade Id    ${confirmedTrade}
    Verify Trade Status    ${confirmedTradeId}    CONFIRMED
    
    Run Keyword And Expect Error    Trade execution failed: Trade already confirmed: *
    ...    Execute Trade    ${confirmedTradeId}

System Failure In Execution With Retry
    [Documentation]    Scenario 5: Simulate system failure in execution
    ...    Expected: Proper retry/error assertion
    ${isin}=    Set Variable    GB0002875804
    ${quantity}=    Set Variable    200
    ${midPrice}=    Get Market Data Mid Price    ${isin}
    
    ${trade}=    Create Trade    ${isin}    ${quantity}    ${midPrice}
    ${tradeId}=    Get Trade Id    ${trade}
    ${maxRetries}=    Set Variable    5
    ${executed}=    Set Variable    ${False}
    
    FOR    ${i}    IN RANGE    ${maxRetries}
        ${result}=    Run Keyword And Return Status    Execute Trade    ${tradeId}
        IF    ${result}
            ${executedTrade}=    TradingLib.Get Trade    ${tradeId}
            ${executedTradeId}=    Get Trade Id    ${executedTrade}
            Verify Trade Status    ${executedTradeId}    EXECUTED
            ${executed}=    Set Variable    ${True}
            Exit For Loop
        ELSE
            ${error}=    Run Keyword And Expect Error    *    Execute Trade    ${tradeId}
            Log    Retry ${i+1}: ${error}
            Should Contain    ${error}    System failure    OR    Trade execution failed
        END
    END
    
    Run Keyword If    not ${executed}    Fail    Trade execution failed after ${maxRetries} attempts. Expected success or proper error handling.

