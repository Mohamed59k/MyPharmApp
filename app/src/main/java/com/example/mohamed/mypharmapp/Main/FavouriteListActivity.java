package com.example.mohamed.mypharmapp.Main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.mohamed.mypharmapp.R;
import com.example.mohamed.mypharmapp.Adapter.Pharmacy;
import com.example.mohamed.mypharmapp.Adapter.PharmacyAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouriteListActivity extends AppCompatActivity {
    private List<Pharmacy> pharmacies;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        DataBase pharmacyDb = new DataBase(getApplicationContext());
        pharmacies = pharmacyDb.getAllFavourites();
        PharmacyAdapter adapter = new PharmacyAdapter(this, (ArrayList<Pharmacy>) pharmacies);
        listView = (ListView) findViewById(R.id.list_favourite);
        listView.setAdapter(adapter);
    }
}
