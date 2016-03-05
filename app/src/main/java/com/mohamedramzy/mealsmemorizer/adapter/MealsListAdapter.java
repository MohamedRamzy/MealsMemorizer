package com.mohamedramzy.mealsmemorizer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohamedramzy.mealsmemorizer.R;
import com.mohamedramzy.mealsmemorizer.model.Meal;
import com.mohamedramzy.mealsmemorizer.utility.Utility;

public class MealsListAdapter extends ArrayAdapter<Meal> {

    Context mContext;
    public MealsListAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Meal meal = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.meal_list_item, null);

        TextView nameTextView = (TextView) view.findViewById(R.id.meal_name);
        TextView mealCategory = (TextView) view.findViewById(R.id.meal_type);
        ImageView starImageView = (ImageView) view.findViewById(R.id.star_image_view);
        ImageView iconImageView = (ImageView) view.findViewById(R.id.meal_icon);

        mealCategory.setText(Utility.getCategoryName(meal.getCategoryID()));
        iconImageView.setImageResource(Utility.getMealIconFromCategory(meal.getCategoryID()));

        if(meal.getFav() == 1){
            starImageView.setImageResource(R.drawable.star);
        }else{
            starImageView.setImageResource(R.drawable.star_blue);
        }

        nameTextView.setText(meal.getName());

        return view;
    }

}