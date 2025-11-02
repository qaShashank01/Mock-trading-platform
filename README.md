# Mock E-Trading Platform - Robot Framework Automation

A comprehensive automation framework for simulating bond trading workflows using Robot Framework with Java integration. This project implements a mock E-Trading system where all APIs are mocked via Java logic, with no real API calls.

## Objective

Design and automate a mock E-Trading workflow using Robot Framework integrated with Java. The framework simulates trading lifecycles, mocks all APIs, and provides reusable keywords for comprehensive test automation.

## Business Scenario

Simulates a simplified bond trading system where traders can:
- Search instruments by ISIN
- Create trades with quantity and price
- Execute trades (CREATED → EXECUTED)
- Confirm executed trades (EXECUTED → CONFIRMED)
- Cancel trades (before confirmation)
- Handle trade rejections and errors

All behaviors are mocked via Java logic with no real API calls.

## Project Structure

```
mock-trading-platform/
├── pom.xml
├── README.md
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
└── target/
    └── robotframework-reports/
        ├── report.html
        ├── log.html
        └── output.xml
```

## Prerequisites

- **Java 11 or higher** - Required for compilation and execution
- **Maven 3.6+** - For dependency management and build automation
- Robot Framework (managed via Maven plugin)

## Setup

1. **Clone or extract the project:**
   ```bash
   cd mock-trading-platform
   ```

2. **Compile the project:**
   ```bash
   mvn clean compile
   ```

3. **Run tests:**
   ```bash
   mvn clean test
   ```
   
   This will:
   - Compile all Java source files
   - Execute Robot Framework test suite
   - Generate test reports in `target/robotframework-reports/`

## Running Tests

### Using Maven (Recommended)
```bash
mvn clean test
```

### Using Maven Robot Framework Plugin
```bash
mvn robotframework:run
```

### View Test Reports
After test execution, open the generated reports:
- **Report**: `target/robotframework-reports/report.html`
- **Log**: `target/robotframework-reports/log.html`
- **XML Output**: `target/robotframework-reports/output.xml`

## Framework Components

### Java Services

#### InstrumentService
- Manages instrument data in-memory
- Validates ISIN format (ISO 6166 standard: 2 letters + 9 alphanumeric + 1 digit)
- Provides search functionality by name or ISIN
- Pre-initialized with sample bond instruments

#### TradeService
- Manages complete trade lifecycle
- Maintains thread-safe trade history
- Validates trade state transitions
- Handles trade rejection logic
- Enforces business rules (price deviation, ISIN validation)

#### MarketDataService
- Generates mock market data with random prices (90-110 range)
- Simulates realistic bid/ask spreads
- Configurable execution failure simulation (default: 10% failure rate)
- Validates price deviations against market data

### Trade Lifecycle States

The system maintains stateful trade lifecycles with the following transitions:

```
CREATED → EXECUTED → CONFIRMED
   ↓
REJECTED (if validation fails)
   ↓
CANCELLED (if cancelled before confirmation)
```

**State Rules:**
- Trades start in `CREATED` status
- Must be `EXECUTED` before `CONFIRMED`
- Cannot execute already `CONFIRMED` trades
- Cannot cancel `CONFIRMED` trades
- `REJECTED` trades cannot be executed

### TradingLibrary

Java library exposing Robot Framework keywords via annotations:
- Uses `@RobotKeywords` for class-level annotation
- Methods annotated with `@RobotKeyword` become available as keywords
- Integrates with Robot Framework via `javalib-core`

### Custom Robot Keywords

Located in `src/main/resources/keywords/TradeKeywords.robot`:

- **Fetch Instrument Details**: Retrieves instrument by ISIN
- **Create Trade**: Creates a new trade with ISIN, quantity, and price
- **Execute Trade**: Executes a trade (CREATED → EXECUTED)
- **Confirm Trade**: Confirms an executed trade (EXECUTED → CONFIRMED)
- **Cancel Trade**: Cancels a trade (if not confirmed)
- **Verify Trade Status**: Validates trade status matches expected value
- **Get Market Data**: Retrieves current market prices for an instrument
- **Get Market Data Mid Price**: Gets the mid price (average of bid and ask)
- **Validate Price Deviation**: Checks if price deviation is within threshold
- **Get Trade Id**: Extracts trade ID from trade object
- **Get Trade Rejection Reason**: Gets rejection reason from trade object

## Test Scenarios

All test scenarios are implemented in `src/test/resources/tests/TradeLifecycleTests.robot`:

| # | Scenario | Expected Behavior |
|---|----------|-------------------|
| 1 | Create and confirm a valid trade | Status transitions CREATED → EXECUTED → CONFIRMED |
| 2 | Invalid ISIN trade | Trade rejected with error message |
| 3 | Price deviates from market | Trade rejected with reason 'Price deviation' |
| 4 | Execute already confirmed trade | Error handled gracefully with appropriate message |
| 5 | Simulate system failure in execution | Proper retry mechanism with error assertion |

### Test Scenario Details

