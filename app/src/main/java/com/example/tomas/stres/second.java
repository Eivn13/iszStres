package com.example.tomas.stres;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.net.wifi.WifiManager;

import java.util.EmptyStackException;
import java.util.List;

public class second extends AppCompatActivity {


    public EditText sysTlak;
    public EditText diasTlak;
    public EditText tep;
    public EditText zataz;
    public Button btn2;
    private WifiManager mainWifiObj;

    public void getInf(){

        sysTlak = (EditText)findViewById(R.id.editText);
        diasTlak = (EditText)findViewById(R.id.editText2);
        tep = (EditText)findViewById(R.id.editText3);
        zataz = (EditText)findViewById(R.id.editText6);
        btn2 = (Button)findViewById(R.id.button);
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
                    //odosli();
                    mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    mainWifiObj.startScan();
                    WifiScanReceiver wifiReceiver = new WifiScanReceiver();
                    registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                    List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
                    if(wifiScanList.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Nenaslo ziadnu wifi.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String data = wifiScanList.get(0).toString();
                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show(); //skusobny vypis, este nepretestovane
                    }
                    //parse dat a posielanie dat
                    //viac menej staci poslat serveru views od nic po 6 aka findViewById(R.id.editText) to findViewById(R.id.editText6) okrem jednotky
                    Intent intent = getIntent();
                    System.out.println(intent.getStringExtra("vek")); //vek
                    System.out.println(intent.getStringExtra("pohlavie")); //pohlavie
                    System.out.println(intent.getStringExtra("okres")); //okres
                    System.out.println(((EditText) findViewById(R.id.editText)).getText().toString()); //sysTlak
                    System.out.println(((EditText) findViewById(R.id.editText2)).getText().toString()); //diasTlak
                    System.out.println(((EditText) findViewById(R.id.editText3)).getText().toString()); //tep
                    System.out.println(((EditText) findViewById(R.id.editText6)).getText().toString()); //zataz

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
