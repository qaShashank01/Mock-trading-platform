package com.mocktrading;

import com.mocktrading.model.Instrument;
import com.mocktrading.model.MarketData;
import com.mocktrading.model.Trade;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.javalib.annotation.ArgumentNames;
import java.util.List;

@RobotKeywords
public class TradingLibrary {
    private final InstrumentService instrumentService;
    private final MarketDataService marketDataService;
    private final TradeService tradeService;

    public TradingLibrary() {
        this.instrumentService = new InstrumentService();
        this.marketDataService = new MarketDataService();
        this.tradeService = new TradeService(instrumentService, marketDataService);
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Fetch Instrument Details")
    @ArgumentNames({"isin"})
    public Instrument fetchInstrumentDetails(String isin) {
        return instrumentService.fetchInstrument(isin);
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Create Trade")
    @ArgumentNames({"isin", "quantity", "price"})
    public Trade createTrade(String isin, int quantity, double price) {
        try {
            return tradeService.createTrade(isin, quantity, price);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Trade creation failed: " + e.getMessage());
        }
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Execute Trade")
    @ArgumentNames({"tradeId"})
    public Trade executeTrade(String tradeId) {
        try {
            return tradeService.executeTrade(tradeId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Trade execution failed: " + e.getMessage());
        }
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Confirm Trade")
    @ArgumentNames({"tradeId"})
    public Trade confirmTrade(String tradeId) {
        try {
            return tradeService.confirmTrade(tradeId);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Trade confirmation failed: " + e.getMessage());
        }
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Cancel Trade")
    @ArgumentNames({"tradeId"})
    public Trade cancelTrade(String tradeId) {
        try {
            return tradeService.cancelTrade(tradeId);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Trade cancellation failed: " + e.getMessage());
        }
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Verify Trade Status")
    @ArgumentNames({"tradeId", "expectedStatus"})
    public boolean verifyTradeStatus(String tradeId, String expectedStatus) {
        Trade trade = tradeService.getTrade(tradeId);
        return trade.getStatus().toString().equals(expectedStatus);
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Get Market Data")
    @ArgumentNames({"isin"})
    public MarketData getMarketData(String isin) {
        return marketDataService.getMarketData(isin);
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Validate Price Deviation")
    @ArgumentNames({"isin", "price", "thresholdPercent"})
    public boolean validatePriceDeviation(String isin, double price, double thresholdPercent) {
        return marketDataService.validatePriceDeviation(isin, price, thresholdPercent);
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Get Trade")
    @ArgumentNames({"tradeId"})
    public Trade getTrade(String tradeId) {
        return tradeService.getTrade(tradeId);
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Get Market Data Mid Price")
    @ArgumentNames({"isin"})
    public double getMarketDataMidPrice(String isin) {
        return marketDataService.getMarketData(isin).getMidPrice();
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Get Trade Id")
    @ArgumentNames({"trade"})
    public String getTradeId(Trade trade) {
        return trade.getTradeId();
    }

    @org.robotframework.javalib.annotation.RobotKeyword("Get Trade Rejection Reason")
    @ArgumentNames({"trade"})
    public String getTradeRejectionReason(Trade trade) {
        return trade.getRejectionReason() != null ? trade.getRejectionReason() : "";
    }
}

