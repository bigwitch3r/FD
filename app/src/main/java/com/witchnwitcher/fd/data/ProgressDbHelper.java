package com.witchnwitcher.fd.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProgressDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ProgressDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "progress.db";

    private static final int DATABASE_VERSION = 1;

    public ProgressDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PROGRESS_TABLE = "CREATE TABLE " + ProgressContract.Progress.TABLE_NAME + " ("
                + ProgressContract.Progress._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProgressContract.Progress.COLUMN_NAME + " TEXT NOT NULL, "
                + ProgressContract.Progress.COLUMN_CALORIES + " FLOAT NOT NULL DEFAULT 0, "
                + ProgressContract.Progress.COLUMN_PROTEINS + " FLOAT NOT NULL DEFAULT 0, "
                + ProgressContract.Progress.COLUMN_FATS + " FLOAT NOT NULL DEFAULT 0, "
                + ProgressContract.Progress.COLUMN_CARBOHYDRATES + " FLOAT NOT NULL DEFAULT 0, "
                + ProgressContract.Progress.COLUMN_AMOUNT + " INT NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_PROGRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + ProgressContract.Progress.TABLE_NAME);
        // Создаём новую таблицу
        onCreate(db);

    }
}
