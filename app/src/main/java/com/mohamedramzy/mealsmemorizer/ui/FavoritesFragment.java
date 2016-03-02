package com.mohamedramzy.mealsmemorizer.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mohamedramzy.mealsmemorizer.R;
import com.mohamedramzy.mealsmemorizer.adapter.MealsListAdapter;
import com.mohamedramzy.mealsmemorizer.model.Meal;
import com.mohamedramzy.mealsmemorizer.utility.Utility;

import java.util.List;

/**
 * Created by mmahfouz on 3/2/2016.
 */
public class FavoritesFragment extends Fragment {

    MealsListAdapter mFavMealsListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fav_fragment,container,false);
        ImageView random = (ImageView) rootView.findViewById(R.id.fav_random_btn);

        mFavMealsListAdapter = new MealsListAdapter(getActivity(),R.layout.meal_list_item);
        ListView mMealsListView = (ListView) rootView.findViewById(R.id.meals_listview);
        mMealsListView.setAdapter(mFavMealsListAdapter);

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), mFavMealsListAdapter.getItem(mFavMealsListAdapter.getCount() / 2).getName(), Toast.LENGTH_SHORT).show();
                showRandomMealAlert();
            }
        });

        mMealsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MealsFragment.CallBack)getActivity()).onItemClicked(mFavMealsListAdapter.getItem(position));
            }
        });


        // empty list
        LinearLayout noMealsLayout = (LinearLayout) rootView.findViewById(R.id.no_meals_layout);

        if(((MainActivity)getActivity()).getFavMeals().size() == 0){
            noMealsLayout.setVisibility(View.VISIBLE);
            mMealsListView.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    public void showRandomMealAlert(){
        if(mFavMealsListAdapter.getCount() == 0){
            Toast.makeText(getActivity(), "No favourite meals yet :D", Toast.LENGTH_SHORT).show();
            return;
        }

        int index = Utility.getRandomIndex(mFavMealsListAdapter.getCount());
        String meal = mFavMealsListAdapter.getItem(index).getName();
        new AlertDialog.Builder(getActivity())
                .setTitle("Random Favourite Meal")
                .setMessage(meal + " :)")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();
    }


    private void loadData() {
        mFavMealsListAdapter.clear();
        List<Meal> favMeals = ((MainActivity)getActivity()).getFavMeals();
        for(int i = 0; i < favMeals.size(); i++){
            mFavMealsListAdapter.add(favMeals.get(i));
        }
        mFavMealsListAdapter.notifyDataSetChanged();
    }


}
