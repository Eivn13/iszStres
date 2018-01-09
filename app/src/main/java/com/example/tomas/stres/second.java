package com.example.tomas.stres;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.net.wifi.WifiManager;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class second extends AppCompatActivity {


    public EditText sysTlak;
    public EditText diasTlak;
    public EditText tep;
    public EditText zataz;
    public String potiaze;
    public String aktivity;
    public String problemy;
    public String numOfWifi;
    public Button btn2;
    public RadioButton radioButton3;
    public RadioButton radioButton5;
    private WifiManager mainWifiObj;

    public void getInf(){

        sysTlak = (EditText)findViewById(R.id.editText);
        diasTlak = (EditText)findViewById(R.id.editText2);
        tep = (EditText)findViewById(R.id.editText3);
        zataz = (EditText)findViewById(R.id.editText6);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
        aktivity = "";
        numOfWifi = "0";
        btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( ( sysTlak.getText().toString().trim().equals("")) )
                {
                    Toast.makeText(getApplicationContext(), "Vyplnte systolicky tlak", Toast.LENGTH_SHORT).show();
                }
                else if( ( diasTlak.getText().toString().trim().equals("")) )
                {
                    Toast.makeText(getApplicationContext(), "Vyplnte diastolicky tlak", Toast.LENGTH_SHORT).show();
                }
                else if( ( tep.getText().toString().trim().equals("")) )
                {
                    Toast.makeText(getApplicationContext(), "Vyplnte tep", Toast.LENGTH_SHORT).show();
                }
                else if( ( zataz.getText().toString().trim().equals("")) )
                {
                    Toast.makeText(getApplicationContext(), "Kedy ste naposledy citili zataz ?", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //wifi
                    mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE); //pridane getApplicationContext
                    mainWifiObj.startScan();
                    WifiScanReceiver wifiReceiver = new WifiScanReceiver();
                    registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                    List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
                    if(wifiScanList.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Nenaslo ziadnu wifi.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        numOfWifi = String.valueOf(wifiScanList.size()); //TOTO sa bude posielat na server
                        String data = "Pocet wifi v blizkosti je " + numOfWifi;
                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                    }
                    //parse dat
                    potiaze = "Áno";
                    if(radioButton3.isChecked())
                        potiaze = "Nie";
                    problemy = "Áno";
                    if(radioButton5.isChecked())
                        problemy = "Nie";
                    aktivity = "";
                    List<CheckBox> aktivities = new ArrayList<>();
                    aktivities.add((CheckBox) findViewById(R.id.checkBox6));
                    aktivities.add((CheckBox) findViewById(R.id.checkBox7));
                    aktivities.add((CheckBox) findViewById(R.id.checkBox8));
                    aktivities.add((CheckBox) findViewById(R.id.checkBox9));
                    aktivities.add((CheckBox) findViewById(R.id.checkBox10));
                    for(CheckBox aktivit : aktivities){
                        if(aktivit.isChecked()){
                            aktivity = aktivity + aktivit.getText().toString() + " ";
                        }
                    }
                    //parse dat
                    final Intent intent = getIntent();
                    System.out.println(intent.getStringExtra("vek")); //vek
                    System.out.println(intent.getStringExtra("pohlavie")); //pohlavie
                    System.out.println(intent.getStringExtra("okres")); //okres
                    System.out.println(((EditText) findViewById(R.id.editText)).getText().toString()); //sysTlak
                    System.out.println(((EditText) findViewById(R.id.editText2)).getText().toString()); //diasTlak
                    System.out.println(((EditText) findViewById(R.id.editText3)).getText().toString()); //tep
                    System.out.println(((EditText) findViewById(R.id.editText6)).getText().toString()); //
                    System.out.println(aktivity);
                    String date = new Date().toString();
                    System.out.println(date);
                    String[] parts = date.split(" ");
                    final String cas = parts[3];
                    final String datum = parts[2] + " " + parts[1] + " " + parts[5];
                    System.out.println(datum + " " + cas);
                    //posielanie
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = "http://www.ecomamk.sk/new_predict.php";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("My success",""+response); //whatever

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.i("My error",""+error);
                            error.printStackTrace();
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> header = new HashMap<String, String>();
                            header.put("name", "Domino");
                            return header;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String,String> map = new HashMap<String, String>();
                            map.put("vek", intent.getStringExtra("vek"));
                            map.put("pohlavie", intent.getStringExtra("pohlavie"));
                            map.put("okres", intent.getStringExtra("okres"));
                            map.put("sysTlak", ((EditText) findViewById(R.id.editText)).getText().toString());
                            map.put("diasTlak", ((EditText) findViewById(R.id.editText2)).getText().toString());
                            map.put("tep", ((EditText) findViewById(R.id.editText3)).getText().toString());
                            map.put("zataz", ((EditText) findViewById(R.id.editText6)).getText().toString());
                            map.put("aktivity", aktivity);
                            map.put("datum", datum);
                            map.put("cas", cas);
                            map.put("wifi", numOfWifi);
                            return map;
                        }
                    };
                    queue.add(request);

                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getInf();
    }
}
