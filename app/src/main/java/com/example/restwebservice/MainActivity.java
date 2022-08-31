package com.example.restwebservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private Button btnSubmit;
    String responseText=new String();
    StringBuffer response;
    URL url;
    Activity activity;
    ArrayList<Country> countries=new ArrayList<Country>();
    private ProgressDialog progressDialog;
    ListView listView;
    // In case if you deploy rest web service, then use below link and replace below ip address with yours
    //http://192.168.2.22:8080/JAXRSJsonExample/rest/countries

    //Direct Web services URL
    private String path = "https://restcountries.com/v2/all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        listView = (ListView) findViewById(android.R.id.list);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countries.clear();
                //Call WebService
                new GetServerData().execute();
            }
        });
    }

    class GetServerData extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching conntry data");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return getWebServiceResponseData();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            // For populating list data
            CustomCountryList customCountryList = new CustomCountryList(activity, countries);
            listView.setAdapter(customCountryList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Toast.makeText(getApplicationContext(),"You Selected "+countries.get(position).getCountryName()+ " as Country", Toast.LENGTH_SHORT).show();        }
            });
        }
    }
    protected Void getWebServiceResponseData() {

        try {

            url = new URL(path);
            Log.d("TAG", "ServerData: " + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d("TAG","Connection: "+conn);
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();

            Log.d("TAG", "Response code: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                // Reading response from input Stream
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String output;
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        responseText = response.toString();
        //Call ServerData() method to call webservice and store result in response
         // response = service.ServerData(path, postDataParams);
        Log.d("TAG", "data:" + responseText);
        try {
            JSONArray jsonarray = new JSONArray(responseText);
            int jsonarraysize=jsonarray.length();
            Log.d("tag","json array length: "+jsonarraysize);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String cap="";
                //if value of capital is not available in API
                try {
                cap = jsonobject.getString("capital");}
                catch (JSONException e)
                {
                    cap="";
                    Log.d("tag","inner catch for capital");
                    e.printStackTrace();
                }
                int id=0;
                String country = jsonobject.getString("name");
                Log.d("TAG", "id:" + id);
                Log.d("TAG", "country:" + country);
                Country countryObj=new Country(country,cap);
                countries.add(countryObj);
                            }

        } catch (JSONException e) {
            Log.d("tag3","countries array list size: "+ countries.size());
            e.printStackTrace();
        }

        return null;
    }
}

