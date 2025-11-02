# Mock E-Trading Platform - Robot Framework Automation

A mock E-Trading system for simulating bond trading workflows using Robot Framework with Java integration. All APIs are mocked via Java logic with no real API calls.

## Prerequisites

- Java 11 or higher
- Maven 3.6+

## Setup & Run

1. **Compile the project:**
   ```bash
   mvn clean compile
   ```

2. **Run tests:**

   **Using Maven test:**
   ```bash
   mvn clean test
   ```

   **Using Robot Framework plugin:**
   ```bash
   mvn robotframework:run
   ```

3. **View test reports:**
   - Report: `target/robotframework-reports/report.html`
   - Log: `target/robotframework-reports/log.html`

## Framework Overview

### Java Services

- **InstrumentService**: Manages instruments, validates ISIN format, provides search
- **TradeService**: Handles trade lifecycle (CREATED → EXECUTED → CONFIRMED), maintains trade history
- **MarketDataService**: Generates mock market data with random prices, simulates execution failures (10% default)

### Trade Lifecycle

```
CREATED → EXECUTED → CONFIRMED
   ↓
REJECTED (validation fails) / CANCELLED (before confirmation)
```

### Custom Keywords

Located in `src/main/resources/keywords/TradeKeywords.robot`:
- Fetch Instrument Details, Create Trade, Execute Trade, Confirm Trade, Cancel Trade
- Verify Trade Status, Get Market Data, Get Market Data Mid Price
- Validate Price Deviation, Get Trade Id, Get Trade Rejection Reason

### Test Scenarios

Implemented in `src/test/resources/tests/TradeLifecycleTests.robot`:

1. Create and confirm a valid trade (CREATED → EXECUTED → CONFIRMED)
2. Invalid ISIN trade rejection
3. Price deviation trade rejection (>5% threshold)
4. Execute already confirmed trade error handling
5. System failure in execution with retry mechanism

## Configuration

- **Price Range**: 90.0 - 110.0 (MarketDataService)
- **Failure Rate**: 10% (configurable)
- **Price Deviation Threshold**: 5% (TradeService)

## Project Structure

```
src/
├── main/
│   ├── java/com/mocktrading/
│   │   ├── InstrumentService.java
│   │   ├── TradeService.java
│   │   ├── MarketDataService.java
│   │   ├── TradingLibrary.java
│   │   ├── model/ (Instrument, Trade, MarketData)
│   │   └── enums/ (TradeStatus)
│   └── resources/keywords/
│       └── TradeKeywords.robot
└── test/resources/tests/
    └── TradeLifecycleTests.robot
```

## Troubleshooting

- **Tests fail with "Module does not contain 'TradingLibrary'"**: Run `mvn clean compile` first
- **Trade execution failures**: Expected due to 10% random failure rate (handled by retry test)
- **Maven build fails**: Verify Java 11+ and Maven 3.6+ are installed
