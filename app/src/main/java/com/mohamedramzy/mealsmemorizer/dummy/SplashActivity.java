package com.mohamedramzy.mealsmemorizer.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mohamedramzy.mealsmemorizer.ui.MainActivity;

/**
 * Created by mmahfouz on 3/5/2016.
 */
public class SplashActivity extends AppCompatActivity {

    // optimized one - load the splash quickly without even inflate layouts
    // https://www.bignerdranch.com/blog/splash-screens-the-right-way/

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
