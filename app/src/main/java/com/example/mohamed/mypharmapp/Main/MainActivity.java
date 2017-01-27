package com.example.mohamed.mypharmapp.Main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mohamed.mypharmapp.Adapter.Pharmacy;
import com.example.mohamed.mypharmapp.Adapter.PharmacyAdapter;
import com.example.mohamed.mypharmapp.LocationPlacesAutocomplete.PlaceJSONParser;
import com.example.mohamed.mypharmapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Double lat;
    private Double lng;
    SupportMapFragment mapFragment;
    private Integer radius;

    protected LocationManager mLocationManager;
    LocationListener mLocationListener;
    // The minimum distance to change Updates in meters
    private static final float LOCATION_REFRESH_DISTANCE = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long LOCATION_REFRESH_TIME = 1; // 1 minute

    AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);




        LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                //your code here
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }


        };

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                this.lat = location.getLatitude();
                this.lng = location.getLongitude();
            }
        }

/////////////////////
        final ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) findViewById(R.id.action_search);;
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //searchView.setText(adapter.getItem(position).toString());

            }
        });

        /*atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });*/
//////////////////////


        //this.lat = 50.703094;
        //this.lng = 3.218449;
        this.radius = 1100;
        new JsonTask("ALL", this.lat, this.lng).execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius="+this.radius+"&types=pharmacy&key=AIzaSyCRFuEoAV8sdMDanZ3sFgQ4PY2h5dOLePA");

    }

    @Override
    public void onResume(){
        super.onResume();
        mapFragment.getMapAsync(this);
        //this.refreshList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu); return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favourite:
                Intent intent = new Intent(this, FavouriteListActivity.class);//FavouriteListActivity
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        ArrayList<Pharmacy> pharmacies = pharmacyDb.getAllPharmaciesByRadius(this.radius);

        for(Pharmacy pharmacy: pharmacies){
            Log.d("id: ", pharmacy.getId() + ", name : " +pharmacy.getName() + ", adress : " +pharmacy.getAdress() + ", phone : " +pharmacy.getPhone() + ", openNow : " +pharmacy.isOpenNow() + ", openingHours : " +pharmacy.getOpeningHours() + ", lat : " +pharmacy.getLat() + ", lng : " +pharmacy.getLng());
            LatLng lieu = new LatLng(pharmacy.getLat(), pharmacy.getLng());
            mMap.addMarker(new MarkerOptions()
                    .title(pharmacy.getName())
                    .snippet(pharmacy.getAdress())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_adress))
                    .position(lieu));
        }

        PharmacyAdapter adapter = new PharmacyAdapter(this, pharmacies);
        ListView listView = (ListView) findViewById(R.id.pharmacy_list);
        listView.setAdapter(adapter);
        pharmacyDb.close();


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

        mMap.setMyLocationEnabled(true);

        LatLng lieu = new LatLng(this.lat, this.lng);

        //mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lieu, 14));

        mMap.addMarker(new MarkerOptions()
                .title("Votre position")
                .position(lieu));

        this.refreshList();
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        private String type;
        ProgressDialog pd;
        private Double lat;
        private Double lng;
        private Pharmacy pharmacy;

        protected JsonTask(String type, Double lat, Double lng){
            this.type = type;
            this.lat = lat;
            this.lng = lng;
        }

        protected JsonTask(String type, Pharmacy pharmacy){
            this.type = type;
            this.pharmacy = pharmacy;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                   // Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
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
                }else if(this.type.equalsIgnoreCase("DISTANCE")){
                    jsonSearchDistancePharmacy(json);
                }
                //refreshList();
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
                    new JsonTask("DETAILS", this.lat, this.lng).execute("https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeId+"&language=fr&key=AIzaSyCRFuEoAV8sdMDanZ3sFgQ4PY2h5dOLePA");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void jsonSearchDetailsPharmacy(JSONObject json) {
            try {
                JSONObject result = json.getJSONObject("result");

                //get name
                String name = "";
                if(result.has("name")) {
                    name = result.getString("name");
                }

                //get adress
                String adress = "";
                if(result.has("vicinity")) {
                    adress = result.getString("vicinity");
                }

                //get phone
                String phone = "";
                if(result.has("formatted_phone_number")) {
                    phone = result.getString("formatted_phone_number");
                }

                //get openingHours and closingHours
                String openingHours = "";
                boolean openNow = false;
                if(result.has("opening_hours")) {
                    JSONObject hours = result.getJSONObject("opening_hours");
                    openNow = hours.getBoolean("open_now");
                    JSONArray hoursArray = hours.getJSONArray("weekday_text");
                    for (int i = 0; i < hoursArray.length(); i++) {
                        if (i == (hoursArray.length() - 1)) {
                            openingHours += hoursArray.get(i);
                        } else {
                            openingHours += hoursArray.get(i) + "\n";
                        }

                    }
                    //Log.d("openingHours: ", openingHours);
                }

                //get lat and lng
                Float lat = 0F;
                Float lng = 0F;
                if(result.has("geometry")){
                    JSONObject location = result.getJSONObject("geometry");
                    if(location.has("location")){
                        location = location.getJSONObject("location");
                        lat = BigDecimal.valueOf(location.getDouble("lat")).floatValue();
                        lng = BigDecimal.valueOf(location.getDouble("lng")).floatValue();
                    }
                }

                Pharmacy pharmacy = new Pharmacy(name, adress, phone, openNow, openingHours, lat, lng, "", 0, false);
                new JsonTask("DISTANCE", pharmacy).execute("https://maps.googleapis.com/maps/api/distancematrix/json?origins="+this.lat+","+this.lng+"&destinations="+lat+","+lng+"&mode=walking&transit_routing_preference=less_walking&language=fr-FR&key=AIzaSyCRFuEoAV8sdMDanZ3sFgQ4PY2h5dOLePA");

                /*Location user = new Location("User");
                user.setLatitude(this.lat);
                user.setLongitude(this.lng);

                Location locationB = new Location("Pharmacy");
                locationB.setLatitude(lat);
                locationB.setLongitude(lng);

                Float distance = user.distanceTo(locationB);
                Log.d("Distance : ", name+", d : "+distance);*/

                //Pharmacy pharmacy = new Pharmacy(name, adress, phone, openNow, openingHours, lat, lng, distance, false);
                //DataBase pharmacyDb = new DataBase(getApplicationContext());
                //pharmacyDb.putPharmacy(pharmacy);
                //pharmacyDb.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void jsonSearchDistancePharmacy(JSONObject json) {
            try {
                JSONArray elements = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");

                String distanceText = elements.getJSONObject(0).getJSONObject("distance").getString("text");
                Integer distanceValue = elements.getJSONObject(0).getJSONObject("distance").getInt("value");

                pharmacy.setDistanceText(distanceText);
                pharmacy.setDistanceValue(distanceValue);
                DataBase pharmacyDb = new DataBase(getApplicationContext());
                pharmacyDb.putPharmacy(pharmacy);
                pharmacyDb.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }






    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception downloading", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyCRFuEoAV8sdMDanZ3sFgQ4PY2h5dOLePA";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            atvPlaces.setAdapter(adapter);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/

}
