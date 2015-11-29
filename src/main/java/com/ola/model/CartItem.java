package com.ola.model;

import java.text.DecimalFormat;

/**
 * Created by olaskierbiszewska on 14.11.15.
 */
public class CartItem {
    private Integer productId;
    private String name;
    private double price;
    private Integer amount;
    private double value;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double cena) {
        this.price = cena;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public double getValue() {
        double finalValue = Math.round( price * amount * 100.0 ) / 100.0;
        return finalValue;
    }
}

