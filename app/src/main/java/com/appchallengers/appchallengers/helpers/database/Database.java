package com.appchallengers.appchallengers.helpers.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MHMTNASIF on 22.02.2018.
 */

public class Database {

    private static final String DATABASE_NAME = "appchallengers_db";

    private Context context;
    SQLiteDatabase dB;

    public SQLiteDatabase open(Context context) {
        this.context = context;
        dB = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        return dB;
    }

    public void close() {
        dB.close();
    }
}
