package com.mocktrading.model;

import java.time.LocalDateTime;

public class MarketData {
    private String isin;
    private double bidPrice;
    private double askPrice;
    private double lastPrice;
    private LocalDateTime timestamp;

    public MarketData(String isin, double bidPrice, double askPrice, double lastPrice) {
        this.isin = isin;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.lastPrice = lastPrice;
        this.timestamp = LocalDateTime.now();
    }

    public String getIsin() {
        return isin;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getMidPrice() {
        return (bidPrice + askPrice) / 2.0;
    }

    @Override
    public String toString() {
        return String.format("MarketData{isin='%s', bid=%.2f, ask=%.2f, last=%.2f}", 
            isin, bidPrice, askPrice, lastPrice);
    }
}

