package com.example.yoshiki.wakeup;

import android.app.Application;

/**
 * Created by 1013067 on 2015/11/20.
 */
public class Common extends Application {

    int smile;
    int smoke_count;
    int cafein1_count, cafein2_count, cafein3_count;
    int degital_count;
    int light_count;
    int bath1_count, bath2_count, bath3;
    int beer1_count, beer2_count, beer3_count;
    int food_count;
    int sport1_count, sport2_count, sport3_count;

    public void init_kao() {
        smile = 0;
    }
    public void init_select(){
        smoke_count = 0;
        cafein1_count = 0;
        cafein2_count = 0;
        cafein3_count = 0;
        degital_count = 0;
        light_count = 0;
        bath1_count = 0;
        bath2_count = 0;
        bath3 = 0;
        beer1_count = 0;
        beer2_count = 0;
        beer3_count = 0;
        food_count = 0;
        sport1_count = 0;
        sport2_count = 0;
        sport3_count = 0;
    }
}
