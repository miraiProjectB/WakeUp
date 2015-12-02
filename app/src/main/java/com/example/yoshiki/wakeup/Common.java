package com.example.yoshiki.wakeup;

import android.app.Application;

/**
 * Created by 1013067 on 2015/11/20.
 */
public class Common extends Application {

    int smile;
    int smoke_count;
    int cafein_count;
    int degital_count;
    int light_count;
    int bath_count;
    int beer_count;
    int food_count;
    int sport_count;

    int week_count;
    int month_count;
    int year_count;

    public void init_kao() {
        smile = 0;
    }
    public void init_select(){
        smoke_count = 0;
        cafein_count = 0;
        degital_count = 0;
        light_count = 0;
        bath_count = 0;
        beer_count = 0;
        food_count = 0;
        sport_count = 0;
    }
    public void init_top_select(){
        week_count = 0;
        month_count = 0;
        year_count = 0;
    }
}
