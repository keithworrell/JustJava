package com.example.android.justjava;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * This app displays an order form to order coffee.
 */
@SuppressWarnings("unchecked")
public class MainActivity extends ActionBarActivity {
    public List customers = new LinkedList();
    public Order currentCustomer = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creates first customer; currentCustomer
        newOrder();

        final EditText meditText = (EditText) findViewById(R.id.customer_name_edittext);
        meditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // you can call or do what you want with your EditText here
                currentCustomer.name = meditText.getText().toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }
        });
    }

    private void newOrder() {
        Order newCustomer = new Order();
        customers.add(newCustomer);
        currentCustomer = (Order) customers.get(customers.size() - 1);
        currentCustomer.orderNumber = customers.size();
        display();
    }

    public void submitOrder(View view) {
        String orderMessage = "Name: " + currentCustomer.name;
        orderMessage += "\n Order Number: " + currentCustomer.orderNumberAsString();
        orderMessage += "\n\n     Order Details -";
        orderMessage += "\n     " + currentCustomer.quantityOfCoffeeAsString() + " cups of coffee";
        if (currentCustomer.hasWhippedCream) {
            orderMessage += "\n          add Whipped Cream";
        } else {
            orderMessage += "\n          no additions";
        }
        orderMessage += "\n\n Order Total: " + currentCustomer.getOrderTotal();

        TextView orderSummaryMessage = (TextView) findViewById(
                R.id.order_summary_text_view);
        TextView orderSummaryTitle = (TextView) findViewById(
                R.id.order_heading_text_view);
        Button submitOrder = (Button) findViewById(
                R.id.submit_order_button);
        TextView priceTextView = (TextView) findViewById(
                R.id.price_text_view);
        Button newCustomer = (Button) findViewById(
                R.id.new_customer_button);

        orderSummaryTitle.setVisibility(View.VISIBLE);
        orderSummaryMessage.setText(orderMessage);
        orderSummaryMessage.setVisibility(View.VISIBLE);
        submitOrder.setVisibility(View.GONE);
        priceTextView.setVisibility(View.GONE);
        newCustomer.setVisibility(View.VISIBLE);
    }

    public void increment(View view) {
        switch (view.getId()) {
            case R.id.more_coffee:
                currentCustomer.quantityOfCoffee += 1;
                break;
            case R.id.less_coffee:
                currentCustomer.quantityOfCoffee -= 1;
                if (currentCustomer.quantityOfCoffee < 0) {
                    currentCustomer.quantityOfCoffee = 0;
                }
                ;
                break;
        }
        display();
    }

    public void addItem(View view) {
        CheckBox itemAdded = (CheckBox) view;

        switch (itemAdded.getId()) {
            case (R.id.whipped_cream_checkbox):
                currentCustomer.hasWhippedCream = itemAdded.isChecked();
                break;
        }
        display();
    }

    public void newCustomer(View view) {
        TextView orderSummaryMessage = (TextView) findViewById(
                R.id.order_summary_text_view);
        TextView orderSummaryTitle = (TextView) findViewById(
                R.id.order_heading_text_view);
        Button newCustomer = (Button) findViewById(
                R.id.new_customer_button);
        Button submitOrder = (Button) findViewById(
                R.id.submit_order_button);
        TextView priceTextView = (TextView) findViewById(
                R.id.price_text_view);

        orderSummaryTitle.setVisibility(View.GONE);
        orderSummaryMessage.setVisibility(View.GONE);
        newCustomer.setVisibility(View.GONE);
        submitOrder.setVisibility(View.VISIBLE);
        priceTextView.setVisibility(View.VISIBLE);

        newOrder();
        display();
    }

    /**
     * This method displays current Order information on the screen.
     */
    private void display() {
        EditText customerName = (EditText) findViewById(
                R.id.customer_name_edittext);
        customerName.setText("" + currentCustomer.name);

        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + currentCustomer.quantityOfCoffee);

        CheckBox whippedCream = (CheckBox) findViewById(
                R.id.whipped_cream_checkbox);
        whippedCream.setChecked(currentCustomer.hasWhippedCream);

        TextView priceTextView = (TextView) findViewById(
                R.id.price_text_view);
        priceTextView.setText(currentCustomer.getOrderTotal());
    }
}