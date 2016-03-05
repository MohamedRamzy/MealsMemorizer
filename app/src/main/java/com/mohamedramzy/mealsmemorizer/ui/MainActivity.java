package com.mohamedramzy.mealsmemorizer.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mohamedramzy.mealsmemorizer.R;
import com.mohamedramzy.mealsmemorizer.adapter.LeftPanelListAdapter;
import com.mohamedramzy.mealsmemorizer.database.DBManager;
import com.mohamedramzy.mealsmemorizer.model.Meal;
import com.mohamedramzy.mealsmemorizer.utility.LeftPanelItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MealsFragment.CallBack {



    private static final String MEALS_TAG = "All Meals";
    private static final String FAV_TAG = " Favourites";

    private List<Meal> mMeals;
    private List<Meal> mFavMeals;

    ActionBar.Tab mealsTab;
    ActionBar.Tab favouitesTab;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    LeftPanelListAdapter mLeftPanelListAdapter;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tabs_page);

        // Show status bar
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dbManager = DBManager.getInstance(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mLeftPanelListAdapter = new LeftPanelListAdapter(this,R.layout.left_panel_list_item);
        mDrawerList.setAdapter(mLeftPanelListAdapter);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        navigateToAllMeals();
                        break;
                    case 1:
                        navigateToFavourites();
                        break;
                    case 2:
                        navigateToAddMeal();
                        break;
                }
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });


        final ActionBar actionBar = getSupportActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                //Log.v("onTabSelected : ",tab.getText().toString());
                if(tab.getTag().equals(MEALS_TAG)){
                    mealsTab.setIcon(R.drawable.meals_bar_icon_clicked);
                    favouitesTab.setIcon(R.drawable.fav_bar_icon);
                    getSupportFragmentManager().beginTransaction().replace(R.id.tab_content, new MealsFragment()).commit();
                }else if(tab.getTag().equals(FAV_TAG)){
                    mealsTab.setIcon(R.drawable.meals_bar_icon);
                    favouitesTab.setIcon(R.drawable.fav_bar_icon_clicked);
                    getSupportFragmentManager().beginTransaction().replace(R.id.tab_content,new FavoritesFragment()).commit();
                }
                mDrawerLayout.closeDrawer(mDrawerList);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                //Log.v("onTabUnselected : ",tab.getText().toString());
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };
        /*
        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 3; i++) {
            //actionBar.addTab(actionBar.newTab().setIcon(R.drawable.meal).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("Tab " + (i + 1)).setIcon(R.drawable.meal).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("Tab " + (i + 1)).setTabListener(tabListener));
        }*/
        mealsTab = actionBar.newTab().setTag(MEALS_TAG).setText(MEALS_TAG).setIcon(R.drawable.meals_bar_icon).setTabListener(tabListener);
        favouitesTab = actionBar.newTab().setTag(FAV_TAG).setText(FAV_TAG).setIcon(R.drawable.fav_bar_icon).setTabListener(tabListener);

        actionBar.addTab(mealsTab);
        actionBar.addTab(favouitesTab);
    }


    @Override
    public void navigateToAllMeals(){
        mealsTab.setIcon(R.drawable.meals_bar_icon_clicked);
        favouitesTab.setIcon(R.drawable.fav_bar_icon);
        getSupportFragmentManager().beginTransaction().replace(R.id.tab_content, new MealsFragment()).commit();
    }

    @Override
    public void navigateToFavourites(){
        mealsTab.setIcon(R.drawable.meals_bar_icon);
        favouitesTab.setIcon(R.drawable.fav_bar_icon_clicked);
        getSupportFragmentManager().beginTransaction().replace(R.id.tab_content,new FavoritesFragment()).commit();
    }

    @Override
    public void navigateToAddMeal() {
        Intent intent = new Intent(this,MealDetailActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("MainActivity", "onStart");
        loadData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void loadData(){
        mMeals = getMeals();
        mFavMeals = getFavMeals();
        loadLeftPanelInfo();
    }



    private void loadLeftPanelInfo() {
        mLeftPanelListAdapter.clear();

        String [] titles = {"All Meals","Favourites","Add Meal"};
        int [] icons = {R.drawable.meals_bar_icon_clicked,R.drawable.fav_bar_icon_clicked,R.drawable.add};

        for(int i = 0; i < titles.length; i++)
            mLeftPanelListAdapter.add(new LeftPanelItem(titles[i],icons[i]));

        mLeftPanelListAdapter.notifyDataSetChanged();
    }

    public List<Meal> getMeals() {
        Cursor cursor = dbManager.queryAll();
        mMeals = new ArrayList<>();
        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Meal meal = new Meal();
                meal.setId(cursor.getInt(cursor.getColumnIndex(DBManager._ID)));
                meal.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_MEAL_NAME)));
                meal.setFav(cursor.getInt(cursor.getColumnIndex(DBManager.COLUMN_MEAL_FAV)));
                meal.setCategoryID(cursor.getInt(cursor.getColumnIndex(DBManager.COLUMN_MEAL_CAT)));  // check this when added to db
                mMeals.add(meal);
            }
        }
        return mMeals;
    }

    public List<Meal> getFavMeals() {

        Cursor cursor = dbManager.queryAllFavourites();
        mFavMeals = new ArrayList<>();
        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Meal meal = new Meal();
                meal.setId(cursor.getInt(cursor.getColumnIndex(DBManager._ID)));
                meal.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_MEAL_NAME)));
                meal.setFav(cursor.getInt(cursor.getColumnIndex(DBManager.COLUMN_MEAL_FAV)));
                meal.setCategoryID(cursor.getInt(cursor.getColumnIndex(DBManager.COLUMN_MEAL_CAT)));  // check this when added to db
                mFavMeals.add(meal);
            }
        }
        return mFavMeals;
    }


    @Override
    public void addMeal(Meal m) {
        dbManager.insert(m);
    }

    public void addFavMeal(Meal m){
        mFavMeals.add(m);
    }

    @Override
    public void showMeals() {
        getSupportFragmentManager().beginTransaction().replace(R.id.tab_content, new MealsFragment()).commit();
    }


    public void onItemClicked(Meal meal){
        Intent intent = new Intent(this,MealDetailActivity.class);
        intent.putExtra("meal", (Parcelable) meal);
        startActivity(intent);
    }
}
