package com.witchnwitcher.fd.data;

import android.provider.BaseColumns;

public final class NormsContract {
    private NormsContract() {};

    public static final class Norm implements BaseColumns {
        public final static String TABLE_NAME = "norms";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CALORIES = "calories";
        public final static String COLUMN_PROTEINS = "proteins";
        public final static String COLUMN_FATS = "fats";
        public final static String COLUMN_CARBOHYDRATES = "carbohydrates";
    }
}
