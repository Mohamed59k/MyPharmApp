package com.example.mohamed.mypharmapp.Main;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class PharmacyActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double userLat;
    private Double userLng;
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
    private String note;
    private boolean recent;
    private ImageButton itinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        //On récupère les informations passées à l'activité et on remplit les champs
        Bundle b = getIntent().getExtras();
        if(b != null){
            ArrayList<String> values = b.getStringArrayList("values");
            this.userLat = Double.parseDouble(values.get(0));
            this.userLng = Double.parseDouble(values.get(1));
            this.id = Long.parseLong(values.get(2));
            this.name = values.get(3);
            this.adress = values.get(4);
            this.phone = values.get(5);
            this.isOpenNow = Boolean.parseBoolean(values.get(6));
            this.openingHours = values.get(7);
            this.lat = Float.parseFloat(values.get(8));
            this.lng = Float.parseFloat(values.get(9));
            this.distanceText = values.get(10);
            this.distanceValue = Integer.parseInt(values.get(11));
            this.isFavorite = Boolean.parseBoolean(values.get(12));
            this.note = values.get(13);
            this.recent = Boolean.parseBoolean(values.get(14));
        }
        itinerary = (ImageButton)findViewById(R.id.button_itinerary);
        itinerary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                callItinerary();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pharmacy_map);
        mapFragment.getMapAsync(this);

        fillFields();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataBase pharmacyDb = new DataBase(getApplicationContext());
        Pharmacy pharmacy = pharmacyDb.getPharmacy(id);
        pharmacyDb.close();

        if(pharmacy!=null){
            TextView noteField = (TextView) findViewById(R.id.pharmacy_note);
            noteField.setText(pharmacy.getNote());
        }

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
        if(this.openingHours==""){
            hoursField.setText(R.string.no_hours_found);
        }else{
            hoursField.setText(this.openingHours);
        }
        TextView phoneField = (TextView) findViewById(R.id.pharmacy_phone);
        if(this.phone==""){
            phoneField.setText(R.string.no_phone_found);
        }else{
            phoneField.setText(this.phone);
        }
        TextView adressFiels = (TextView) findViewById(R.id.pharmacy_adress);
        adressFiels.setText(this.adress);


        final TextView distanceField = (TextView) findViewById(R.id.pharmacy_distance);
        distanceField.setText(this.distanceText);

        final ImageView imageView = (ImageView) findViewById(R.id.pharmacy_imageView);
        if(this.isOpenNow){
            imageView.setBackgroundColor(Color.parseColor("#C0FFC9"));
        }else{
            imageView.setBackgroundColor(Color.parseColor("#FFCACA"));
        }

        final ImageButton favoriteButton = (ImageButton) findViewById(R.id.pharmacy_favorite);

        if(isFavorite){
            favoriteButton.setImageResource(R.mipmap.ic_plain_star);
        }else{
            favoriteButton.setImageResource(R.mipmap.ic_empty_star);

        }

        favoriteButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase pharmacyDb = new DataBase(getApplicationContext());
                if(isFavorite){
                    isFavorite=false;
                    favoriteButton.setImageResource(R.mipmap.ic_empty_star);
                }else{
                    isFavorite=true;
                    favoriteButton.setImageResource(R.mipmap.ic_plain_star);
                }
                pharmacyDb.updatePharmacy(new Pharmacy(id, name, adress, phone, isOpenNow, openingHours, lat, lng, distanceText, distanceValue, isFavorite, note, recent));
                pharmacyDb.close();
            }
        });


        TextView noteField = (TextView) findViewById(R.id.pharmacy_note);
        noteField.setText(this.note);

        final ImageButton noteButton = (ImageButton) findViewById(R.id.note_image);
        noteButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                ArrayList<String> values = new ArrayList<String>();
                Bundle b = new Bundle();
                values.add(Long.toString(id));
                values.add(name);
                values.add(adress);
                values.add(phone);
                values.add(Boolean.toString(isOpenNow));
                values.add(openingHours);
                values.add(Float.toString(lat));
                values.add(Float.toString(lng));
                values.add(distanceText);
                values.add(Integer.toString(distanceValue));
                values.add(Boolean.toString(isFavorite));
                values.add(note);
                values.add(Boolean.toString(recent));
                b.putStringArrayList("values", values);
                intent.putExtras(b);
                startActivityForResult(intent, 1);
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
                .title("Votre position")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_localisation))
                .position(new LatLng(this.userLat, this.userLng)));

        mMap.addMarker(new MarkerOptions()
                .title(this.name)
                .snippet(this.adress)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_address))
                .position(lieu));

        PolylineOptions line=
                new PolylineOptions().add(new LatLng(this.userLat, this.userLng),
                        lieu)
                        .color(ContextCompat.getColor(getApplicationContext(), R.color.appTheme))
                        .width(9);

        mMap.addPolyline(line);

        mMap.setMyLocationEnabled(true);

        //mMap.addPolyline()
    }

    public void callItinerary(){
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+this.lat+","+this.lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
