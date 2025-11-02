# Mock E-Trading Platform - Robot Framework Automation

A comprehensive automation framework for simulating bond trading workflows using Robot Framework with Java integration.

## Project Structure

```
demo_project/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/mocktrading/
│   │   │   ├── InstrumentService.java
│   │   │   ├── TradeService.java
│   │   │   ├── MarketDataService.java
│   │   │   ├── TradingLibrary.java
│   │   │   ├── model/
│   │   │   │   ├── Instrument.java
│   │   │   │   ├── Trade.java
│   │   │   │   └── MarketData.java
│   │   │   └── enums/
│   │   │       └── TradeStatus.java
│   │   └── resources/keywords/
│   │       └── TradeKeywords.robot
│   └── test/resources/tests/
│       └── TradeLifecycleTests.robot
└── README.md
```

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Robot Framework

## Setup

1. **Compile the project:**
   ```bash
   mvn clean compile
   ```

2. **Run tests:**
   ```bash
   mvn test
   ```

   Or use Robot Framework directly:
   ```bash
   robot --pythonpath target/classes src/test/resources/tests/TradeLifecycleTests.robot
   ```

## Features

### Services

- **InstrumentService**: Manages instrument data, validates ISINs, and provides search functionality
- **TradeService**: Handles trade lifecycle (CREATED → EXECUTED → CONFIRMED), maintains trade history
- **MarketDataService**: Generates mock market data with random prices, simulates execution failures

### Test Scenarios

1. **Create And Confirm Valid Trade**: Validates complete trade lifecycle
2. **Invalid ISIN Trade Rejection**: Tests error handling for invalid instruments
3. **Price Deviation Trade Rejection**: Validates price deviation logic (5% threshold)
4. **Execute Already Confirmed Trade Error Handling**: Tests state transition validation
5. **System Failure In Execution With Retry**: Simulates failures and retry mechanism

## Custom Keywords

- `Fetch Instrument Details`: Retrieves instrument by ISIN
- `Create Trade`: Creates a new trade
- `Execute Trade`: Moves trade to EXECUTED status
- `Confirm Trade`: Moves trade to CONFIRMED status
- `Cancel Trade`: Cancels a trade
- `Verify Trade Status`: Validates trade status
- `Get Market Data`: Retrieves current market prices
- `Validate Price Deviation`: Checks price deviation threshold

## Design Principles

- **State Management**: Thread-safe in-memory storage for trades and instruments
- **Mocking**: All APIs mocked via Java services, no real API calls
- **Error Handling**: Comprehensive exception handling with meaningful messages
- **Extensibility**: Clean separation of concerns, easy to extend

## Configuration

Failure rate for execution simulation is configurable in `MarketDataService` (default: 10%).

Price deviation threshold is 5% (configurable in `TradeService`).

