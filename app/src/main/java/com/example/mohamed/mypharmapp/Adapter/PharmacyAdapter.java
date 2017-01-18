package com.example.mohamed.mypharmapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mohamed.mypharmapp.Main.MainActivity;
import com.example.mohamed.mypharmapp.Main.PharmacyActivity;
import com.example.mohamed.mypharmapp.R;

import java.util.ArrayList;

/**
 * Created by Mohamed on 08/01/2017.
 */

public class PharmacyAdapter extends ArrayAdapter<Pharmacy> {

    private View currentView;

    public PharmacyAdapter(Context context, ArrayList<Pharmacy> pharmacies) {
        super(context, 0, pharmacies);
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

        //On met les éléments demandés dans les champs (NB : cela doit être une string)
        planteName.setText(pharmacy.getName());
        planteAdress.setText(pharmacy.getAdress());
        planteDistance.setText(String.valueOf(pharmacy.getId()));

        /*Ajouter un clique. Lorsque l'on clique sur un élément de la ListView on récupère les différentes
         informations de la plante et on les envoie à notre Activité PlanteActivity pour qu'on puisse
         les afficher et les mettre à jour*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PharmacyActivity.class);
                ArrayList<String> values = new ArrayList<String>();
                Bundle b = new Bundle();
                values.add(Long.toString(pharmacy.getId()));
                values.add(pharmacy.getName());
                values.add(Float.toString(pharmacy.getLat()));
                values.add(Float.toString(pharmacy.getLng()));
                b.putStringArrayList("values", values);
                intent.putExtras(b);
                ((MainActivity)getContext()).startActivityForResult(intent, 1);
            }
        });

        //Code pour gérer le clique sur bouton de suppression d'une plante
        /*ImageButton deleteImageView = (ImageButton) convertView.findViewById(R.id.imageButton);
        deleteImageView.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext() instanceof MainActivity){
                    PlanteDB planteDb = new PlanteDB(getContext());
                    planteDb.deletePlante(plante);
                    ((MainActivity)getContext()).refreshList();
                }
            }
        });*/

        //On retourne la vue de la ligne
        return convertView;
    }
}
