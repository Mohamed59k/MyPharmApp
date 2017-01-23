package com.example.mohamed.mypharmapp.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mohamed.mypharmapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PharmacyActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private long id;
    private String name;
    private String adress;
    private String phone;
    private Boolean isOpenNow;
    private String openingHours;
    private Float lat;
    private Float lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        //On récupère les informations passées à l'activité et on remplit les champs
        Bundle b = getIntent().getExtras();
        if(b != null){
            ArrayList<String> values = b.getStringArrayList("values");
            this.id = Long.parseLong(values.get(0));
            this.name = values.get(1);
            this.adress = values.get(2);
            this.phone = values.get(3);
            this.isOpenNow = Boolean.parseBoolean(values.get(4));
            this.openingHours = values.get(5);
            this.lat = Float.parseFloat(values.get(6));
            this.lng = Float.parseFloat(values.get(7));
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fillFields();
    }

    /**
     * Cette fonction permet de remplir les champs en fonction du nom et
     * de la fréquence d'arrosage de la plante récupèrée.
     */
    public void fillFields()
    {
        TextView nameField = (TextView) findViewById(R.id.pharmacy_name);
        nameField.setText(this.name);
        TextView hoursField = (TextView) findViewById(R.id.pharmacy_hours);
        hoursField.setText(this.openingHours);
        TextView phoneField = (TextView) findViewById(R.id.pharmacy_phone);
        phoneField.setText(this.phone);
        TextView adressFiels = (TextView) findViewById(R.id.pharmacy_adress);
        adressFiels.setText(this.adress);
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
}
