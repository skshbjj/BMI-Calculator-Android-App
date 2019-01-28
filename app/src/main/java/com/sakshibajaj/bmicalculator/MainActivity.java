package com.sakshibajaj.bmicalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etName, etAge, etPhone;
    RadioGroup rgSex;
    Button btnSubmit;
    SharedPreferences sp;
    RadioButton radioButton,radioButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etPhone = findViewById(R.id.etPhone);
        btnSubmit = findViewById(R.id.btnSubmit);
        rgSex = findViewById(R.id.rgSex);
        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        setTitle("Personal Details");
        sp = getSharedPreferences("MyP", MODE_PRIVATE);
        if(sp.getBoolean("c",false)==false) {

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etName.getText().toString();
                    String age = etAge.getText().toString();
                    String phone = etPhone.getText().toString();
                    if (name.length() == 0) {
                        etName.setError("Please enter a name");
                        etName.requestFocus();
                        return;
                    }
                    if (age.length() == 0) {
                        etAge.setError("Please enter an age");
                        etAge.requestFocus();
                        return;
                    }
                    if (phone.length() == 0) {
                        etPhone.setError("Please enter a number");
                        etPhone.requestFocus();
                        return;
                    }
                    if (!name.matches("[a-zA-Z ]*")) {
                        etName.setError("Invalid name");
                        etName.requestFocus();
                        return;
                    }
                    if (Integer.parseInt(age) > 120) {
                        etAge.setError("Invalid age");
                        etAge.requestFocus();
                        return;
                    }
                    if (phone.length() != 10) {
                        etPhone.setError("Invalid number");
                        etPhone.requestFocus();
                        return;
                    }
                    if ((!radioButton.isChecked()) && (!radioButton2.isChecked())) {
                        Toast.makeText(MainActivity.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        String sex = ((RadioButton) findViewById(rgSex.getCheckedRadioButtonId())).getText().toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("s", sex);
                        editor.apply();
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("a", age);
                    editor.putString("n", name);
                    editor.putString("p", phone);
                    editor.putBoolean("c", true);
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Submitted successfully", Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etAge.setText("");
                    etPhone.setText("");
                    Intent i = new Intent(MainActivity.this, SecondActivity.class);
                    i.putExtra("n", name);
                    i.putExtra("a", age);
                    i.putExtra("p", phone);
                    startActivity(i);
                    finish();
                }
            });
        }
        else{
            Intent a = new Intent(this, SecondActivity.class);
            startActivity(a);
            finish(); }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.about){
            Toast.makeText(this, "Created by Sakshi Bajaj", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId()==R.id.website){
            Intent w = new Intent(Intent.ACTION_VIEW);
            w.setData(Uri.parse("http://"+ "www.stackoverflow.com"));
            startActivity(w); }
        return super.onOptionsItemSelected(item);
    }
}