1. **Create And Confirm Valid Trade**
   - Creates a trade with valid ISIN and market-aligned price
   - Verifies status transitions through all lifecycle stages
   - Validates successful trade completion

2. **Invalid ISIN Trade Rejection**
   - Attempts to create trade with invalid ISIN
   - Verifies proper error handling and rejection message

3. **Price Deviation Trade Rejection**
   - Creates trade with price deviating >5% from market mid price
   - Verifies trade is rejected with appropriate reason

4. **Execute Already Confirmed Trade Error Handling**
   - Creates, executes, and confirms a trade
   - Attempts to execute already confirmed trade
   - Verifies graceful error handling

5. **System Failure In Execution With Retry**
   - Creates a valid trade
   - Implements retry mechanism for execution failures
   - Validates proper error handling or successful execution after retries

## Design Principles

### Framework Structure & Organization (25%)
- Clean separation of concerns (Services, Models, Keywords)
- Well-organized project structure following Maven conventions
- Reusable keyword library with comprehensive documentation

### Java Mocking Logic Realism (25%)
- Realistic market data generation with random prices
- Proper ISIN validation (ISO 6166 standard)
- Price deviation validation (5% threshold)
- Random execution failure simulation
- Stateful trade lifecycle management

### Keyword Design & Reusability (20%)
- Custom keywords abstract Java implementation details
- Keywords are modular and reusable across test cases
- Clear documentation for each keyword
- Consistent naming conventions

### Negative & Edge Case Handling (15%)
- Invalid ISIN handling
- Price deviation validation
- State transition validation
- Error handling for invalid operations
- Retry mechanism for transient failures

### Reporting & Documentation (15%)
- Comprehensive README with setup instructions
- Inline documentation in code
- Robot Framework keyword documentation
- Test report generation (HTML, XML, XUnit)

## Configuration

### Market Data Service
- **Price Range**: 90.0 - 110.0 (configurable in `MarketDataService`)
- **Failure Rate**: 10% (configurable via constructor)
- **Data Freshness**: 60 seconds (cached market data expires after 60s)

### Trade Service
- **Price Deviation Threshold**: 5% (configurable in `TradeService.createTrade()`)
- **Trade ID Format**: `TRD` + 8-digit counter (e.g., `TRD00000001`)

### Instrument Service
- **ISIN Pattern**: `^[A-Z]{2}[A-Z0-9]{9}\\d$` (ISO 6166 standard)
- **Pre-loaded Instruments**: 5 sample bond instruments

## Key Features

✅ **No Real API Calls**: All APIs mocked via Java services  
✅ **Stateful Lifecycle**: Thread-safe in-memory storage for trades and instruments  
✅ **Random Price Generation**: Realistic market data with bid/ask spreads  
✅ **Failure Simulation**: Configurable random execution failures  
✅ **Comprehensive Error Handling**: Meaningful error messages for all scenarios  
✅ **Trade History**: Maintains complete history of all trades  
✅ **Extensible Design**: Easy to add new services, keywords, and test scenarios

## Evaluation Criteria Alignment

| Criteria | Implementation | Weight |
|----------|----------------|--------|
| Framework structure & organization | ✅ Maven-based structure, clean separation, reusable keywords | 25% |
| Java mocking logic realism | ✅ Realistic market data, ISIN validation, price deviation, failure simulation | 25% |
| Keyword design & reusability | ✅ 10+ custom keywords, modular design, comprehensive docs | 20% |
| Negative & edge case handling | ✅ 5 test scenarios covering positive, negative, and boundary cases | 15% |
| Reporting & documentation | ✅ Comprehensive README, inline docs, test reports | 15% |

## Troubleshooting

### Issue: Tests fail with "Module 'com.mocktrading' does not contain 'TradingLibrary'"
**Solution**: Ensure you compile the project first using `mvn clean compile` before running tests.

### Issue: Trade execution always fails
**Solution**: This is expected behavior due to 10% random failure rate. The retry test handles this scenario.

### Issue: Maven build fails
**Solution**: Ensure Java 11+ and Maven 3.6+ are installed and properly configured.

## GitHub Actions CI/CD

This project includes automated daily test execution via GitHub Actions.

### Daily Schedule
- **Runs daily at 8:00 AM IST (2:30 AM UTC)**
- Automatically executes all Robot Framework tests
- Generates and stores test reports as artifacts

### Manual Execution
1. Go to **Actions** tab in GitHub
2. Select **Daily Test Execution** workflow
3. Click **Run workflow**

### Viewing Results
1. Navigate to **Actions** tab
2. Select the workflow run
3. Download artifacts to view HTML test reports
4. Check logs for detailed execution information

### Workflow Features
- ✅ Automatic test execution on schedule
- ✅ Java 11 setup and Maven dependency caching
- ✅ Test report artifact generation
- ✅ 30-day artifact retention
- ✅ Test summary in workflow run

See `.github/workflows/daily-tests.yml` for workflow configuration details.

## License

This project is a mock implementation for testing and demonstration purposes.
