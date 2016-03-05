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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mohamedramzy.mealsmemorizer.R;
import com.mohamedramzy.mealsmemorizer.adapter.MealsListAdapter;
import com.mohamedramzy.mealsmemorizer.model.Meal;
import com.mohamedramzy.mealsmemorizer.utility.PopupDialog;
import com.mohamedramzy.mealsmemorizer.utility.Utility;

import java.util.List;

/**
 * Created by mmahfouz on 3/2/2016.
 */
public class MealsFragment extends Fragment {

    MealsListAdapter mMealsListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.meals_fragment,container,false);
        ImageView random = (ImageView) rootView.findViewById(R.id.randomMealBtn);
        ImageView addMeal = (ImageView) rootView.findViewById(R.id.addMealBtn);

        mMealsListAdapter = new MealsListAdapter(getActivity(),R.layout.meal_list_item);
        ListView mMealsListView = (ListView) rootView.findViewById(R.id.meals_listview);
        mMealsListView.setAdapter(mMealsListAdapter);


        mMealsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((CallBack)getActivity()).onItemClicked(mMealsListAdapter.getItem(position));
            }
        });

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), mMealsListAdapter.getItem(mMealsListAdapter.getCount() / 2).getName(), Toast.LENGTH_SHORT).show();
                showRandomMealAlert() ;
            }
        });

        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CallBack)getActivity()).openAddMealActivity();
//                showAlert();
            }
        });


        // empty list
        LinearLayout noMealsLayout = (LinearLayout) rootView.findViewById(R.id.no_meals_layout);

        if(((MainActivity)getActivity()).getMeals().size() == 0){
            noMealsLayout.setVisibility(View.VISIBLE);
            mMealsListView.setVisibility(View.GONE);
        }
        return rootView;
    }



    public void showRandomMealAlert(){
        if(mMealsListAdapter.getCount() == 0){
            Toast.makeText(getActivity(), "No meals yet :D", Toast.LENGTH_SHORT).show();
            return;
        }
        int index = Utility.getRandomIndex(mMealsListAdapter.getCount());
        Meal meal = mMealsListAdapter.getItem(index);

        PopupDialog popupDialog = new PopupDialog(getActivity());
        popupDialog.showPopupMeal(meal);
    }

    public void showAlert(){
        final EditText txtUrl = new EditText(getActivity());

        // Set the default text to a link of the Queen
        txtUrl.setHint("Meal Name");

        new AlertDialog.Builder(getActivity())
                .setTitle("Add Meal")
                .setMessage("Enter Meal Name")
                .setView(txtUrl)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = txtUrl.getText().toString();
                        ((CallBack) getActivity()).addMeal(new Meal(1, name, 1));
                        mMealsListAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }


    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        mMealsListAdapter.clear();
        List<Meal> meals = ((MainActivity)getActivity()).getMeals();
        for(int i = 0; i < meals.size(); i++){
            mMealsListAdapter.add(meals.get(i));
        }
        mMealsListAdapter.notifyDataSetChanged();
    }

    public class ViewHolder{

    }
    public interface CallBack{
        public void openAddMealActivity();
        public void addMeal(Meal m);
        public void addFavMeal(Meal m);
        public void showMeals();
        public void onItemClicked(Meal m);
    }
}
