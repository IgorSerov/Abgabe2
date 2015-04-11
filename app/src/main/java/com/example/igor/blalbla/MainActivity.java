package com.example.igor.blalbla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private EditText editText;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private static final String URL = "http://api.fixer.io/latest";
    private String geld;
    private double course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DatenLesen().execute();
        Button startButton = (Button) findViewById(R.id.button);
        spinner = (Spinner) findViewById(R.id.spinner);
        editText = (EditText) findViewById(R.id.editText);
        startButton.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };

        // Erstellung eine Liste mit allen Währungen
        adapter.add("AUD");
        adapter.add("BGN");
        adapter.add("BRL");
        adapter.add("CAD");
        adapter.add("CHF");
        adapter.add("CNY");
        adapter.add("CZK");
        adapter.add("DKK");
        adapter.add("GBP");
        adapter.add("HKD");
        adapter.add("HRK");
        adapter.add("HUF");
        adapter.add("IDR");
        adapter.add("ILS");
        adapter.add("INR");
        adapter.add("JPY");
        adapter.add("KRW");
        adapter.add("MXN");
        adapter.add("MYR");
        adapter.add("NOK");
        adapter.add("NZD");
        adapter.add("PHP");
        adapter.add("PLN");
        adapter.add("RON");
        adapter.add("RUB");
        adapter.add("SEK");
        adapter.add("SGD");
        adapter.add("THB");
        adapter.add("TRY");
        adapter.add("USD");
        adapter.add("ZAR");
        adapter.add("please select a currency");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
        spinner.setOnItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /*

    Berechnen das Resultat und aufrufen Endbildschirm
    */
    @Override
    public void onClick(View v) {

        try {
            double anzahl = Double.parseDouble(editText.getText().toString());
            double antwort = course * anzahl;
            if (course == 0)
                throw new Exception("bad");
            Intent intent = new Intent(this.getApplicationContext(), ResultActivity.class);
            intent.putExtra("result", antwort);
            intent.putExtra("geld", geld);
            intent.putExtra("start", anzahl);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
        }
        //  System.out.println(anzahl+" "+course);


    }
    /*

        Auswahl eine Währung aus der Liste
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        geld = parent.getItemAtPosition(position).toString();
        try {
            if (jsonObject != null)
                course = jsonObject.getDouble(geld);
        } catch (JSONException e) {
            Toast.makeText(MainActivity.this, R.string.incorrect_toast1, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*

    Lesen Daten aus fixer.io
     */
    private class DatenLesen extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading Data!!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpEntity entity;
                HttpResponse response;
                HttpGet get = new HttpGet(URL);
                response = client.execute(get);
                entity = response.getEntity();
                String text = EntityUtils.toString(entity);

                JSONObject object = new JSONObject(text);
                jsonObject = object.getJSONObject("rates");

               /* Map<String, Double> map = new HashMap<String, Double>();
                Iterator<String> iterator = jsonObject.keys();
                    while(iterator.hasNext()){
                        String key = (String)iterator.next();
                        Double value = Double.parseDouble(jsonObject.getString(key));
                        map.put(key,value);
                    }
                    for (String a: map.keySet())
                        waehrungsName.add(a);
                    waehrungsName.add("please select a currency");*/


            } catch (Exception e) {
                Toast.makeText(MainActivity.this, R.string.incorrect_toast1, Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
