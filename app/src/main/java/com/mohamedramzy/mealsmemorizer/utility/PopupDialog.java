package com.mohamedramzy.mealsmemorizer.utility;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohamedramzy.mealsmemorizer.R;
import com.mohamedramzy.mealsmemorizer.model.Meal;

/**
 * Created by mmahfouz on 3/5/2016.
 */
public class PopupDialog {

    private Context mContext;
    public PopupDialog(Context context){
        this.mContext = context;
    }

    public void showPopupMeal(Meal meal){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialoglayout = inflater.inflate(R.layout.popup_dialog, null);

        TextView mealName = (TextView)dialoglayout.findViewById(R.id.meal_name);
        ImageView mealIcon = (ImageView)dialoglayout.findViewById(R.id.meal_icon);

        mealName.setText(meal.getName());
        mealIcon.setImageResource(Utility.getMealIconFromCategory(meal.getCategoryID()));

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialoglayout);
        builder.show();
    }
}
