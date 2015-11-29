package com.ola.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olaskierbiszewska on 14.11.15.
 */
public class Cart {
    private Integer id;
    private List<CartItem> cartItemsList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CartItem> getCartItemsList() {
        return cartItemsList;
    }

    public void setCartItemsList(List<CartItem> cartItemsList) {
        this.cartItemsList = cartItemsList;
    }

    public double getCartValue() {
        double value = 0;
        for(CartItem item : this.cartItemsList){
            value += item.getValue();
        }
        return value;
    }
}
