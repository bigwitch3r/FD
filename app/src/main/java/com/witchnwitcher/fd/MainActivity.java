package com.witchnwitcher.fd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.witchnwitcher.fd.data.DishesContract;
import com.witchnwitcher.fd.data.NormsContract;
import com.witchnwitcher.fd.data.NormsDbHelper;
import com.witchnwitcher.fd.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NormsDbHelper mDbHelper;
    private NormsDbHelper checkDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDbHelper = new NormsDbHelper(this);
        checkDbHelper = new NormsDbHelper(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    public void calculateCPFC(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        SQLiteDatabase checkDb = checkDbHelper.getReadableDatabase();
        // Layout initialization
        LinearLayout layout = (LinearLayout) this.findViewById(R.id.scroll_layout);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView calories_textView = new TextView(this);
        calories_textView.setLayoutParams(lparams);

        TextView proteins_textView = new TextView(this);
        proteins_textView.setLayoutParams(lparams);

        TextView fats_textView = new TextView(this);
        fats_textView.setLayoutParams(lparams);

        TextView carbohydrates_textView = new TextView(this);
        carbohydrates_textView.setLayoutParams(lparams);

        calories_textView.setTextSize(20);
        proteins_textView.setTextSize(20);
        fats_textView.setTextSize(20);
        carbohydrates_textView.setTextSize(20);

        calories_textView.setTextColor(Color.BLACK);
        proteins_textView.setTextColor(Color.BLACK);
        fats_textView.setTextColor(Color.BLACK);
        carbohydrates_textView.setTextColor(Color.BLACK);

        RadioGroup activity_group = (RadioGroup) findViewById(R.id.activity_group);
        RadioGroup goal_group = (RadioGroup) findViewById(R.id.goal_group);
        RadioGroup gender_group = (RadioGroup) findViewById(R.id.gender_group);

        EditText input_weight = (EditText) findViewById(R.id.input_weight);
        EditText input_height = (EditText) findViewById(R.id.input_height);
        EditText input_age = (EditText) findViewById(R.id.input_age);

        boolean input_correct = (!input_age.getText().toString().isEmpty()) && (!input_height.getText().toString().isEmpty()) && (!input_weight.getText().toString().isEmpty());
        boolean radio_correct = (activity_group.getCheckedRadioButtonId() != -1) && (goal_group.getCheckedRadioButtonId() != -1) && (gender_group.getCheckedRadioButtonId() != -1);
        boolean correct = input_correct && radio_correct;

        double calories = 0;
        double proteins = 0;
        double fats = 0;
        double carbohydrates = 0;
        double multiplier = 0;
        double goal_mult = 0;

        if (correct)
        {
            switch (activity_group.getCheckedRadioButtonId())
            {
                case R.id.passive_button:
                    multiplier = 1.2;
                    break;
                case R.id.two_times_button:
                    multiplier = 1.4;
                    break;
                case R.id.four_five_times_button:
                    multiplier = 1.46;
                    break;
                case R.id.six_times_button:
                    multiplier = 1.55;
                    break;
                case R.id.everyday_button:
                    multiplier = 1.63;
                    break;
                case R.id.twice_a_day_button:
                    multiplier = 1.72;
                    break;
                case R.id.hard_button:
                    multiplier = 1.9;
                    break;
                default:
                    Toast.makeText(getBaseContext(), "???? ???? ?????????????? ?????????? ??????????", Toast.LENGTH_LONG).show();
                    break;
            }

            switch (goal_group.getCheckedRadioButtonId())
            {
                case R.id.lose_weight_button:
                    goal_mult = 0.9;
                    break;
                case R.id.save_weight_button:
                    goal_mult = 1;
                    break;
                case R.id.gain_weight_button:
                    goal_mult = 1.1;
                    break;
                default:
                    Toast.makeText(getBaseContext(), "???? ???? ?????????????? ????????", Toast.LENGTH_LONG).show();
                    break;
            }

            switch (gender_group.getCheckedRadioButtonId())
            {
                case R.id.male_button:
                    double converted_weight = Double.parseDouble(input_weight.getText().toString()) * 9.9;
                    double converted_height = Double.parseDouble(input_height.getText().toString()) * 6.25;
                    double converted_age = Double.parseDouble(input_age.getText().toString()) * 4.92;


                    calories = (converted_weight + converted_height - converted_age + 5) * multiplier * goal_mult;
                    proteins = calories * 0.3 / 4;
                    fats = calories * 0.3 / 9;
                    carbohydrates = calories * 0.4 / 4;

                    calories_textView.setText(String.format("???????? ?????????? ?????????????? %f ????????", calories));
                    proteins_textView.setText(String.format("???????? ?????????? ???????????? %f ??", proteins));
                    fats_textView.setText(String.format("???????? ?????????? ?????????? %f ??", fats));
                    carbohydrates_textView.setText(String.format("???????? ?????????? ?????????????????? %f ??", carbohydrates));

                    layout.addView(calories_textView);
                    layout.addView(proteins_textView);
                    layout.addView(fats_textView);
                    layout.addView(carbohydrates_textView);
                    break;
                case R.id.female_button:
                    converted_weight = Double.parseDouble(input_weight.getText().toString()) * 9.9;
                    converted_height = Double.parseDouble(input_height.getText().toString()) * 6.25;
                    converted_age = Double.parseDouble(input_age.getText().toString()) * 4.92;


                    calories = (converted_weight + converted_height - converted_age - 161) * multiplier * goal_mult;
                    proteins = calories * 0.3 / 4;
                    fats = calories * 0.3 / 9;
                    carbohydrates = calories * 0.4 / 4;

                    calories_textView.setText(String.format("???????? ?????????? ?????????????? %f ????????", calories));
                    proteins_textView.setText(String.format("???????? ?????????? ???????????? %f ??", proteins));
                    fats_textView.setText(String.format("???????? ?????????? ?????????? %f ??", fats));
                    carbohydrates_textView.setText(String.format("???????? ?????????? ?????????????????? %f ??", carbohydrates));

                    layout.addView(calories_textView);
                    layout.addView(proteins_textView);
                    layout.addView(fats_textView);
                    layout.addView(carbohydrates_textView);
                    break;
                default:
                    Toast.makeText(getBaseContext(), "???? ???? ?????????????? ??????", Toast.LENGTH_LONG).show();
                    break;
            }

            String[] projection = {
                    DishesContract.Dish._ID,
                    NormsContract.Norm.COLUMN_CALORIES,
                    NormsContract.Norm.COLUMN_PROTEINS,
                    NormsContract.Norm.COLUMN_FATS,
                    NormsContract.Norm.COLUMN_CARBOHYDRATES
            };

            Cursor cursor = checkDb.query(
                    NormsContract.Norm.TABLE_NAME,   // ??????????????
                    projection,            // ??????????????
                    null,                  // ?????????????? ?????? ?????????????? WHERE
                    null,                  // ???????????????? ?????? ?????????????? WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null);                   // ?????????????? ????????????????????

            ContentValues values = new ContentValues();
            values.put(NormsContract.Norm.COLUMN_CALORIES, calories);
            values.put(NormsContract.Norm.COLUMN_PROTEINS, proteins);
            values.put(NormsContract.Norm.COLUMN_FATS, fats);
            values.put(NormsContract.Norm.COLUMN_CARBOHYDRATES, carbohydrates);

            if (cursor.getCount() == 0)
            {
                long newRowId = db.insert(NormsContract.Norm.TABLE_NAME, null, values);
                Toast.makeText(getBaseContext(), "?????????????? ??????????????????", Toast.LENGTH_LONG).show();
            }
            else
            {
                String whereClause = "_id = ?";
                String whereArgs[] = { "1" };
                long newRowId = db.update(NormsContract.Norm.TABLE_NAME, values, whereClause, whereArgs);
                Toast.makeText(getBaseContext(), "?????????????? ??????????????????", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getBaseContext(), "??????????????????, ?????? ???? ???????????? ???? ??????????????", Toast.LENGTH_LONG).show();
        }
    }
}