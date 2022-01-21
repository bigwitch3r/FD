package com.witchnwitcher.fd.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DishDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DishDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "dishes.db";

    private static final int DATABASE_VERSION = 1;

    public DishDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_DISHES_TABLE = "CREATE TABLE " + DishesContract.Dish.TABLE_NAME + " ("
                + DishesContract.Dish._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DishesContract.Dish.COLUMN_NAME + " TEXT NOT NULL, "
                + DishesContract.Dish.COLUMN_CALORIES + " FLOAT NOT NULL DEFAULT 0, "
                + DishesContract.Dish.COLUMN_PROTEINS + " FLOAT NOT NULL DEFAULT 0, "
                + DishesContract.Dish.COLUMN_FATS + " FLOAT NOT NULL DEFAULT 0, "
                + DishesContract.Dish.COLUMN_CARBOHYDRATES + " FLOAT NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_DISHES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + DishesContract.Dish.TABLE_NAME);
        // Создаём новую таблицу
        onCreate(db);

    }
}
