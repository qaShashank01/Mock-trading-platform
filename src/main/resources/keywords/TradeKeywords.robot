*** Settings ***
Library    com.mocktrading.TradingLibrary    WITH NAME    TradingLib

*** Keywords ***
Fetch Instrument Details
    [Arguments]    ${isin}
    [Documentation]    Fetches instrument details by ISIN
    ${instrument}=    TradingLib.Fetch Instrument Details    ${isin}
    ${result}=    Set Variable    ${instrument}
    [Return]    ${result}

Create Trade
    [Arguments]    ${isin}    ${quantity}    ${price}
    [Documentation]    Creates a new trade with given parameters
    ${trade}=    TradingLib.Create Trade    ${isin}    ${quantity}    ${price}
    ${result}=    Set Variable    ${trade}
    [Return]    ${result}

Execute Trade
    [Arguments]    ${tradeId}
    [Documentation]    Executes a trade (CREATED -> EXECUTED)
    ${trade}=    TradingLib.Execute Trade    ${tradeId}
    ${result}=    Set Variable    ${trade}
    [Return]    ${result}

Confirm Trade
    [Arguments]    ${tradeId}
    [Documentation]    Confirms an executed trade (EXECUTED -> CONFIRMED)
    ${trade}=    TradingLib.Confirm Trade    ${tradeId}
    ${result}=    Set Variable    ${trade}
    [Return]    ${result}

Cancel Trade
    [Arguments]    ${tradeId}
    [Documentation]    Cancels a trade
    ${trade}=    TradingLib.Cancel Trade    ${tradeId}
    ${result}=    Set Variable    ${trade}
    [Return]    ${result}

Verify Trade Status
    [Arguments]    ${tradeId}    ${expectedStatus}
    [Documentation]    Verifies trade status matches expected value
    ${result}=    TradingLib.Verify Trade Status    ${tradeId}    ${expectedStatus}
    Should Be True    ${result}    Trade status verification failed

Get Market Data
    [Arguments]    ${isin}
    [Documentation]    Retrieves current market data for an instrument
    ${marketData}=    TradingLib.Get Market Data    ${isin}
    ${result}=    Set Variable    ${marketData}
    [Return]    ${result}

Validate Price Deviation
    [Arguments]    ${isin}    ${price}    ${thresholdPercent}=5.0
    [Documentation]    Validates if price deviation is within threshold
    ${result}=    TradingLib.Validate Price Deviation    ${isin}    ${price}    ${thresholdPercent}
    ${result}=    Set Variable    ${result}
    [Return]    ${result}

Get Market Data Mid Price
    [Arguments]    ${isin}
    [Documentation]    Gets the mid price (average of bid and ask) for an instrument
    ${midPrice}=    TradingLib.Get Market Data Mid Price    ${isin}
    ${result}=    Set Variable    ${midPrice}
    [Return]    ${result}

Get Trade Id
    [Arguments]    ${trade}
    [Documentation]    Extracts trade ID from trade object
    ${tradeId}=    TradingLib.Get Trade Id    ${trade}
    ${result}=    Set Variable    ${tradeId}
    [Return]    ${result}

Get Trade Rejection Reason
    [Arguments]    ${trade}
    [Documentation]    Gets rejection reason from trade object
    ${reason}=    TradingLib.Get Trade Rejection Reason    ${trade}
    ${result}=    Set Variable    ${reason}
    [Return]    ${result}

