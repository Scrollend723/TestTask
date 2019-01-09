package com.bikov.testtask.Service;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.bikov.testtask.Entity.MapMarker;
import com.bikov.testtask.Entity.MarkerList;
import com.bikov.testtask.R;
import com.google.android.gms.maps.model.LatLng;

public class MapDataManager {
    private static MapDataManager instance;
    private int titleIndex;
    private int subtitleIndex;
    private int iconIDIndex;
    private int latIndex;
    private int lngIndex;
    private Context context;
    private Cursor cursor;
    private DBManager dbManager;

    public static synchronized MapDataManager getInstance(Context context) {
        if(instance == null) {
            instance = new MapDataManager(context);
        }
        return instance;
    }

    private MapDataManager(Context context) {
        this.context = context;
        dbManager = new DBManager(context);

        dbManager.addMarkerToDB("5 Элемент", "Независимости 117", R.drawable.putin, 53.9305300, 27.6346841);
        dbManager.addMarkerToDB("5 Элемент", "кульман 14", R.drawable.putin, 53.9210960, 27.5816002);
        dbManager.addMarkerToDB("5 Элемент", "Дзержинского 104", R.drawable.putin, 53.8610232, 27.4798459);
        dbManager.commit();
    }

    public MarkerList getMarkerList() {
        cursor = dbManager.getCursor();
        MarkerList markerList = new MarkerList();

        setColumnIndexes();
        cursor.moveToFirst();
        do{
            markerList.add(getMarkerFromDB());
        } while (cursor.moveToNext());

        return markerList;
    }

    private void setColumnIndexes(){
        titleIndex = cursor.getColumnIndex("title");
        subtitleIndex = cursor.getColumnIndex("subtitle");
        iconIDIndex = cursor.getColumnIndex("iconID");
        latIndex = cursor.getColumnIndex("lat");
        lngIndex = cursor.getColumnIndex("lng");
    }

    private MapMarker getMarkerFromDB(){
        Drawable icon = context.getResources().getDrawable(cursor.getInt(iconIDIndex));
        LatLng latLng = new LatLng(cursor.getDouble(latIndex), cursor.getDouble(lngIndex));
        return new MapMarker(cursor.getString(titleIndex), cursor.getString(subtitleIndex), icon, latLng);
    }
}
