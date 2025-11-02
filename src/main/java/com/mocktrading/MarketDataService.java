package com.mocktrading;

import com.mocktrading.model.MarketData;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

public class MarketDataService {
    private final Map<String, MarketData> marketDataCache;
    private final Random random;
    private final double failureRate;
    private static final double MIN_PRICE = 90.0;
    private static final double MAX_PRICE = 110.0;

    public MarketDataService() {
        this(new Random(), 0.1);
    }

    public MarketDataService(Random random, double failureRate) {
        this.marketDataCache = new ConcurrentHashMap<>();
        this.random = random;
        this.failureRate = failureRate;
    }

    public MarketData getMarketData(String isin) {
        MarketData data = marketDataCache.get(isin);
        if (data == null || isStale(data)) {
            data = generateMarketData(isin);
            marketDataCache.put(isin, data);
        }
        return data;
    }

    private MarketData generateMarketData(String isin) {
        double basePrice = MIN_PRICE + (MAX_PRICE - MIN_PRICE) * random.nextDouble();
        double spread = basePrice * 0.001;
        double bidPrice = basePrice - spread / 2;
        double askPrice = basePrice + spread / 2;
        double lastPrice = bidPrice + (askPrice - bidPrice) * random.nextDouble();
        
        return new MarketData(isin, bidPrice, askPrice, lastPrice);
    }

    public boolean validatePriceDeviation(String isin, double tradePrice, double thresholdPercent) {
        MarketData data = getMarketData(isin);
        double midPrice = data.getMidPrice();
        double deviation = Math.abs((tradePrice - midPrice) / midPrice * 100);
        return deviation <= thresholdPercent;
    }

    public boolean simulateExecutionFailure() {
        return random.nextDouble() < failureRate;
    }

    private boolean isStale(MarketData data) {
        return java.time.Duration.between(data.getTimestamp(), java.time.LocalDateTime.now())
                .getSeconds() > 60;
    }
}

