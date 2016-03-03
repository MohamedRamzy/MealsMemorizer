package com.mohamedramzy.mealsmemorizer.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohamedramzy.mealsmemorizer.R;
import com.mohamedramzy.mealsmemorizer.database.DBManager;
import com.mohamedramzy.mealsmemorizer.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MealsFragment.CallBack {

    private List<Meal> mMeals;
    private List<Meal> mFavMeals;

    private static final String MEALS_TAG = "All Meals";
    private static final String FAV_TAG = " Favourites";

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tabs_page);

        // Show status bar
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dbManager = DBManager.getInstance(this);


        final ActionBar actionBar = getSupportActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                //Log.v("onTabSelected : ",tab.getText().toString());
                if(tab.getTag().equals(MEALS_TAG)){
                    getSupportFragmentManager().beginTransaction().replace(R.id.tab_content,new MealsFragment()).commit();
                }else if(tab.getTag().equals(FAV_TAG)){
                    getSupportFragmentManager().beginTransaction().replace(R.id.tab_content,new FavoritesFragment()).commit();
                }
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
        ActionBar.Tab mealsTab = actionBar.newTab().setTag(MEALS_TAG).setText(MEALS_TAG).setIcon(R.drawable.icon_meal).setTabListener(tabListener);
        ActionBar.Tab favouitesTab = actionBar.newTab().setTag(FAV_TAG).setText(FAV_TAG).setIcon(R.drawable.icon_star).setTabListener(tabListener);

        actionBar.addTab(mealsTab);
        actionBar.addTab(favouitesTab);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("MainActivity", "onStart");
        loadMeals();
    }

    public void loadMeals(){
        mMeals = getMeals();
        mFavMeals = getFavMeals();
    }

    public List<Meal> getMeals() {
        Cursor cursor = dbManager.queryAll();
        mMeals = new ArrayList<>();
        if (cursor.getCount() > 0) {
            int i = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Meal meal = new Meal();
                meal.setId(cursor.getInt(cursor.getColumnIndex(DBManager._ID)));
                meal.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_MEAL_NAME)));
                meal.setFav(cursor.getInt(cursor.getColumnIndex(DBManager.COLUMN_MEAL_FAV)));
                mMeals.add(meal);
            }
        }

        /*if(mMeals == null) {
            mMeals = new ArrayList<>();
            for(int i = 1; i < 31 ;i+=3){

                mMeals.add(new Meal(i+"","Meal .................................. #"+i,i%2));

                if(mMeals.get(mMeals.size()-1).getFav() == 1)
                    getFavMeals().add(mMeals.get(mMeals.size()-1));
            }
        }*/
        return mMeals;
    }

    public List<Meal> getFavMeals() {

        Cursor cursor = dbManager.queryAllFavourites();
        mFavMeals = new ArrayList<>();
        if (cursor.getCount() > 0) {
            int i = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Meal meal = new Meal();
                meal.setId(cursor.getInt(cursor.getColumnIndex(DBManager._ID)));
                meal.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_MEAL_NAME)));
                meal.setFav(cursor.getInt(cursor.getColumnIndex(DBManager.COLUMN_MEAL_FAV)));
                mFavMeals.add(meal);
            }
        }

/*
        if(mFavMeals == null)
            mFavMeals = new ArrayList<>();*/
        return mFavMeals;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void openAddMealActivity() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.tab_content,new AddMealFragment()).commit();
        Intent intent = new Intent(this,MealDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void addMeal(Meal m) {
//        getMeals().add(m);
        dbManager.insert(m);
    }

    public void addFavMeal(Meal m){
        getFavMeals().add(m);
    }

    @Override
    public void showMeals() {
        getSupportFragmentManager().beginTransaction().replace(R.id.tab_content, new MealsFragment()).commit();
    }


    public void onItemClicked(Meal meal){
        Intent intent = new Intent(this,MealDetailActivity.class);
        intent.putExtra("meal", (Parcelable)meal);
        startActivity(intent);
    }


    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();
            args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1); // Our object is just an integer :-P
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DemoObjectFragment extends Fragment {

        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fav_fragment, container, false);
           /* Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    Integer.toString(args.getInt(ARG_OBJECT)));*/
            return rootView;
        }
    }
}
