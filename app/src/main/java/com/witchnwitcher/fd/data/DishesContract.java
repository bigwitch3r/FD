package com.witchnwitcher.fd.data;

import android.provider.BaseColumns;

public final class DishesContract {
    private DishesContract() {};

    public static final class Dish implements BaseColumns {
        public final static String TABLE_NAME = "dishes";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_CALORIES = "calories";
        public final static String COLUMN_PROTEINS = "proteins";
        public final static String COLUMN_FATS = "fats";
        public final static String COLUMN_CARBOHYDRATES = "carbohydrates";
    }
}
