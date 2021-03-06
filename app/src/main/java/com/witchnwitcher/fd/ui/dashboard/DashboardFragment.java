package com.witchnwitcher.fd.ui.dashboard;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.witchnwitcher.fd.EditorActivity;
import com.witchnwitcher.fd.MainActivity;
import com.witchnwitcher.fd.R;
import com.witchnwitcher.fd.data.DishDbHelper;
import com.witchnwitcher.fd.data.DishesContract;
import com.witchnwitcher.fd.databinding.FragmentDashboardBinding;

import org.w3c.dom.Text;

public class DashboardFragment extends Fragment {

    private DishDbHelper mDbHelper;

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final FloatingActionButton fab = binding.floatingActionButton;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new DishDbHelper(getContext());
//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        // ???????????????? ?? ?????????????? ?????? ???????????? ???????? ????????????
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // ?????????????? ?????????????? ?????? ?????????????? - ???????????? ????????????????
        String[] projection = {
                DishesContract.Dish._ID,
                DishesContract.Dish.COLUMN_NAME,
                DishesContract.Dish.COLUMN_CALORIES,
                DishesContract.Dish.COLUMN_PROTEINS,
                DishesContract.Dish.COLUMN_FATS,
                DishesContract.Dish.COLUMN_CARBOHYDRATES
        };

        // ???????????? ????????????
        Cursor cursor = db.query(
                DishesContract.Dish.TABLE_NAME,   // ??????????????
                projection,            // ??????????????
                null,                  // ?????????????? ?????? ?????????????? WHERE
                null,                  // ???????????????? ?????? ?????????????? WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // ?????????????? ????????????????????

        final TextView displayTextView = binding.dbInfoTextView;

        try {
            displayTextView.setText("?????????????? ???????????????? " + cursor.getCount() + " ????????.\n\n");
            displayTextView.append(DishesContract.Dish._ID + " - " +
                    DishesContract.Dish.COLUMN_NAME + " - " +
                    DishesContract.Dish.COLUMN_CALORIES + " - " +
                    DishesContract.Dish.COLUMN_PROTEINS + " - " +
                    DishesContract.Dish.COLUMN_FATS + " - " +
                    DishesContract.Dish.COLUMN_CARBOHYDRATES + "\n");

            // ???????????? ???????????? ?????????????? ??????????????
            int idColumnIndex = cursor.getColumnIndex(DishesContract.Dish._ID);
            int nameColumnIndex = cursor.getColumnIndex(DishesContract.Dish.COLUMN_NAME);
            int caloriesColumnIndex = cursor.getColumnIndex(DishesContract.Dish.COLUMN_CALORIES);
            int proteinsColumnIndex = cursor.getColumnIndex(DishesContract.Dish.COLUMN_PROTEINS);
            int fatsColumnIndex = cursor.getColumnIndex(DishesContract.Dish.COLUMN_FATS);
            int carbohydratesColumnIndex = cursor.getColumnIndex(DishesContract.Dish.COLUMN_CARBOHYDRATES);

            // ???????????????? ?????????? ?????? ????????
            while (cursor.moveToNext()) {
                // ???????????????????? ???????????? ?????? ?????????????????? ???????????? ?????? ??????????
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                float currentCalories = cursor.getFloat(caloriesColumnIndex);
                float currentProteins = cursor.getFloat(proteinsColumnIndex);
                float currentFats = cursor.getFloat(fatsColumnIndex);
                float currentCarbohydrates = cursor.getFloat(carbohydratesColumnIndex);
                // ?????????????? ???????????????? ?????????????? ??????????????
                displayTextView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentCalories + " - " +
                        currentProteins + " - " +
                        currentFats + " - " +
                        currentCarbohydrates));
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