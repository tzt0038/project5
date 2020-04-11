package com.example.hw5;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb = new DatabaseHelper(this);
    DecimalFormat df = new DecimalFormat("0.00");

    TextView tvBalance;
    Button btnAdd;
    Button btnSub;
    EditText etDate;
    EditText etAmount;
    EditText etCategory;
    TableLayout history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        tvBalance = (TextView) findViewById(R.id.tvBalance);
        etDate = (EditText) findViewById(R.id.date);
        etAmount = (EditText) findViewById(R.id.amount);
        etCategory = (EditText) findViewById(R.id.category);
        btnAdd = (Button) findViewById(R.id.add);
        btnSub = (Button) findViewById(R.id.minus);
        history = (TableLayout) findViewById(R.id.tbHistory);

        AddTransactions();
        ViewAll();
    }

    public void AddTransactions() {

        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double amt = Double.parseDouble(etAmount.getText().toString());

                        TransactionModel trans = new TransactionModel();

                        trans.mDate = etDate.getText().toString();
                        trans.mAmount = amt;
                        trans.mCategory = etCategory.getText().toString();


                        boolean isInserted = myDb.insertTransaction(trans);

                        if (isInserted) {
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Date not Inserted", Toast.LENGTH_LONG).show();
                        }
                        ViewAll();
                    }
                }
        );

        btnSub.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double amt = Double.parseDouble(etAmount.getText().toString());

                        if (amt > 0) {
                            amt *= -1;
                        }

                        TransactionModel trans = new TransactionModel();

                        trans.mDate = etDate.getText().toString();
                        trans.mAmount = amt;
                        trans.mCategory = etCategory.getText().toString();

                        boolean isInserted = myDb.insertTransaction(trans);

                        if (isInserted) {
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Date not Inserted", Toast.LENGTH_LONG).show();
                        }
                        ViewAll();
                    }
                }
        );
    }


    public void ViewAll() {
        ClearTable();
        Cursor cursor = myDb.getAllData();
        Double bal = 0.0;

        while(cursor.moveToNext()) {
            TableRow tr = new TableRow(this);
            TableRow.LayoutParams columnLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            columnLayout.weight = 1;

            TextView date = new TextView(this);
            date.setLayoutParams(columnLayout);
            date.setText(cursor.getString(1));
            tr.addView(date);

            TextView amount = new TextView(this);
            amount.setLayoutParams(columnLayout);
            amount.setText(cursor.getString(2));
            tr.addView(amount);

            TextView category = new TextView(this);
            category.setLayoutParams(columnLayout);
            category.setText(cursor.getString(3));
            tr.addView(category);

            history.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            double temp = Double.parseDouble(cursor.getString(2));
            bal += temp;
        }
        String curBalance = "Current Balance: $" + df.format(bal).toString();
        MainActivity.this.tvBalance.setText(curBalance);
    }

    public void ClearTable() {
        int numOfTable = history.getChildCount();
        for (int i = 1; i < numOfTable; i++){
            history.removeViewAt(i);
        }
    }
}