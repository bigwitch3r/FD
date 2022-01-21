package com.witchnwitcher.fd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.witchnwitcher.fd.data.DishDbHelper;
import com.witchnwitcher.fd.data.DishesContract;

public class EditorActivity extends AppCompatActivity {
    private DishDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);



        Button add_button = (Button) findViewById(R.id.add_dish_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDish();
            }
        });

        Button dropById_button = (Button) findViewById(R.id.drop_by_id_button);
        dropById_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropById();
            }
        });

        mDbHelper = new DishDbHelper(this);

    }

    private void dropById()
    {
        EditText id_editText = (EditText) findViewById(R.id.input_drop_id);

        boolean correct = (!id_editText.getText().toString().isEmpty());

        if (correct)
        {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            String whereClause = "_id = ?";
            String[] whereArgs = new String[]{ id_editText.getText().toString() };
            db.delete(DishesContract.Dish.TABLE_NAME, whereClause, whereArgs);
        }
        else
        {
            Toast.makeText(this, "Проверьте, все ли данные заполнены", Toast.LENGTH_LONG).show();
        }
    }

    private void insertDish() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        EditText name_editText = (EditText) findViewById(R.id.input_dish_name);
        EditText calories_editText = (EditText) findViewById(R.id.input_calories);
        EditText proteins_editText = (EditText) findViewById(R.id.input_proteins);
        EditText fats_editText = (EditText) findViewById(R.id.input_fats);
        EditText carbohydrates_editText = (EditText) findViewById(R.id.input_carbohydrates);

        boolean correct = (!name_editText.getText().toString().isEmpty())
                && (!calories_editText.getText().toString().isEmpty())
                && (!proteins_editText.getText().toString().isEmpty())
                && (!fats_editText.getText().toString().isEmpty())
                && (!carbohydrates_editText.getText().toString().isEmpty());

        if (correct)
        {
            // Создаем объект ContentValues, где имена столбцов ключи,
            // а информация о госте является значениями ключей
            ContentValues values = new ContentValues();
            values.put(DishesContract.Dish.COLUMN_NAME, name_editText.getText().toString());
            values.put(DishesContract.Dish.COLUMN_CALORIES, Float.parseFloat(calories_editText.getText().toString()));
            values.put(DishesContract.Dish.COLUMN_PROTEINS, Float.parseFloat(proteins_editText.getText().toString()));
            values.put(DishesContract.Dish.COLUMN_FATS, Float.parseFloat(fats_editText.getText().toString()));
            values.put(DishesContract.Dish.COLUMN_CARBOHYDRATES, Float.parseFloat(carbohydrates_editText.getText().toString()));

            long newRowId = db.insert(DishesContract.Dish.TABLE_NAME, null, values);
        }
        else
        {
            Toast.makeText(this, "Проверьте, все ли данные заполнены", Toast.LENGTH_LONG).show();
        }
    }
}