package com.witchnwitcher.fd.data;

import android.provider.BaseColumns;

public final class ProgressContract {
    private ProgressContract() {};

    public static final class Progress implements BaseColumns {
        public final static String TABLE_NAME = "progress";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_CALORIES = "calories";
        public final static String COLUMN_PROTEINS = "proteins";
        public final static String COLUMN_FATS = "fats";
        public final static String COLUMN_CARBOHYDRATES = "carbohydrates";
        public final static String COLUMN_AMOUNT = "amount";
    }
}
