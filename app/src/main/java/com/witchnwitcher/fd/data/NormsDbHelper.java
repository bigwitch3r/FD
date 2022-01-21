package com.witchnwitcher.fd.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NormsDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = NormsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "norms.db";

    private static final int DATABASE_VERSION = 1;

    public NormsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_NORMS_TABLE = "CREATE TABLE " + NormsContract.Norm.TABLE_NAME + " ("
                + NormsContract.Norm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NormsContract.Norm.COLUMN_CALORIES + " FLOAT NOT NULL DEFAULT 0, "
                + NormsContract.Norm.COLUMN_PROTEINS + " FLOAT NOT NULL DEFAULT 0, "
                + NormsContract.Norm.COLUMN_FATS + " FLOAT NOT NULL DEFAULT 0, "
                + NormsContract.Norm.COLUMN_CARBOHYDRATES + " FLOAT NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_NORMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + NormsContract.Norm.TABLE_NAME);
        // Создаём новую таблицу
        onCreate(db);

    }
}
