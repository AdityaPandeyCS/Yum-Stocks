package com.aditya.stocks.models;

import androidx.annotation.Nullable;

/**
 * Represents a detailed stock ticker.
 */
public class StockTicker {
    private String id;
    private String name;
    private double prevPrice;
    private double price;
    private String[] companyType;

    public StockTicker(String id, String name, double price, String[] companyType) {
        this.id = id;
        this.name = name;
        this.prevPrice = price;
        this.price = price;
        this.companyType = companyType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return String.format("$%.2f", price);
    }

    private double roundToHundredths(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }

    public double getChange() {
        // gson.fromJson() doesn't use constructor,
        // so handle uninitialized prevPrice
        if (prevPrice == 0) {
            prevPrice = price;
        }

        return roundToHundredths(price) - roundToHundredths(prevPrice);
    }

    public String[] getCompanyType() {
        return companyType;
    }

    public void update(StockTicker updatedTicker) {
        this.name = updatedTicker.name;
        this.prevPrice = this.price;
        this.price = updatedTicker.price;
        this.companyType = updatedTicker.companyType;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        StockTicker other = (StockTicker) obj;
        return this.id.equals(other.id);
    }
}
