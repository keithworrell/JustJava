package com.example.android.justjava;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by keith on 3/26/2016.
 *
 * This class defines the order object
 */
class Order {
    // TODO: MENU ITEMS - separate customer information and item information by putting specific items in to a linked list that contains MenuItem Objects. Add new button to xml that adds item to the linked list separate from submit order.
    // TODO: ORDER PREVIEW - Create an order preview page that allows previously added items to be removed

    private final static double priceOfCoffee = 5.25d;
    private final static double priceOfWhippedCream = 0.50d;

    String name = "Anonymous";
    int orderNumber = 0;
    int quantityOfCoffee = 0;
    boolean hasWhippedCream = false;

    public String getOrderTotal() {
        Double pricePerCup = priceOfCoffee;
        if (this.hasWhippedCream) {
            pricePerCup += priceOfWhippedCream;
        }

        double total = this.quantityOfCoffee * pricePerCup;

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormatter.format(total);
    }

    public String orderNumberAsString() {
        return Integer.toString(orderNumber);
    }

    public String quantityOfCoffeeAsString() {
        return Integer.toString(quantityOfCoffee);
    }
}