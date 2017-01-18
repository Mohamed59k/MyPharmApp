package com.example.mohamed.mypharmapp.Main;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.mohamed.mypharmapp.Adapter.Pharmacy;
import com.example.mohamed.mypharmapp.Adapter.PharmacyAdapter;
import com.example.mohamed.mypharmapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    //change
    private GoogleMap mMap;
    private Double lat;
    private Double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.lat = 50.6167;
        this.lng = 3.1666;
        new JsonTask().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=1000&types=pharmacy&key=AIzaSyCRFuEoAV8sdMDanZ3sFgQ4PY2h5dOLePA", "ALL");

        refreshList();

    }

    /*@Override
    public void onPause() {
        super.onPause();
        if ((pd != null) && pd.isShowing())
            pd.dismiss();
    }*/

    /**
     * Permet de rafraîchir la ListView lors de l'initialisation et après chaque modification.
     * On récupère une liste de plantes depuis la base de données, on crée l'adaptateur d'item pour une ligne,
     * on déclare notre ListView et on finit par ajouter notre adapter à la liste.
     */
    public void refreshList() {
        DataBase pharmacyDb = new DataBase(getApplicationContext());
        ArrayList<Pharmacy> pharmacies = pharmacyDb.getAllPharmacies();

        for(Pharmacy pharmacy: pharmacies){
            Log.d("id: ", pharmacy.getId() + ", name : " +pharmacy.getName() + ", adress : " +pharmacy.getAdress() + ", phone : " +pharmacy.getPhone() + ", openNow : " +pharmacy.isOpenNow() + ", openingHour : " +pharmacy.getOpeningHour() + ", closingHour : " +pharmacy.getClosingHour() + ", lat : " +pharmacy.getLat() + ", lng : " +pharmacy.getLng());
        }

        PharmacyAdapter adapter = new PharmacyAdapter(this, pharmacies);
        ListView listView = (ListView) findViewById(R.id.pharmacy_list);
        listView.setAdapter(adapter);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng lieu = new LatLng(this.lat, this.lng);

        //mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lieu, 13));

        mMap.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(lieu));
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        private String type;
        ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            System.out.println("icii");

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                this.type = params[1];
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                JSONObject json = new JSONObject(result);
                if(this.type.equalsIgnoreCase("ALL")){
                    jsonSearchAllPharmacies(json);
                }else if(this.type.equalsIgnoreCase("DETAILS")){
                    jsonSearchDetailsPharmacy(json);
                }
                refreshList();
            } catch (Exception e) {

            }
        }

        public void jsonSearchAllPharmacies(JSONObject json) {
            try {
                JSONArray results = json.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject current = results.getJSONObject(i);
                    String placeId = current.getString("place_id");
                    //lancer requete pour recuperer les details de la pharmacie
                    new JsonTask().execute("https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeId+"&language=fr&key=AIzaSyCRFuEoAV8sdMDanZ3sFgQ4PY2h5dOLePA", "DETAILS");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void jsonSearchDetailsPharmacy(JSONObject json) {
            try {
                JSONObject result = json.getJSONObject("result");

                //get name
                String name = result.getString("name");

                //get adress
                String adress = result.getString("vicinity");

                //get phone
                String phone = result.getString("formatted_phone_number");

                //get openingHours and closingHours
                JSONObject hours = result.getJSONObject("opening_hours");
                boolean openNow = hours.getBoolean("open_now");
                String weekdayText = hours.getString("weekday_text");
                Log.d("weekdayText: ", weekdayText);

                //get lat and lng
                JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
                Float lat = BigDecimal.valueOf(location.getDouble("lat")).floatValue();
                Float lng = BigDecimal.valueOf(location.getDouble("lng")).floatValue();

                Pharmacy pharmacy = new Pharmacy(name, adress, phone, openNow, "", "", lat, lng);
                DataBase pharmacyDb = new DataBase(getApplicationContext());
                pharmacyDb.putPharmacy(pharmacy);
                pharmacyDb.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
