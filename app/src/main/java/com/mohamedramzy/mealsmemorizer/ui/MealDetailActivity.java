package com.mohamedramzy.mealsmemorizer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.mohamedramzy.mealsmemorizer.R;
import com.mohamedramzy.mealsmemorizer.database.DBManager;
import com.mohamedramzy.mealsmemorizer.model.Meal;

/**
 * Created by mmahfouz on 3/2/2016.
 */
public class MealDetailActivity extends AppCompatActivity {
    private final int ADD = 1;
    private final int EDIT = 2;
    int operation = -1;

    private CheckBox editCheckBox;
    private EditText mealNameEditText;
    private ImageView starImageView;

    private DBManager dbManager;

    private Meal currentMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_detail_activity);

        dbManager = DBManager.getInstance(this);

        Button button = (Button) findViewById(R.id.add_save_button);
        mealNameEditText = (EditText)findViewById(R.id.meal_name);
        editCheckBox = (CheckBox)findViewById(R.id.editCheckBox);
        starImageView = (ImageView)findViewById(R.id.star_image_view);

        editCheckBox.setChecked(false);
        mealNameEditText.setEnabled(false);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("meal")){
            operation = EDIT;

            currentMeal = intent.getParcelableExtra("meal");
            mealNameEditText.setText(currentMeal.getName());
            starImageView.setImageResource(currentMeal.getFav() == 1? R.drawable.star : R.drawable.star_blue);

            button.setText("Save");
        }else{
            // add new meal
            operation = ADD;
            mealNameEditText.setText("");
            currentMeal = new Meal();
            currentMeal.setFav(0);
            starImageView.setImageResource(R.drawable.star_blue);

            button.setText("Add");

            editCheckBox.setChecked(true);
            mealNameEditText.setEnabled(true);
        }



        editCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mealNameEditText.setEnabled(true);
                } else {
                    mealNameEditText.setEnabled(false);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (operation) {
                    case ADD:
                        Log.v("ADD", "ADD");
                        // call content resolver to add it to db
                        currentMeal.setName(mealNameEditText.getText().toString());
                        dbManager.insert(currentMeal);
                        break;
                    case EDIT:
                        Log.v("EDIT", "EDIT");
                        // call the content resolver to update them
                        currentMeal.setName(mealNameEditText.getText().toString());
                        dbManager.update(currentMeal);
                        break;
                }
                finish();
            }
        });

        starImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentMeal.getFav() == 1){
                    currentMeal.setFav(0);
                    starImageView.setImageResource(R.drawable.star_blue);
                }else{
                    currentMeal.setFav(1);
                    starImageView.setImageResource(R.drawable.star);
                }
            }
        });
    }
}
