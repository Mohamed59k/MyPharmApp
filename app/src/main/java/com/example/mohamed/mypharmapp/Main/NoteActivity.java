package com.example.mohamed.mypharmapp.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mohamed.mypharmapp.Adapter.Pharmacy;
import com.example.mohamed.mypharmapp.R;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

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
            this.note = values.get(11);
        }

        final EditText textNote = (EditText) findViewById(R.id.text_note);
        textNote.setText(this.note);

        final ImageButton saveButton = (ImageButton) findViewById(R.id.save_note);
        saveButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedNote = textNote.getText().toString();
                DataBase pharmacyDb = new DataBase(getApplicationContext());
                pharmacyDb.updatePharmacy(new Pharmacy(id, name, adress, phone, isOpenNow, openingHours, lat, lng, distanceText, distanceValue, isFavorite, updatedNote));
                pharmacyDb.close();
                finish();
            }
        });

        final ImageButton cancelButton = (ImageButton) findViewById(R.id.cancel_note);
        cancelButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Annuler la saisie")
                        .setMessage("Voulez-vous vraiement annuler la saise ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}
