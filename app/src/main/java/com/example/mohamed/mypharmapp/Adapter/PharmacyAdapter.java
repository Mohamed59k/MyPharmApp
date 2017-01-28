package com.example.mohamed.mypharmapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.mypharmapp.Main.DataBase;
import com.example.mohamed.mypharmapp.Main.FavouriteListActivity;
import com.example.mohamed.mypharmapp.Main.MainActivity;
import com.example.mohamed.mypharmapp.Main.PharmacyActivity;
import com.example.mohamed.mypharmapp.R;

import java.util.ArrayList;

public class PharmacyAdapter extends ArrayAdapter<Pharmacy> {

    private View currentView;
    private Double userLat;
    private Double userLng;

    public PharmacyAdapter(Context context, ArrayList<Pharmacy> pharmacies, Double userLat, Double userLng) {
        super(context, 0, pharmacies);
        this.userLat = userLat;
        this.userLng = userLng;
    }

    /**
     * Récupere la vue et la position de l'element qui a été cliqué dans la ListView. On met ensuite
     * à jour les différents champs de l'élément par rapport aux données de la pharmacie récuperée.
     * @param position La position de l'element dans le ListView
     * @param convertView La vue de l'élément
     * @param parent La vue parent de l'élément
     * @return Retourne la vue de la ligne
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //On récupère la plante à une position précise
        final Pharmacy pharmacy = getItem(position);

        //On vérifie s'il existe une view, si oui on récupère la vue de la ligne pour la plante
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        this.currentView = convertView;

        //On déclare les différents champs de la vue
        final TextView planteName = (TextView) convertView.findViewById(R.id.name);
        final TextView planteAdress = (TextView) convertView.findViewById(R.id.adress);
        final TextView planteDistance = (TextView) convertView.findViewById(R.id.distance);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        final ImageButton favoriteButton = (ImageButton) convertView.findViewById(R.id.favorite_button);

        if(pharmacy.isOpenNow()){
            imageView.setBackgroundColor(Color.parseColor("#A3FFAF"));
        }else{
            imageView.setBackgroundColor(Color.parseColor("#FF6565"));
        }

        if(pharmacy.isFavorite()){
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
        }else{
            favoriteButton.setImageResource(android.R.drawable.btn_star);

        }


        //On met les éléments demandés dans les champs (NB : cela doit être une string)
        planteName.setText(pharmacy.getName());
        planteAdress.setText(pharmacy.getAdress());
        planteDistance.setText(pharmacy.getDistanceText());

        /*Ajouter un clique. Lorsque l'on clique sur un élément de la ListView on récupère les différentes
         informations de la plante et on les envoie à notre Activité PlanteActivity pour qu'on puisse
         les afficher et les mettre à jour*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PharmacyActivity.class);
                ArrayList<String> values = new ArrayList<String>();
                Bundle b = new Bundle();
                values.add(Double.toString(userLat));
                values.add(Double.toString(userLng));
                values.add(Long.toString(pharmacy.getId()));
                values.add(pharmacy.getName());
                values.add(pharmacy.getAdress());
                values.add(pharmacy.getPhone());
                values.add(Boolean.toString(pharmacy.isOpenNow()));
                values.add(pharmacy.getOpeningHours());
                values.add(Float.toString(pharmacy.getLat()));
                values.add(Float.toString(pharmacy.getLng()));
                values.add(pharmacy.getDistanceText());
                values.add(Integer.toString(pharmacy.getDistanceValue()));
                values.add(Boolean.toString(pharmacy.isFavorite()));
                values.add(pharmacy.getNote());
                b.putStringArrayList("values", values);
                intent.putExtras(b);

                if(getContext() instanceof MainActivity) {
                    ((MainActivity) getContext()).startActivityForResult(intent, 1);
                }else if(getContext() instanceof FavouriteListActivity){
                    ((FavouriteListActivity) getContext()).startActivityForResult(intent, 1);
                }
            }
        });

        //Code pour gérer le clique sur bouton de favoris d'une pharmacie
        //final ImageButton favoriteButton = (ImageButton) convertView.findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext() instanceof MainActivity || getContext() instanceof FavouriteListActivity){
                    DataBase pharmacyDb = new DataBase(getContext());
                    if(pharmacy.isFavorite()){
                        pharmacy.setFavorite(false);
                        favoriteButton.setImageResource(android.R.drawable.btn_star);
                    }else{
                        pharmacy.setFavorite(true);
                        favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                    }
                    pharmacyDb.updatePharmacy(pharmacy);
                    pharmacyDb.close();
                    if(getContext() instanceof FavouriteListActivity){
                        ((FavouriteListActivity) getContext()).refreshList();
                    }
                }
            }
        });

        //On retourne la vue de la ligne
        return convertView;
    }

    public static String formatDist(float meters) {
        if (meters < 1000) {
            return ((int) meters) + "m";
        } else if (meters < 10000) {
            return formatDec(meters / 1000f, 1) + "km";
        } else {
            return ((int) (meters / 1000f)) + "km";
        }
    }

    static String formatDec(float val, int dec) {
        int factor = (int) Math.pow(10, dec);

        int front = (int) (val);
        int back = (int) Math.abs(val * (factor)) % factor;

        return front + "." + back;
    }
}
