package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * This app displays an order form to order coffee.
 */
@SuppressWarnings("unchecked")
public class MainActivity extends AppCompatActivity {
    private final List customers = new LinkedList();
    private Order currentCustomer = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Something something Google.
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        final TextView mIntent = (TextView) findViewById(R.id.intent);
        if ((action != null) && (mIntent != null)) {
            String message = "I don't know what " + action + " is, but thank you for visiting from " + data.toString();
            mIntent.setText(message);
            mIntent.setVisibility(View.VISIBLE);
        }

        //Creates first customer; currentCustomer
        newOrder();

        final EditText mEditText = (EditText) findViewById(R.id.customer_name_edit_text);
        if (mEditText != null) {
            mEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    // you can call or do what you want with your EditText here
                    currentCustomer.name = mEditText.getText().toString();
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //do nothing
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //do nothing
                }
            });
        } else {
            somethingWentWrong("mEditText listener", "customer_name_edit_text null; impossible?");
        }
    }

    private void newOrder() {
        Order newCustomer = new Order();
        customers.add(newCustomer);
        currentCustomer = (Order) customers.get(customers.size() - 1);
        currentCustomer.orderNumber = customers.size();
        display();
    }

    @SuppressWarnings("unused") // Invoked by Order button
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

        setContentView(R.layout.order_summary);

        TextView orderSummaryMessage = (TextView) findViewById(
                R.id.order_summary_text_view);
        if (orderSummaryMessage != null) {
            orderSummaryMessage.setText(orderMessage);
        } else {
            somethingWentWrong("submitOrder", "orderSummaryMessage null; impossible?");
        }
    }

    @SuppressWarnings("unused") // Invoked by Increment buttons
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
                break;
        }
        display();
    }

    @SuppressWarnings("unused") // Invoked by Item Checkboxes
    public void addItem(View view) {
        CheckBox itemAdded = (CheckBox) view;

        switch (itemAdded.getId()) {
            case (R.id.whipped_cream_checkbox):
                currentCustomer.hasWhippedCream = itemAdded.isChecked();
                break;
        }
        display();
    }

    @SuppressWarnings("unused") // Invoked by New Customer button
    public void newCustomer(View view) {
        setContentView(R.layout.activity_main);

        newOrder();
        display();
    }

    private void display() {
        EditText customerName = (EditText) findViewById(
                R.id.customer_name_edit_text);
        if (customerName != null) {
            customerName.setText(currentCustomer.name);
        } else {
            somethingWentWrong("display", "customer_edit_text null; impossible?");
        }

        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        if (quantityTextView != null) {
            quantityTextView.setText(currentCustomer.quantityOfCoffee);
        } else {
            somethingWentWrong("display", "quantity_text_view null; impossible?");
        }

        CheckBox whippedCream = (CheckBox) findViewById(
                R.id.whipped_cream_checkbox);
        if (whippedCream != null) {
            whippedCream.setChecked(currentCustomer.hasWhippedCream);
        } else {
            somethingWentWrong("display", "whipped_cream_checkbox null; impossible?");
        }

        TextView priceTextView = (TextView) findViewById(
                R.id.price_text_view);
        if (priceTextView != null) {
            priceTextView.setText(currentCustomer.getOrderTotal());
        } else {
            somethingWentWrong("Display", "price_text_view null; impossible?");
        }
    }

    private void somethingWentWrong(String method, String message) {
        Log.e(method, message);
        System.exit(-1);
    }
}