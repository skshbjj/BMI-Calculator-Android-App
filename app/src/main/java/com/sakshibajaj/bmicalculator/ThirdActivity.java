package com.sakshibajaj.bmicalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ThirdActivity extends AppCompatActivity {

    TextView textView, tv1, tv2, tv3, tv4;
    Button btnBack , btnShare, btnSave;
    static DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        textView = findViewById(R.id.textView);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4  = findViewById(R.id.tv4);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnShare = findViewById(R.id.btnShare);
        db = new DBHandler(this);
        DateFormat df = new SimpleDateFormat("EEE , dd-MMM-yyyy, HH:mm:ss");
        final String date = df.format(Calendar.getInstance().getTime());


        final Intent i3 = getIntent();
        final String bmi = i3.getStringExtra("bmi");

        final SharedPreferences sp1 = getSharedPreferences("MyP",Context.MODE_PRIVATE);
        final double b = Double.parseDouble(sp1.getString("bmi", bmi));
        final String t;

        if(b < 18.5){
            tv1.setTextColor(Color.RED);
            t = "Underweight.";
            textView.setText("Your BMI is: " +b+ "\nYou are "+t);
        }
        else if(b >= 18.5 && b < 25 ){
            t = "Normal.";
            tv2.setTextColor(Color.RED);
            textView.setText("Your BMI is: " +b+ "\nYou are "+t);
        }
        else if(b >= 25 && b < 30){
            tv3.setTextColor(Color.RED);
            t = "Overweight.";
            textView.setText("Your BMI is: " +b+ "\nYou are "+t);
        }
        else{
            tv4.setTextColor(Color.RED);
            t = "Obese.";
            textView.setText("Your BMI is: " +b+ "\nYou are "+t);
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ThirdActivity.this, SecondActivity.class);
                startActivity(i);
            }
        });
                btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("MyP",Context.MODE_PRIVATE);
                String name = sp.getString("n", "");
                String age = sp.getString("a","");
                String ph = sp.getString("p", "");
                String sex = sp.getString("s","");
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("t",t);
                editor.commit();
                final double b = Double.parseDouble(sp1.getString("bmi", bmi));
                Intent i1 = new Intent();
                i1.setAction(Intent.ACTION_SEND);
                i1.putExtra(Intent.EXTRA_TEXT, "Name is "+name+"\nSex: "+sex+"\nAge is "+age+"\nPhone number is "+ph+
                        "\nBMI is "+b+"\n"+t );
                i1.setType("text/plain");
                startActivity(i1);

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("MyP",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                double b = Double.parseDouble(sp1.getString("bmi", bmi));
                editor.commit();
                db.add(date,b);
                }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Attention!")
                .setMessage("Are you sure you want to exit?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        }).setNegativeButton("No",null).show();
    }
}
