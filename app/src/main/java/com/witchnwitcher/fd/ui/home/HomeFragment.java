package com.witchnwitcher.fd.ui.home;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.witchnwitcher.fd.data.DishDbHelper;
import com.witchnwitcher.fd.data.DishesContract;
import com.witchnwitcher.fd.data.NormsContract;
import com.witchnwitcher.fd.data.NormsDbHelper;
import com.witchnwitcher.fd.data.ProgressContract;
import com.witchnwitcher.fd.data.ProgressDbHelper;
import com.witchnwitcher.fd.databinding.FragmentHomeBinding;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private NormsDbHelper mDbHelper;
    private ProgressDbHelper progressDbHelper;
    private DishDbHelper dishDbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mDbHelper = new NormsDbHelper(getContext());
        progressDbHelper = new ProgressDbHelper(getContext());
        dishDbHelper = new DishDbHelper(getContext());

        final Button add_day_dish_button = binding.addDayDishButton;
        add_day_dish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDayDish();
            }
        });

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    private void addDayDish() {
        final EditText input_dish_id_textView = binding.inputDishIdTextView;
        final EditText input_dish_amount_textView = binding.inputDishAmountTextView;

        String selection_ID = input_dish_id_textView.getText().toString();

        int currentId;
        String currentName;
        float currentCalories = 0;
        float currentProteins = 0;
        float currentFats = 0;
        float currentCarbohydrates = 0;
        int currentAmount = 0;

        SQLiteDatabase db = progressDbHelper.getWritableDatabase();
        SQLiteDatabase dishDb = dishDbHelper.getReadableDatabase();

        String dishProjection[] = {
                DishesContract.Dish._ID,
                DishesContract.Dish.COLUMN_NAME,
                DishesContract.Dish.COLUMN_CALORIES,
                DishesContract.Dish.COLUMN_PROTEINS,
                DishesContract.Dish.COLUMN_FATS,
                DishesContract.Dish.COLUMN_CARBOHYDRATES
        };

        String selection = "_id = ?";
        String selectionArgs[] = { selection_ID };

        Cursor dishCursor = dishDb.query(
                DishesContract.Dish.TABLE_NAME,
                dishProjection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        try {
            int idColumnIndex = dishCursor.getColumnIndex(DishesContract.Dish._ID);
            int nameColumnIndex = dishCursor.getColumnIndex(DishesContract.Dish.COLUMN_NAME);
            int caloriesColumnIndex = dishCursor.getColumnIndex(DishesContract.Dish.COLUMN_CALORIES);
            int proteinsColumnIndex = dishCursor.getColumnIndex(DishesContract.Dish.COLUMN_PROTEINS);
            int fatsColumnIndex = dishCursor.getColumnIndex(DishesContract.Dish.COLUMN_FATS);
            int carbohydratesColumnIndex = dishCursor.getColumnIndex(DishesContract.Dish.COLUMN_CARBOHYDRATES);

            String insert_name = "";
            float insert_calories = 0;
            float insert_proteins = 0;
            float insert_fats = 0;
            float insert_carbohydrates = 0;


            while (dishCursor.moveToNext()) {
                int currentID = dishCursor.getInt(idColumnIndex);
                insert_name = dishCursor.getString(nameColumnIndex);
                insert_calories = dishCursor.getFloat(caloriesColumnIndex);
                insert_proteins = dishCursor.getFloat(proteinsColumnIndex);
                insert_fats = dishCursor.getFloat(fatsColumnIndex);
                insert_carbohydrates = dishCursor.getFloat(carbohydratesColumnIndex);
            }

            if (!selection_ID.isEmpty() && !input_dish_amount_textView.getText().toString().isEmpty())
            {
                float insert_amount = Float.parseFloat(input_dish_amount_textView.getText().toString());
                ContentValues values = new ContentValues();
                values.put(ProgressContract.Progress.COLUMN_NAME, insert_name);
                values.put(ProgressContract.Progress.COLUMN_CALORIES, (insert_calories / 100 * insert_amount));
                values.put(ProgressContract.Progress.COLUMN_PROTEINS, (insert_proteins / 100 * insert_amount));
                values.put(ProgressContract.Progress.COLUMN_FATS, (insert_fats / 100 * insert_amount));
                values.put(ProgressContract.Progress.COLUMN_CARBOHYDRATES, (insert_carbohydrates / 100 * insert_amount));
                values.put(ProgressContract.Progress.COLUMN_AMOUNT, insert_amount);

                long newRowId = db.insert(ProgressContract.Progress.TABLE_NAME, null, values);
                Toast.makeText(getContext(), "?????????????? ??????????????????", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getContext(), "???? ???????????? ??????-???? ????????????", Toast.LENGTH_LONG).show();
            }


        } finally {
            dishCursor.close();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        displayDatabaseInfo();
        updateProgress();
    }

    private void updateProgress() {
        final TextView norms_prog = binding.normsProgressTextView;
        final TextView added_dishes = binding.addedDishesTextView;

        SQLiteDatabase normsDb = mDbHelper.getReadableDatabase();

        // ?????????????? ?????????????? ?????? ?????????????? - ???????????? ????????????????
        String[] normsProjection = {
                NormsContract.Norm._ID,
                NormsContract.Norm.COLUMN_CALORIES,
                NormsContract.Norm.COLUMN_PROTEINS,
                NormsContract.Norm.COLUMN_FATS,
                NormsContract.Norm.COLUMN_CARBOHYDRATES
        };

        // ???????????? ????????????
        Cursor normCursor = normsDb.query(
                NormsContract.Norm.TABLE_NAME,   // ??????????????
                normsProjection,            // ??????????????
                null,                  // ?????????????? ?????? ?????????????? WHERE
                null,                  // ???????????????? ?????? ?????????????? WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // ?????????????? ????????????????????

        SQLiteDatabase db = progressDbHelper.getReadableDatabase();

        // ?????????????? ?????????????? ?????? ?????????????? - ???????????? ????????????????
        String[] projection = {
                ProgressContract.Progress._ID,
                ProgressContract.Progress.COLUMN_NAME,
                ProgressContract.Progress.COLUMN_CALORIES,
                ProgressContract.Progress.COLUMN_PROTEINS,
                ProgressContract.Progress.COLUMN_FATS,
                ProgressContract.Progress.COLUMN_CARBOHYDRATES,
                ProgressContract.Progress.COLUMN_AMOUNT
        };

        // ???????????? ????????????
        Cursor cursor = db.query(
                ProgressContract.Progress.TABLE_NAME,   // ??????????????
                projection,            // ??????????????
                null,                  // ?????????????? ?????? ?????????????? WHERE
                null,                  // ???????????????? ?????? ?????????????? WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // ?????????????? ????????????????????

        try {
            int idColumnIndex = cursor.getColumnIndex(ProgressContract.Progress._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProgressContract.Progress.COLUMN_NAME);
            int caloriesColumnIndex = cursor.getColumnIndex(ProgressContract.Progress.COLUMN_CALORIES);
            int proteinsColumnIndex = cursor.getColumnIndex(ProgressContract.Progress.COLUMN_PROTEINS);
            int fatsColumnIndex = cursor.getColumnIndex(ProgressContract.Progress.COLUMN_FATS);
            int carbohydratesColumnIndex = cursor.getColumnIndex(ProgressContract.Progress.COLUMN_CARBOHYDRATES);
            int amountColumnIndex = cursor.getColumnIndex(ProgressContract.Progress.COLUMN_AMOUNT);

            while (cursor.moveToNext()) {
                // ???????????????????? ???????????? ?????? ?????????????????? ???????????? ?????? ??????????
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                float currentCalories = cursor.getFloat(caloriesColumnIndex);
                float currentProteins = cursor.getFloat(proteinsColumnIndex);
                float currentFats = cursor.getFloat(fatsColumnIndex);
                float currentCarbohydrates = cursor.getFloat(carbohydratesColumnIndex);
                int currentAmount = cursor.getInt(amountColumnIndex);
                // ?????????????? ???????????????? ?????????????? ??????????????
                added_dishes.append(String.format("\n%d\n", currentID) + " " + String.format("%s\n", currentName)
                        + " " + String.format("%f ????????\n", currentCalories)
                        + " " + String.format("%f ??\n", currentProteins)
                        + " " + String.format("%f ??\n", currentFats)
                        + " " + String.format("%f ??\n", currentCarbohydrates)
                        + " " + String.format("%d ??\n", currentAmount)
                );
            }

            idColumnIndex = normCursor.getColumnIndex(ProgressContract.Progress._ID);
            caloriesColumnIndex = normCursor.getColumnIndex(ProgressContract.Progress.COLUMN_CALORIES);
            proteinsColumnIndex = normCursor.getColumnIndex(ProgressContract.Progress.COLUMN_PROTEINS);
            fatsColumnIndex = normCursor.getColumnIndex(ProgressContract.Progress.COLUMN_FATS);
            carbohydratesColumnIndex = normCursor.getColumnIndex(ProgressContract.Progress.COLUMN_CARBOHYDRATES);

            while (normCursor.moveToNext()) {
                int currentID = normCursor.getInt(idColumnIndex);
                float currentCalories = normCursor.getFloat(caloriesColumnIndex);
                float currentProteins = normCursor.getFloat(proteinsColumnIndex);
                float currentFats = normCursor.getFloat(fatsColumnIndex);
                float currentCarbohydrates = normCursor.getFloat(carbohydratesColumnIndex);

                norms_prog.append(String.format("\n??????????????: %f", currentCalories));
                norms_prog.append(String.format("\n??????????: %f ??", currentProteins));
                norms_prog.append(String.format("\n????????: %f ??", currentFats));
                norms_prog.append(String.format("\n????????????????: %f ??", currentCarbohydrates));
            }


        } finally {
            cursor.close();
            normCursor.close();
        }

    }

    private void displayDatabaseInfo() {

        // ???????????????? ?? ?????????????? ?????? ???????????? ???????? ????????????
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // ?????????????? ?????????????? ?????? ?????????????? - ???????????? ????????????????
        String[] projection = {
                NormsContract.Norm._ID,
                NormsContract.Norm.COLUMN_CALORIES,
                NormsContract.Norm.COLUMN_PROTEINS,
                NormsContract.Norm.COLUMN_FATS,
                NormsContract.Norm.COLUMN_CARBOHYDRATES
        };

        // ???????????? ????????????
        Cursor cursor = db.query(
                NormsContract.Norm.TABLE_NAME,   // ??????????????
                projection,            // ??????????????
                null,                  // ?????????????? ?????? ?????????????? WHERE
                null,                  // ???????????????? ?????? ?????????????? WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // ?????????????? ????????????????????

        try {
            final TextView norms_show = binding.normsShowTextView;
            // ???????????? ???????????? ?????????????? ??????????????
            int idColumnIndex = cursor.getColumnIndex(NormsContract.Norm._ID);
            int caloriesColumnIndex = cursor.getColumnIndex(NormsContract.Norm.COLUMN_CALORIES);
            int proteinsColumnIndex = cursor.getColumnIndex(NormsContract.Norm.COLUMN_PROTEINS);
            int fatsColumnIndex = cursor.getColumnIndex(NormsContract.Norm.COLUMN_FATS);
            int carbohydratesColumnIndex = cursor.getColumnIndex(NormsContract.Norm.COLUMN_CARBOHYDRATES);

            // ???????????????? ?????????? ?????? ????????
            while (cursor.moveToNext()) {
                // ???????????????????? ???????????? ?????? ?????????????????? ???????????? ?????? ??????????
                int currentID = cursor.getInt(idColumnIndex);
                float currentCalories = cursor.getFloat(caloriesColumnIndex);
                float currentProteins = cursor.getFloat(proteinsColumnIndex);
                float currentFats = cursor.getFloat(fatsColumnIndex);
                float currentCarbohydrates = cursor.getFloat(carbohydratesColumnIndex);
                // ?????????????? ???????????????? ?????????????? ??????????????
                norms_show.setText(String.format("%.1f ????????\n", currentCalories));
                norms_show.append(String.format("%.1f ?? ????????????\n", currentProteins));
                norms_show.append(String.format("%.1f ?? ??????????\n", currentFats));
                norms_show.append(String.format("%.1f ?? ??????????????????\n", currentCarbohydrates));
            }
        } finally {
            // ???????????? ?????????????????? ???????????? ?????????? ????????????
            cursor.close();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}