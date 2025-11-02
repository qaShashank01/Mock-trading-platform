*** Settings ***
Library    com.mocktrading.TradingLibrary    WITH NAME    TradingLib

*** Keywords ***
Fetch Instrument Details
    [Arguments]    ${isin}
    [Documentation]    Fetches instrument details by ISIN
    ${instrument}=    TradingLib.Fetch Instrument Details    ${isin}
    [Return]    ${instrument}

Create Trade
    [Arguments]    ${isin}    ${quantity}    ${price}
    [Documentation]    Creates a new trade with given parameters
    ${trade}=    TradingLib.Create Trade    ${isin}    ${quantity}    ${price}
    [Return]    ${trade}

Execute Trade
    [Arguments]    ${tradeId}
    [Documentation]    Executes a trade (CREATED -> EXECUTED)
    ${trade}=    TradingLib.Execute Trade    ${tradeId}
    [Return]    ${trade}

Confirm Trade
    [Arguments]    ${tradeId}
    [Documentation]    Confirms an executed trade (EXECUTED -> CONFIRMED)
    ${trade}=    TradingLib.Confirm Trade    ${tradeId}
    [Return]    ${trade}

Cancel Trade
    [Arguments]    ${tradeId}
    [Documentation]    Cancels a trade
    ${trade}=    TradingLib.Cancel Trade    ${tradeId}
    [Return]    ${trade}

Verify Trade Status
    [Arguments]    ${tradeId}    ${expectedStatus}
    [Documentation]    Verifies trade status matches expected value
    ${result}=    TradingLib.Verify Trade Status    ${tradeId}    ${expectedStatus}
    Should Be True    ${result}    Trade status verification failed

Get Market Data
    [Arguments]    ${isin}
    [Documentation]    Retrieves current market data for an instrument
    ${marketData}=    TradingLib.Get Market Data    ${isin}
    [Return]    ${marketData}

Validate Price Deviation
    [Arguments]    ${isin}    ${price}    ${thresholdPercent}=5.0
    [Documentation]    Validates if price deviation is within threshold
    ${result}=    TradingLib.Validate Price Deviation    ${isin}    ${price}    ${thresholdPercent}
    [Return]    ${result}

Get Market Data Mid Price
    [Arguments]    ${isin}
    [Documentation]    Gets the mid price (average of bid and ask) for an instrument
    ${midPrice}=    TradingLib.Get Market Data Mid Price    ${isin}
    [Return]    ${midPrice}

Get Trade Id
    [Arguments]    ${trade}
    [Documentation]    Extracts trade ID from trade object
    ${tradeId}=    TradingLib.Get Trade Id    ${trade}
    [Return]    ${tradeId}

Get Trade Rejection Reason
    [Arguments]    ${trade}
    [Documentation]    Gets rejection reason from trade object
    ${reason}=    TradingLib.Get Trade Rejection Reason    ${trade}
    [Return]    ${reason}

