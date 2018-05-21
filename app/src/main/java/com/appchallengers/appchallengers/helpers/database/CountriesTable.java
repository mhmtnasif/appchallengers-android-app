package com.appchallengers.appchallengers.helpers.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.SQLException;
import com.appchallengers.appchallengers.webservice.response.CountryList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MHMTNASIF on 22.02.2018.
 */

public class CountriesTable {
    private static final String DATABASE_TABLE = "CountriesTable";
    public static final String KEY_ID = "ID";
    public static final String KEY_COUNTRY_CODE = "COUNTRY_CODE";
    public static final String KEY_COUNTRY_NAME = "COUNTRY_NAME";

    public static final String TABLE_CREATE =
            "create table IF NOT EXISTS " + DATABASE_TABLE + " ("
                    + KEY_ID + " INTEGER Unique, "
                    + KEY_COUNTRY_CODE + " TEXT, "
                    + KEY_COUNTRY_NAME + " TEXT)";


    private SQLiteDatabase mDb;

    public CountriesTable(SQLiteDatabase mDb) {
        mDb.execSQL(TABLE_CREATE);
        this.mDb = mDb;
    }

    public long create(CountryList countryList) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, countryList.getId());
        initialValues.put(KEY_COUNTRY_CODE, countryList.getCountryCode());
        initialValues.put(KEY_COUNTRY_NAME, countryList.getCountryName());

        Log.i("DENEME", "country Created");
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public List<CountryList> getList() throws SQLException {
        Cursor mCursor = mDb.rawQuery("select * from " + DATABASE_TABLE, null);
        ArrayList<CountryList> mList = new ArrayList<>();
        if (mCursor != null) {
            mCursor.moveToFirst();

            while (!mCursor.isAfterLast()) {

                CountryList countryList = new CountryList();
                countryList.setId(mCursor.getInt(mCursor.getColumnIndex(KEY_ID)));
                countryList.setCountryCode(mCursor.getString(mCursor.getColumnIndex(KEY_COUNTRY_CODE)));
                countryList.setCountryName(mCursor.getString(mCursor.getColumnIndex(KEY_COUNTRY_NAME)));
                mList.add(countryList);
                mCursor.moveToNext();
            }

        }
        return mList;
    }

    public void Truncate() {
        mDb.execSQL("delete from " + DATABASE_TABLE);
        Log.i("DENEME", "country Truncated");
    }
}
