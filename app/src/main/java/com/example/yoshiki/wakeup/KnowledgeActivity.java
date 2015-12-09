package com.example.yoshiki.wakeup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by 1013067 on 2015/11/20.
 */
public class KnowledgeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        setTitle("睡眠に関する豆知識");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        ScrollView sv = (ScrollView)findViewById(R.id.scrollView);

        LinearLayout linearLayout1 = (LinearLayout)findViewById(R.id.LinearLayout1);
        int cafein_y = (int) linearLayout1.getY();

        LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.LinearLayout2);
        int bath_y = (int) linearLayout2.getY();

        LinearLayout linearLayout3 = (LinearLayout)findViewById(R.id.LinearLayout3);
        int beer_y = (int) linearLayout3.getY();

        LinearLayout linearLayout4 = (LinearLayout)findViewById(R.id.LinearLayout4);
        int sport_y = (int) linearLayout4.getY();

        LinearLayout linearLayout5 = (LinearLayout)findViewById(R.id.LinearLayout5);
        int degital_y = (int) linearLayout5.getY();

        LinearLayout linearLayout6 = (LinearLayout)findViewById(R.id.LinearLayout6);
        int light_y = (int) linearLayout6.getY();

        LinearLayout linearLayout7 = (LinearLayout)findViewById(R.id.LinearLayout7);
        int food_y = (int) linearLayout7.getY();

        LinearLayout linearLayout8 = (LinearLayout)findViewById(R.id.LinearLayout8);
        int smoke_y = (int) linearLayout8.getY();

        if (itemId == R.id.cafein) {
            sv.smoothScrollTo(0, cafein_y);
        }else if(itemId == R.id.bath){
            sv.smoothScrollTo(0, bath_y);
        }else if(itemId == R.id.beer){
            sv.smoothScrollTo(0, beer_y);
        }else if(itemId == R.id.sport){
            sv.smoothScrollTo(0, sport_y);
        }else if(itemId == R.id.degital){
            sv.smoothScrollTo(0, degital_y);
        }else if(itemId == R.id.light){
            sv.smoothScrollTo(0, light_y);
        }else if(itemId == R.id.food){
            sv.smoothScrollTo(0, food_y);
        }else if(itemId == R.id.smoke){
            sv.smoothScrollTo(0, smoke_y);
        }

        return super.onOptionsItemSelected(item);
    }

}
