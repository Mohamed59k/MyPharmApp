package com.example.mohamed.mypharmapp.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.mohamed.mypharmapp.Adapter.Pharmacy;
import com.example.mohamed.mypharmapp.Adapter.PharmacyAdapter;
import com.example.mohamed.mypharmapp.R;

import java.util.ArrayList;

public class FavouriteListActivity extends AppCompatActivity {

    private Double userLat;
    private Double userLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        Bundle b = getIntent().getExtras();
        if(b != null){
            ArrayList<String> values = b.getStringArrayList("values");
            this.userLat = Double.parseDouble(values.get(0));
            this.userLng = Double.parseDouble(values.get(1));
        }

        this.refreshList();
    }

    @Override
    public void onResume(){
        super.onResume();
        this.refreshList();
    }

    /**
     * Permet de rafraîchir la ListView lors de l'initialisation et après chaque modification.
     * On récupère une liste de plantes depuis la base de données, on crée l'adaptateur d'item pour une ligne,
     * on déclare notre ListView et on finit par ajouter notre adapter à la liste.
     */
    public void refreshList() {
        DataBase pharmacyDb = new DataBase(getApplicationContext());
        ArrayList<Pharmacy> pharmacies = pharmacyDb.getAllFavoritesPharmacies();

        for(Pharmacy pharmacy: pharmacies){
            Log.d("id: ", pharmacy.getId() + ", name : " +pharmacy.getName() + ", adress : " +pharmacy.getAdress() + ", phone : " +pharmacy.getPhone() + ", openNow : " +pharmacy.isOpenNow() + ", openingHours : " +pharmacy.getOpeningHours() + ", lat : " +pharmacy.getLat() + ", lng : " +pharmacy.getLng());
        }

        PharmacyAdapter adapter = new PharmacyAdapter(this, pharmacies, userLat, userLng);
        ListView listView = (ListView) findViewById(R.id.pharmacy_favorite_list);
        listView.setAdapter(adapter);
        pharmacyDb.close();
    }
}
