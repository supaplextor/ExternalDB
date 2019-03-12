package com.example.externaldb;


import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

// Selection of the spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> drivers = new ArrayList<String>();
        for (Enumeration<Driver> e = DriverManager.getDrivers(); e.hasMoreElements();)
        {
            drivers.add(e.nextElement().getClass().getName());
        }
        spinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, drivers));

        final Button button2 = findViewById(R.id.button2);
        final Connection[] connection = {null};

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Spinner spinner = (Spinner) findViewById(R.id.spinner);
                    EditText et_jdbc_string = (EditText) findViewById(R.id.jdbc_string);
                    EditText et_username = (EditText) findViewById(R.id.username);
                    EditText et_password = (EditText) findViewById(R.id.password);

                    String driverName = spinner.getSelectedItem().toString();
                    String jdbc_s;
                    String username;
                    String password;

                    jdbc_s = String.valueOf(et_jdbc_string.getText());
                    username = String.valueOf(et_username.getText());
                    password = String.valueOf(et_password.getText());



                    Intent myIntent = new Intent(MainActivity.this, query.class);
                    myIntent.putExtra("driverName", driverName);
                    myIntent.putExtra("jdbc_s", jdbc_s);
                    myIntent.putExtra("username", username);
                    myIntent.putExtra("password", password);
                    MainActivity.this.startActivity(myIntent);

                } catch (Exception e) {

                    return;
                }
            }
        });



    }

}

