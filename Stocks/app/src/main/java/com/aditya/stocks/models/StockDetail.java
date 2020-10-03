package com.aditya.stocks.models;

/**
 * Represents a detailed stock ticker.
 */
public class StockDetail {
    private String id;
    private String name;
    private double price;
    private String[] companyType;
    private double allTimeHigh;
    private String address;
    private String imageUrl;
    private String website;

    public StockDetail(String id, String name, double price, String[] companyType, double allTimeHigh, String address, String imageUrl, String website) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.companyType = companyType;
        this.allTimeHigh = allTimeHigh;
        this.address = address;
        this.imageUrl = imageUrl;
        this.website = website;
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

    public String[] getCompanyType() {
        return companyType;
    }

    public String getAllTimeHigh() {
        return String.format("$%.2f", allTimeHigh);
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getWebsite() {
        return website;
    }
}
