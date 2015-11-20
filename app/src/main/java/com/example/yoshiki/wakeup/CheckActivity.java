package com.example.yoshiki.wakeup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by 1013067 on 2015/11/20.
 */
public class CheckActivity extends AppCompatActivity {

    private Common common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        setTitle("これでよろしいですか？");

        common = (Common) getApplication();

        if(common.smile == 1){
            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            imageView.setImageResource(R.drawable.kao0);
        }else if(common.smile == 2){
            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            imageView.setImageResource(R.drawable.kao1);
        }else if(common.smile == 3){
            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            imageView.setImageResource(R.drawable.kao2);
        }else if(common.smile == 4 || common.smile == 0){
            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            imageView.setImageResource(R.drawable.kao3);
        }else if(common.smile == 5){
            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            imageView.setImageResource(R.drawable.kao4);
        }else if(common.smile == 6){
            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            imageView.setImageResource(R.drawable.kao5);
        }else if(common.smile == 7){
            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            imageView.setImageResource(R.drawable.kao6);
        }else if(common.smile == 8){
            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            imageView.setImageResource(R.drawable.kao7);
        }

        if(common.cafein1_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.cafein1);
        }else if(common.cafein2_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.cafein2);
        }else if(common.cafein3_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.cafein3);
        }

        if(common.bath1_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView2);
            imageView.setImageResource(R.drawable.bath1);
        }else if(common.bath2_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView2);
            imageView.setImageResource(R.drawable.bath2);
        }else if(common.bath3 == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView2);
            imageView.setImageResource(R.drawable.bath3);
        }

        if(common.beer1_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView3);
            imageView.setImageResource(R.drawable.beer1);
        }else if(common.beer2_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView3);
            imageView.setImageResource(R.drawable.beer2);
        }else if(common.beer3_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView3);
            imageView.setImageResource(R.drawable.beer3);
        }

        if(common.sport1_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView4);
            imageView.setImageResource(R.drawable.sport1);
        }else if(common.sport2_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView4);
            imageView.setImageResource(R.drawable.sport2);
        }else if(common.sport3_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView4);
            imageView.setImageResource(R.drawable.sport3);
        }

        if(common.degital_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView5);
            imageView.setImageResource(R.drawable.degital1);
        }

        if(common.light_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView6);
            imageView.setImageResource(R.drawable.light1);
        }

        if(common.food_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView7);
            imageView.setImageResource(R.drawable.food1);
        }

        if(common.smoke_count == 1){
            ImageView imageView = (ImageView)findViewById(R.id.imageView8);
            imageView.setImageResource(R.drawable.smoke1);
        }


    }
}
