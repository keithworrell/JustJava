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
import android.widget.Toast;

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
        if ((action != null) && (mIntent != null) && (data != null)) {
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

    public String orderSummary() {
        String orderMessage = "Name: " + currentCustomer.name;
        orderMessage += "\n Order Number: " + currentCustomer.orderNumberAsString();
        orderMessage += "\n\n     Order Details -";
        orderMessage += "\n     " + currentCustomer.quantityOfCoffeeAsString() + " cups of coffee";

        // TODO: scalability issue foreseen here.

        boolean noAdditions = true;

        if (currentCustomer.hasWhippedCream) {
            orderMessage += "\n          add Whipped Cream";
            noAdditions = false;
        }
        if (currentCustomer.hasChocolate) {
            orderMessage += "\n          add Chocolate";
            noAdditions = false;
        }
        if (noAdditions) {
            orderMessage += "\n          no additions";
        }
        orderMessage += "\n\n Order Total: " + currentCustomer.getOrderTotal();

        return orderMessage;
    }

    @SuppressWarnings("unused") // Invoked by Order button
    public void submitOrder(View view) {
        String orderMessage = orderSummary();
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
                if (currentCustomer.quantityOfCoffee < 100) {
                    currentCustomer.quantityOfCoffee += 1;
                } else {
                    Toast.makeText(this, "At this time we are limiting cups of coffee per order to 99.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.less_coffee:
                if (currentCustomer.quantityOfCoffee > 1) {
                    currentCustomer.quantityOfCoffee -= 1;
                } else {
                    Toast.makeText(this, "If you want less then one cup of coffee, I don't know why you are here.", Toast.LENGTH_SHORT).show();
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
            case (R.id.chocolate_checkbox):
                currentCustomer.hasChocolate = itemAdded.isChecked();
                break;
        }
        display();
    }

    @SuppressWarnings("unused") // Invoked by Send Order button
    public void emailOrder(View view) {
        String[] address = {"keith.worrell@gmail.com"};
        String subject = "Coffee Order for " + currentCustomer.name;
        String orderMessage = orderSummary();
        composeEmail(address, subject, orderMessage);
    }

    public void composeEmail(String[] addresses, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
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
            quantityTextView.setText(currentCustomer.quantityOfCoffeeAsString());
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
        CheckBox chocolate = (CheckBox) findViewById(
                R.id.chocolate_checkbox);
        if (chocolate != null) {
            chocolate.setChecked(currentCustomer.hasChocolate);
        } else {
            somethingWentWrong("display", "chocolate_checkbox null; impossible?");
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