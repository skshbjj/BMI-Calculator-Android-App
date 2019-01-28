package com.sakshibajaj.bmicalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends AppCompatActivity {
    TextView tvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("History");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tvHistory = findViewById(R.id.tvHistory);
        tvHistory.setMovementMethod(new ScrollingMovementMethod());
        String data = SecondActivity.db.view();
        if(data.length()==0)
            Toast.makeText(this, "No History", Toast.LENGTH_SHORT).show();
        else {
            tvHistory.setText(data);
        }

    }
}
