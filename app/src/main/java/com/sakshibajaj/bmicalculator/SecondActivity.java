package com.sakshibajaj.bmicalculator;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    TextView tvWelcome, tvLocation;
    public TextView tvTemp;
    Spinner sFeet, sInches;
    Button btnView, btnCalculate;
    EditText etWeight;
    GoogleApiClient gac;
    Location loc;
    static DBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tvWelcome = findViewById(R.id.tvWelcome);
        sFeet = findViewById(R.id.sFeet);
        sInches = findViewById(R.id.sInches);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnView = findViewById(R.id.btnView);
        etWeight = findViewById(R.id.etWeight);
        tvLocation = findViewById(R.id.tvLocation);
        tvTemp = findViewById(R.id.tvTemp);
        db = new DBHandler(this);

        final ArrayList<String> height = new ArrayList<String>();
        height.add("1");
        height.add("2");
        height.add("3");
        height.add("4");
        height.add("5");
        height.add("6");
        height.add("7");

        ArrayAdapter<String> heightAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, height);

        sFeet.setAdapter(heightAdapter);
        sFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                String f = parent.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                }
        });


        final ArrayList<String> Inches = new ArrayList<String>();
        Inches.add("1");
        Inches.add("2");
        Inches.add("3");
        Inches.add("4");
        Inches.add("5");
        Inches.add("6");
        Inches.add("7");
        Inches.add("8");
        Inches.add("9");
        Inches.add("10");
        Inches.add("11");
        Inches.add("12");

        ArrayAdapter<String> InchesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Inches);
        sInches.setAdapter(InchesAdapter);
        sInches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i2, long id) {
                String i1 = parent.getItemAtPosition(i2).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Intent i = getIntent();
        final String name = i.getStringExtra("n");

        final SharedPreferences sp = getSharedPreferences("MyP", Context.MODE_PRIVATE);

        tvWelcome.setText("Welcome " + sp.getString("n", name));

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos1 = sFeet.getSelectedItemPosition();
                int pos2 = sInches.getSelectedItemPosition();
                int feet = Integer.parseInt(height.get(pos1));
                int inch = Integer.parseInt(Inches.get(pos2));
                String we = etWeight.getText().toString();
                if (we.length() == 0)
                    etWeight.setError("Please enter your weight");
                else {
                    double w = Double.parseDouble(we);
                    double h = (feet * 0.3048 + inch * 0.0254);
                    double bmi = w / (h * h);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("bmi", String.valueOf(bmi));
                    editor.apply();

                    Toast.makeText(SecondActivity.this, "Submitted successfully", Toast.LENGTH_SHORT).show();
                    etWeight.setText("");
                    Intent i3 = new Intent(SecondActivity.this, ThirdActivity.class);
                    i3.putExtra("bmi", bmi);
                    startActivity(i3);
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecondActivity.this, HistoryActivity.class);
                startActivity(i);

            }
        });
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        gac = builder.build();
        gac.connect();
     }

    @Override
    public void onBackPressed() {
        SharedPreferences sp = getSharedPreferences("MyP",Context.MODE_PRIVATE);
        if(sp.getBoolean("c",false)==true){
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Attention!")
                    .setMessage("Are you sure you want to exit?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            }).setNegativeButton("No",null).show();
        }
        else{}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            Toast.makeText(this, "Created by Sakshi Bajaj", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.website) {
            Intent w = new Intent(Intent.ACTION_VIEW);
            w.setData(Uri.parse("http://" + "www.stackoverflow.com"));
            startActivity(w);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (gac != null)
            gac.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (gac != null)
            gac.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){
            return;
            }

        loc = LocationServices.FusedLocationApi.getLastLocation(gac);
        if(loc!=null){
            double lat = loc.getLatitude();
            double lon = loc.getLongitude();
            Geocoder g = new Geocoder(SecondActivity.this, Locale.ENGLISH);
            try {
                List<Address> addressList = g.getFromLocation(lat,lon,1);
                Address address = addressList.get(0);
                String add = address.getLocality()+", "+address.getSubAdminArea()+", "+address.getAdminArea()+", "+address.getPostalCode();
                tvLocation.setText(add);
                String web = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric";
                //String que = "&q=" + address.getLocality();
                String api = "&APPID=8d5b1c784f311fd04cfb347350de2435";
                String info = web + api;
                new MyTask(tvTemp).execute(info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(this, "Please enable GPS/Use in open area to get Location", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_SHORT).show();

    }

}

class MyTask extends AsyncTask<String,Void,Double>
{
double temp;
private TextView msg;

    public MyTask(TextView msg) {
        this.msg = msg;
    }

    @Override
    protected Double doInBackground(String... strings) {
        String json ="", line = "";
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            while ((line = br.readLine()) !=null){
                json = json + line + "\n";
            }
            JSONObject o = new JSONObject(json);
            JSONObject p = o.getJSONObject("main");
            temp = p.getDouble("temp");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;

    }

    @Override
    protected void onPostExecute(Double aDouble) {
        super.onPostExecute(aDouble);
        msg.setText("Temp:"+temp+"C");
    }
}

