package com.example.mohamed.mypharmapp.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mohamed.mypharmapp.Adapter.Pharmacy;
import com.example.mohamed.mypharmapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private String distanceText;
    private Integer distanceValue;
    private boolean isFavorite;

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
            this.distanceText = values.get(8);
            this.distanceValue = Integer.parseInt(values.get(9));
            this.isFavorite = Boolean.parseBoolean(values.get(10));
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pharmacy_map);
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

        final ImageButton favoriteButton = (ImageButton) findViewById(R.id.pharmacy_favorite);

        if(isFavorite){
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
        }else{
            favoriteButton.setImageResource(android.R.drawable.btn_star);

        }

        favoriteButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase pharmacyDb = new DataBase(getApplicationContext());
                if(isFavorite){
                    isFavorite=false;
                    favoriteButton.setImageResource(android.R.drawable.btn_star);
                }else{
                    isFavorite=true;
                    favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                }
                pharmacyDb.updatePharmacy(new Pharmacy(id, name, adress, phone, isOpenNow, openingHours, lat, lng, distanceText, distanceValue, isFavorite));
                pharmacyDb.close();
            }
        });
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
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_adress))
                .position(lieu));

        mMap.setMyLocationEnabled(true);

        //mMap.addPolyline()
    }
}
