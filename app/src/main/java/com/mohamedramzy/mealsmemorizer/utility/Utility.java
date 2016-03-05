package com.mohamedramzy.mealsmemorizer.utility;

import com.mohamedramzy.mealsmemorizer.R;
import com.mohamedramzy.mealsmemorizer.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmahfouz on 3/3/2016.
 */
public class Utility {

    static List<Category> mCategories;

    public static int getRandomIndex (int n){
        int index = (int)((n+0.0)*(Math.random()));
        if(index < n)
            return index;
        return 0;
    }

    public static int getMealIconFromCategory(int categoryID){
        switch (categoryID){
            case Category.CAT_CHICKEN :
                return R.drawable.chicken;
            case Category.CAT_MEAT :
                return R.drawable.meat;
            case Category.CAT_FISH :
                return R.drawable.fish;
            case Category.CAT_VEGETABLES:
                return R.drawable.vegetables;
            case Category.CAT_OTHER :
                return R.drawable.other;
            default:
                return R.drawable.other;
        }
    }

    public static List<Category> getCategories() {
        mCategories = new ArrayList<Category>();
        mCategories.add(new Category(Category.CAT_FISH ,"Fish","fish"));
        mCategories.add(new Category(Category.CAT_MEAT ,"Meat","meat"));
        mCategories.add(new Category(Category.CAT_CHICKEN ,"Chicken","chicken"));
        mCategories.add(new Category(Category.CAT_VEGETABLES ,"Vegetables","vegetables"));
        mCategories.add(new Category(Category.CAT_OTHER ,"Other","other"));
        return mCategories;
    }

    public static String getCategoryName(int categoryID){
        if(mCategories == null)
            mCategories = getCategories();
        for (Category c : mCategories){
            if(c.getId() == categoryID)return c.getName();
        }
        return null;
    }

}
