package com.mocktrading;

import com.mocktrading.enums.TradeStatus;
import com.mocktrading.model.Trade;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TradeService {
    private final Map<String, Trade> trades;
    private final AtomicLong tradeIdCounter;
    private final InstrumentService instrumentService;
    private final MarketDataService marketDataService;

    public TradeService(InstrumentService instrumentService, MarketDataService marketDataService) {
        this.trades = new ConcurrentHashMap<>();
        this.tradeIdCounter = new AtomicLong(1);
        this.instrumentService = instrumentService;
        this.marketDataService = marketDataService;
    }

    public Trade createTrade(String isin, int quantity, double price) {
        if (!instrumentService.instrumentExists(isin)) {
            Trade rejectedTrade = new Trade(generateTradeId(), isin, quantity, price);
            rejectedTrade.setStatus(TradeStatus.REJECTED);
            rejectedTrade.setRejectionReason("Invalid ISIN: " + isin);
            trades.put(rejectedTrade.getTradeId(), rejectedTrade);
            throw new IllegalArgumentException("Trade rejected: Invalid ISIN");
        }

        double midPrice = marketDataService.getMarketData(isin).getMidPrice();
        double deviationPercent = Math.abs((price - midPrice) / midPrice * 100);

        Trade trade = new Trade(generateTradeId(), isin, quantity, price);
        
        if (deviationPercent > 5.0) {
            trade.setStatus(TradeStatus.REJECTED);
            trade.setRejectionReason("Price deviation exceeds 5%");
        }

        trades.put(trade.getTradeId(), trade);
        return trade;
    }

    public Trade executeTrade(String tradeId) {
        Trade trade = getTrade(tradeId);
        
        if (trade.getStatus() == TradeStatus.CONFIRMED) {
            throw new IllegalStateException("Trade already confirmed: " + tradeId);
        }
        
        if (trade.getStatus() == TradeStatus.REJECTED) {
            throw new IllegalStateException("Cannot execute rejected trade: " + tradeId);
        }

        if (marketDataService.simulateExecutionFailure()) {
            throw new RuntimeException("System failure during execution");
        }

        trade.setStatus(TradeStatus.EXECUTED);
        return trade;
    }

    public Trade confirmTrade(String tradeId) {
        Trade trade = getTrade(tradeId);
        
        if (trade.getStatus() != TradeStatus.EXECUTED) {
            throw new IllegalStateException("Trade must be EXECUTED before confirmation: " + tradeId);
        }

        trade.setStatus(TradeStatus.CONFIRMED);
        return trade;
    }

    public Trade cancelTrade(String tradeId) {
        Trade trade = getTrade(tradeId);
        
        if (trade.getStatus() == TradeStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel confirmed trade: " + tradeId);
        }

        trade.setStatus(TradeStatus.CANCELLED);
        return trade;
    }

    public Trade getTrade(String tradeId) {
        Trade trade = trades.get(tradeId);
        if (trade == null) {
            throw new NoSuchElementException("Trade not found: " + tradeId);
        }
        return trade;
    }

    public List<Trade> getTradeHistory() {
        return new ArrayList<>(trades.values());
    }

    private String generateTradeId() {
        return "TRD" + String.format("%08d", tradeIdCounter.getAndIncrement());
    }
}

