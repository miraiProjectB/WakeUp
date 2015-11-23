package com.example.yoshiki.wakeup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
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

        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        int cafein_y = (int) frameLayout.getY();

        FrameLayout frameLayout1 = (FrameLayout)findViewById(R.id.frameLayout1);
        int bath_y = (int) frameLayout1.getY();

        FrameLayout frameLayout2 = (FrameLayout)findViewById(R.id.frameLayout2);
        int beer_y = (int) frameLayout2.getY();

        FrameLayout frameLayout3 = (FrameLayout)findViewById(R.id.frameLayout3);
        int sport_y = (int) frameLayout3.getY();

        FrameLayout frameLayout4 = (FrameLayout)findViewById(R.id.frameLayout4);
        int degital_y = (int) frameLayout4.getY();

        FrameLayout frameLayout5 = (FrameLayout)findViewById(R.id.frameLayout5);
        int light_y = (int) frameLayout5.getY();

        FrameLayout frameLayout6 = (FrameLayout)findViewById(R.id.frameLayout6);
        int food_y = (int) frameLayout6.getY();

        FrameLayout frameLayout7 = (FrameLayout)findViewById(R.id.frameLayout7);
        int smoke_y = (int) frameLayout7.getY();

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
