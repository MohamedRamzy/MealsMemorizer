package com.mohamedramzy.mealsmemorizer.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohamedramzy.mealsmemorizer.R;
import com.mohamedramzy.mealsmemorizer.database.DBManager;
import com.mohamedramzy.mealsmemorizer.model.Category;
import com.mohamedramzy.mealsmemorizer.model.Meal;
import com.mohamedramzy.mealsmemorizer.utility.Utility;

import java.util.List;

/**
 * Created by mmahfouz on 3/2/2016.
 */
public class MealDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private final int ADD = 1;
    private final int EDIT = 2;
    int operation = -1;

    private CheckBox editCheckBox;
    private EditText mealNameEditText;
    private ImageView starImageView;
    private ImageView mealIcon;
    private TextView mealCategory;
    private ImageView mButton;

    List<Category> mCategories;

    private DBManager dbManager;

    private Meal currentMeal;
    private int selectedCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_detail_activity);

        dbManager = DBManager.getInstance(this);
        mCategories = Utility.getCategories();

        mButton = (ImageView) findViewById(R.id.add_save_button);
        mealNameEditText = (EditText)findViewById(R.id.meal_name);
        editCheckBox = (CheckBox)findViewById(R.id.editCheckBox);
        starImageView = (ImageView)findViewById(R.id.star_image_view);
        mealIcon = (ImageView)findViewById(R.id.meal_icon);
        mealCategory = (TextView)findViewById(R.id.meal_category);

        editCheckBox.setChecked(false);
        mealNameEditText.setEnabled(false);
        mealCategory.setEnabled(false);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("meal")){
            operation = EDIT;

            currentMeal = intent.getParcelableExtra("meal");
            mealNameEditText.setText(currentMeal.getName());
            starImageView.setImageResource(currentMeal.getFav() == 1 ? R.drawable.star : R.drawable.star_blue);

            mealCategory.setText(Utility.getCategoryName(currentMeal.getCategoryID()));
            selectedCategoryId = currentMeal.getCategoryID();

            mealIcon.setImageResource(Utility.getMealIconFromCategory(currentMeal.getCategoryID()));

            mButton.setTag("Save");
        }else{
            // add new meal
            operation = ADD;
            mealNameEditText.setText("");
            currentMeal = new Meal();
            currentMeal.setFav(0); // no_favourite at start
            selectedCategoryId = Category.CAT_OTHER; // other , need to be updated dynamically
            starImageView.setImageResource(R.drawable.star_blue);

            mealIcon.setImageResource(Utility.getMealIconFromCategory(selectedCategoryId));

            mButton.setTag("Add");

            editCheckBox.setChecked(true);
            mealNameEditText.setEnabled(true);
            mealCategory.setEnabled(true);
        }

        editCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mealNameEditText.setEnabled(true);
                    mealCategory.setEnabled(true);
                } else {
                    mealNameEditText.setEnabled(false);
                    mealCategory.setEnabled(false);
                }
            }
        });
        mButton.setOnClickListener(this);
        starImageView.setOnClickListener(this);
        mealCategory.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(operation == EDIT)
            getMenuInflater().inflate(R.menu.delete_meal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_meal_option :
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Meal")
                        .setMessage("Are you sure you want to delete this meal ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbManager.delete(currentMeal);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.meal_category:
               // expandable list with radio button selection
                final CharSequence[] items = new CharSequence[mCategories.size()];
                for (int i = 0; i < items.length; i++){
                    items[i] = mCategories.get(i).getName();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Make your selection");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        mealCategory.setText(items[item]);
                        selectedCategoryId = mCategories.get(item).getId();
                        mealIcon.setImageResource(Utility.getMealIconFromCategory(selectedCategoryId));
                        currentMeal.setCategoryID(mCategories.get(item).getId());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.star_image_view :
                if(currentMeal.getFav() == 1){
                    currentMeal.setFav(0);
                    starImageView.setImageResource(R.drawable.star_blue);
                }else{
                    currentMeal.setFav(1);
                    starImageView.setImageResource(R.drawable.star);
                }
                break;
            case R.id.add_save_button :
                if(operation == ADD) {
                    Log.v("ADD", "ADD");
                    currentMeal.setName(mealNameEditText.getText().toString());
                    dbManager.insert(currentMeal);
                }else if(operation == EDIT) {
                    Log.v("EDIT", "EDIT");
                    // call the content resolver to update them
                    currentMeal.setName(mealNameEditText.getText().toString());
                    currentMeal.setCategoryID(selectedCategoryId);          // not updated in the db yet
                    dbManager.update(currentMeal);
                }
                finish();
                break;
        }
    }


}
