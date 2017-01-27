package com.example.mohamed.mypharmapp.Main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.mohamed.mypharmapp.Adapter.Pharmacy;

import java.util.ArrayList;

/**
 * Created by Mohamed on 23/11/2016.
 */

public class DataBase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Pharmacies";

    /**
     * Columns of our database
     */
    static class PharmacyTable implements BaseColumns {
        public static final String TABLE_NAME = "Pharmacy";
        public static final String PHARMACY_NAME = "name";
        public static final String PHARMACY_ADRESS = "adress";
        public static final String PHARMACY_PHONE = "phone";
        public static final String PHARMACY_OPENNOW = "openNow";
        public static final String PHARMACY_OPENING_HOURS = "openingHours";
        public static final String PHARMACY_LAT = "lat";
        public static final String PHARMACY_LGT = "lng";
        public static final String PHARMACY_DISTANCETEXT = "distanceText";
        public static final String PHARMACY_DISTANCEVALUE = "distanceValue";
        public static final String PHARMACY_FAVORITE = "favorite";
    }

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Pharmacy(" + PharmacyTable.TABLE_NAME + " INTEGER PRIMARY KEY," +
                PharmacyTable.PHARMACY_NAME + " VARCHAR(100),"+ PharmacyTable.PHARMACY_ADRESS + " VARCHAR(100)," + PharmacyTable.PHARMACY_PHONE + " VARCHAR(100), "+ PharmacyTable.PHARMACY_OPENNOW + " BOOLEAN, " + PharmacyTable.PHARMACY_OPENING_HOURS + " VARCHAR(100), " + PharmacyTable.PHARMACY_LAT + " FLOAT, " + PharmacyTable.PHARMACY_LGT + " FLOAT, " + PharmacyTable.PHARMACY_DISTANCETEXT + " VARCHAR(100), " + PharmacyTable.PHARMACY_DISTANCEVALUE + " INTEGER, " + PharmacyTable.PHARMACY_FAVORITE + " BOOLEAN)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    /**
     * Get all the pharmacies from de database
     * @return ist of all pharmacies
     */
    public ArrayList<Pharmacy> getAllPharmacies() {
        ArrayList<Pharmacy> pharmacies = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + PharmacyTable.TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Pharmacy pharmacy = new Pharmacy(cursor.getLong(cursor.getColumnIndex("Pharmacy")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("adress")), cursor.getString(cursor.getColumnIndex("phone")), (cursor.getInt(cursor.getColumnIndex("openNow")) >0) , cursor.getString(cursor.getColumnIndex("openingHours")), cursor.getFloat(cursor.getColumnIndex("lat")), cursor.getFloat(cursor.getColumnIndex("lng")), cursor.getString(cursor.getColumnIndex("distanceText")), cursor.getInt(cursor.getColumnIndex("distanceValue")), (cursor.getInt(cursor.getColumnIndex("favorite")) >0));
            pharmacies.add(pharmacy);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return pharmacies;
    }

    public ArrayList<Pharmacy> getAllPharmaciesByRadius(Integer radius) {
        ArrayList<Pharmacy> pharmacies = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + PharmacyTable.TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            if(cursor.getInt(cursor.getColumnIndex("distanceValue")) <= radius){
                Pharmacy pharmacy = new Pharmacy(cursor.getLong(cursor.getColumnIndex("Pharmacy")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("adress")), cursor.getString(cursor.getColumnIndex("phone")), (cursor.getInt(cursor.getColumnIndex("openNow")) >0) , cursor.getString(cursor.getColumnIndex("openingHours")), cursor.getFloat(cursor.getColumnIndex("lat")), cursor.getFloat(cursor.getColumnIndex("lng")), cursor.getString(cursor.getColumnIndex("distanceText")), cursor.getInt(cursor.getColumnIndex("distanceValue")), (cursor.getInt(cursor.getColumnIndex("favorite")) >0));
                pharmacies.add(pharmacy);
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return pharmacies;
    }

    public ArrayList<Pharmacy> getAllFavoritesPharmacies() {
        ArrayList<Pharmacy> pharmacies = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + PharmacyTable.TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Pharmacy pharmacy = new Pharmacy(cursor.getLong(cursor.getColumnIndex("Pharmacy")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("adress")), cursor.getString(cursor.getColumnIndex("phone")), (cursor.getInt(cursor.getColumnIndex("openNow")) >0) , cursor.getString(cursor.getColumnIndex("openingHours")), cursor.getFloat(cursor.getColumnIndex("lat")), cursor.getFloat(cursor.getColumnIndex("lng")), cursor.getString(cursor.getColumnIndex("distanceText")), cursor.getInt(cursor.getColumnIndex("distanceValue")), (cursor.getInt(cursor.getColumnIndex("favorite")) >0));
            if(pharmacy.isFavorite()){
                pharmacies.add(pharmacy);
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return pharmacies;
    }

    public Pharmacy searchPharmacyByNameOrAdress(Pharmacy pharmacy){
        Log.d("11111 : ", pharmacy.getAdress());
        String selectQuery = "SELECT  * FROM " + PharmacyTable.TABLE_NAME + " WHERE " + PharmacyTable.PHARMACY_NAME + "=" + DatabaseUtils.sqlEscapeString(pharmacy.getName()) + " OR " + PharmacyTable.PHARMACY_ADRESS + "=" + DatabaseUtils.sqlEscapeString(pharmacy.getAdress());
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        Log.d("22222 : ", "ici");
        if(!cursor.isAfterLast()){
            Log.d("33333 : ", "ici");
            Pharmacy py = new Pharmacy(cursor.getLong(cursor.getColumnIndex("Pharmacy")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("adress")), cursor.getString(cursor.getColumnIndex("phone")), (cursor.getInt(cursor.getColumnIndex("openNow")) >0), cursor.getString(cursor.getColumnIndex("openingHours")), cursor.getFloat(cursor.getColumnIndex("lat")), cursor.getFloat(cursor.getColumnIndex("lng")), cursor.getString(cursor.getColumnIndex("distanceText")), cursor.getInt(cursor.getColumnIndex("distanceValue")), (cursor.getInt(cursor.getColumnIndex("favorite")) >0));
            return py;
        }
        Log.d("44444 : ", "ici");
        cursor.close();
        db.close();
        return null;
    }

    /**
     * Put a pharmacy in the database
     * @param pharmacy
     */
    public void putPharmacy(Pharmacy pharmacy) {
        Pharmacy pharmacyFound = searchPharmacyByNameOrAdress(pharmacy);
        if(pharmacyFound!=null){
            Pharmacy toUpdate = new Pharmacy(pharmacyFound.getId(), pharmacy.getName(), pharmacy.getAdress(), pharmacy.getPhone(), pharmacy.isOpenNow(), pharmacy.getOpeningHours(), pharmacy.getLat(), pharmacy.getLng(), pharmacy.getDistanceText(), pharmacy.getDistanceValue(), pharmacyFound.isFavorite());
            this.updatePharmacy(toUpdate);
        }else{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(PharmacyTable.PHARMACY_NAME, pharmacy.getName());
            values.put(PharmacyTable.PHARMACY_ADRESS, pharmacy.getAdress());
            values.put(PharmacyTable.PHARMACY_PHONE, pharmacy.getPhone());
            values.put(PharmacyTable.PHARMACY_OPENNOW, pharmacy.isOpenNow());
            values.put(PharmacyTable.PHARMACY_OPENING_HOURS, pharmacy.getOpeningHours());
            values.put(PharmacyTable.PHARMACY_LAT, pharmacy.getLat());
            values.put(PharmacyTable.PHARMACY_LGT, pharmacy.getLng());
            values.put(PharmacyTable.PHARMACY_DISTANCETEXT, pharmacy.getDistanceText());
            values.put(PharmacyTable.PHARMACY_DISTANCEVALUE, pharmacy.getDistanceValue());
            values.put(PharmacyTable.PHARMACY_FAVORITE, pharmacy.isFavorite());
            db.insert(PharmacyTable.TABLE_NAME, null, values);
            db.close();
        }
    }

    /**
     * Put multiple pharmacies in the database
     * @param pharmacies
     */
   /* public void putPharmacies(ArrayList<Pharmacy> pharmacies) {
        for (Pharmacy pharmacy: pharmacies) {
            Pharmacy found = searchPharmacyByNameOrAdress(pharmacy);
            if(found!=null){
                Pharmacy toUpdate = new Pharmacy(found.getId(), pharmacy.getName(), pharmacy.getAdress(), pharmacy.getPhone(), pharmacy.isOpenNow(), pharmacy.getOpeningHour(), pharmacy.getClosingHour(), pharmacy.getLat(), pharmacy.getLng());
                this.updatePharmacy(toUpdate);
            }else{
                putPharmacy(pharmacy);
            }
        }
    }*/

    /**
     * Delete a pharmacy in the database
     * @param pharmacy
     */
    public void deletePharmacy(Pharmacy pharmacy){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PharmacyTable.TABLE_NAME, pharmacy.getId());
        db.delete(PharmacyTable.TABLE_NAME, PharmacyTable.TABLE_NAME+"=?", new String[]{Long.toString(pharmacy.getId())});
        db.close();
    }

    /**
     * Update a pharmacy in the database
     * @param pharmacy
     */
    public void updatePharmacy(Pharmacy pharmacy) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PharmacyTable.PHARMACY_NAME, pharmacy.getName());
        values.put(PharmacyTable.PHARMACY_ADRESS, pharmacy.getAdress());
        values.put(PharmacyTable.PHARMACY_PHONE, pharmacy.getPhone());
        values.put(PharmacyTable.PHARMACY_OPENNOW, pharmacy.isOpenNow());
        values.put(PharmacyTable.PHARMACY_OPENING_HOURS, pharmacy.getOpeningHours());
        values.put(PharmacyTable.PHARMACY_LAT, pharmacy.getLat());
        values.put(PharmacyTable.PHARMACY_LGT, pharmacy.getLng());
        values.put(PharmacyTable.PHARMACY_DISTANCETEXT, pharmacy.getDistanceText());
        values.put(PharmacyTable.PHARMACY_DISTANCEVALUE, pharmacy.getDistanceValue());
        values.put(PharmacyTable.PHARMACY_FAVORITE, pharmacy.isFavorite());
        db.update(PharmacyTable.TABLE_NAME, values, PharmacyTable.TABLE_NAME+"=" + pharmacy.getId(), null);
        db.close();
    }
}
