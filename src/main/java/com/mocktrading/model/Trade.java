package com.mocktrading.model;

import com.mocktrading.enums.TradeStatus;
import java.time.LocalDateTime;

public class Trade {
    private String tradeId;
    private String isin;
    private int quantity;
    private double price;
    private TradeStatus status;
    private LocalDateTime timestamp;
    private String rejectionReason;

    public Trade(String tradeId, String isin, int quantity, double price) {
        this.tradeId = tradeId;
        this.isin = isin;
        this.quantity = quantity;
        this.price = price;
        this.status = TradeStatus.CREATED;
        this.timestamp = LocalDateTime.now();
    }

    public String getTradeId() {
        return tradeId;
    }

    public String getIsin() {
        return isin;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public TradeStatus getStatus() {
        return status;
    }

    public void setStatus(TradeStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Override
    public String toString() {
        return String.format("Trade{tradeId='%s', isin='%s', quantity=%d, price=%.2f, status=%s}", 
            tradeId, isin, quantity, price, status);
    }
}

