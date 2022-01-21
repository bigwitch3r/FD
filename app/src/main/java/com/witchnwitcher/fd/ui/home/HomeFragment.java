package com.witchnwitcher.fd.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.witchnwitcher.fd.data.DishesContract;
import com.witchnwitcher.fd.data.NormsContract;
import com.witchnwitcher.fd.data.NormsDbHelper;
import com.witchnwitcher.fd.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private NormsDbHelper mDbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mDbHelper = new NormsDbHelper(getContext());

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                DishesContract.Dish._ID,
                NormsContract.Norm.COLUMN_CALORIES,
                NormsContract.Norm.COLUMN_PROTEINS,
                NormsContract.Norm.COLUMN_FATS,
                NormsContract.Norm.COLUMN_CARBOHYDRATES
        };

        // Делаем запрос
        Cursor cursor = db.query(
                NormsContract.Norm.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        final TextView displayTextView = binding.normsTextView;

        try {
            displayTextView.setText("Таблица содержит " + cursor.getCount() + " блюд.\n\n");
            displayTextView.append(NormsContract.Norm._ID + " - " +
                    NormsContract.Norm.COLUMN_CALORIES + " - " +
                    NormsContract.Norm.COLUMN_PROTEINS + " - " +
                    NormsContract.Norm.COLUMN_FATS + " - " +
                    NormsContract.Norm.COLUMN_CARBOHYDRATES + "\n");

            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(NormsContract.Norm._ID);
            int caloriesColumnIndex = cursor.getColumnIndex(NormsContract.Norm.COLUMN_CALORIES);
            int proteinsColumnIndex = cursor.getColumnIndex(NormsContract.Norm.COLUMN_PROTEINS);
            int fatsColumnIndex = cursor.getColumnIndex(NormsContract.Norm.COLUMN_FATS);
            int carbohydratesColumnIndex = cursor.getColumnIndex(NormsContract.Norm.COLUMN_CARBOHYDRATES);

            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                float currentCalories = cursor.getFloat(caloriesColumnIndex);
                float currentProteins = cursor.getFloat(proteinsColumnIndex);
                float currentFats = cursor.getFloat(fatsColumnIndex);
                float currentCarbohydrates = cursor.getFloat(carbohydratesColumnIndex);
                // Выводим значения каждого столбца
                displayTextView.append(("\n" + currentID + " - " +
                        currentCalories + " - " +
                        currentProteins + " - " +
                        currentFats + " - " +
                        currentCarbohydrates));
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}