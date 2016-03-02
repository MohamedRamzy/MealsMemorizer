package com.mohamedramzy.mealsmemorizer.utility;

/**
 * Created by mmahfouz on 3/3/2016.
 */
public class Utility {

    public static int getRandomIndex (int n){
        int index = (int)((n+0.0)*(Math.random()));
        if(index < n)
            return index;
        return 0;
    }

}
