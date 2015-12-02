package com.example.yoshiki.wakeup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by 1013067 on 2015/11/20.
 */
public class ActionActivity extends AppCompatActivity {

    private Common common;
    static boolean exitFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        setTitle("行動チェック");

        common = (Common) getApplication();
        common.init_select();
    }

    public void onClick(View view){
        switch (view.getId()){

            case R.id.mame:
                Intent goMame = new Intent(this, KnowledgeActivity.class);
                startActivity(goMame);
                break;

            case R.id.next_fab:
                Intent intent = new Intent(this, CheckActivity.class);
                startActivity(intent);

                break;

            case R.id.cafein1:
                if(common.cafein_count == 1) {
                    Button button = (Button) findViewById(R.id.cafein1);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.cafein_count = 0;
                }else if(common.cafein_count == 0){
                    Button button = (Button) findViewById(R.id.cafein1);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.cafein_count = 1;
                }
                break;

            case R.id.cafein2:
                if(common.cafein_count == 2) {
                    Button button = (Button) findViewById(R.id.cafein2);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.cafein_count = 0;
                }else if(common.cafein_count == 0){
                    Button button = (Button) findViewById(R.id.cafein2);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.cafein_count = 2;
                }
                break;

            case R.id.cafein3:
                if(common.cafein_count == 3) {
                    Button button = (Button) findViewById(R.id.cafein3);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.cafein_count = 0;
                }else if(common.cafein_count == 0){
                    Button button = (Button) findViewById(R.id.cafein3);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.cafein_count = 3;
                }
                break;

            case R.id.bath1:
                if(common.bath_count == 1){
                    Button button = (Button) findViewById(R.id.bath1);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.bath_count = 0;
                }else if(common.bath_count == 0){
                    Button button = (Button) findViewById(R.id.bath1);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.bath_count = 1;
                }
                break;

            case R.id.bath2:
                if(common.bath_count == 2){
                    Button button = (Button) findViewById(R.id.bath2);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.bath_count = 0;
                }else if(common.bath_count == 0){
                    Button button = (Button) findViewById(R.id.bath2);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.bath_count = 2;
                }
                break;

            case R.id.bath3:
                if(common.bath_count == 3){
                    Button button = (Button) findViewById(R.id.bath3);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.bath_count = 0;
                }else if(common.bath_count == 0){
                    Button button = (Button) findViewById(R.id.bath3);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.bath_count = 3;
                }
                break;

            case R.id.beer1:
                if(common.beer_count == 1){
                    Button button = (Button) findViewById(R.id.beer1);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.beer_count = 0;
                }else if(common.beer_count == 0){
                    Button button = (Button) findViewById(R.id.beer1);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.beer_count = 1;
                }
                break;

            case R.id.beer2:
                if(common.beer_count == 2){
                    Button button = (Button) findViewById(R.id.beer2);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.beer_count = 0;
                }else if(common.beer_count == 0){
                    Button button = (Button) findViewById(R.id.beer2);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.beer_count = 2;
                }
                break;

            case R.id.beer3:
                if(common.beer_count == 3){
                    Button button = (Button) findViewById(R.id.beer3);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.beer_count = 0;
                }else if(common.beer_count == 0){
                    Button button = (Button) findViewById(R.id.beer3);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.beer_count = 3;
                }
                break;

            case R.id.sport1:
                if(common.sport_count == 1){
                    Button button = (Button) findViewById(R.id.sport1);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.sport_count = 0;
                }else if(common.sport_count == 0){
                    Button button = (Button) findViewById(R.id.sport1);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.sport_count = 1;
                }
                break;

            case R.id.sport2:
                if(common.sport_count == 2){
                    Button button = (Button) findViewById(R.id.sport2);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.sport_count = 0;
                }else if(common.sport_count == 0){
                    Button button = (Button) findViewById(R.id.sport2);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.sport_count = 2;
                }
                break;

            case R.id.sport3:
                if(common.sport_count == 3){
                    Button button = (Button) findViewById(R.id.sport3);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.sport_count = 0;
                }else if(common.sport_count == 0){
                    Button button = (Button) findViewById(R.id.sport3);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.sport_count = 3;
                }
                break;

            case R.id.degital1:
                if(common.degital_count == 1){
                    Button button = (Button) findViewById(R.id.degital1);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.degital_count = 0;
                }else if(common.degital_count == 0){
                    Button button = (Button) findViewById(R.id.degital1);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.degital_count = 1;
                }
                break;

            case R.id.light1:
                if(common.light_count == 1){
                    Button button = (Button) findViewById(R.id.light1);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.light_count = 0;
                }else if(common.light_count == 0){
                    Button button = (Button) findViewById(R.id.light1);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.light_count = 1;
                }
                break;

            case R.id.food1:
                if(common.food_count == 1){
                    Button button = (Button) findViewById(R.id.food1);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.food_count = 0;
                }else if(common.food_count == 0){
                    Button button = (Button) findViewById(R.id.food1);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.food_count = 1;
                }
                break;

            case R.id.smoke1:
                if(common.smoke_count == 1){
                    Button button = (Button) findViewById(R.id.smoke1);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.smoke_count = 0;
                }else if(common.smoke_count == 0){
                    Button button = (Button) findViewById(R.id.smoke1);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.smoke_count = 1;
                }
                break;
        }
    }
    @Override
    protected void  onResume(){
        super.onResume();

    }
    @Override
    protected  void onPause(){
        super.onPause();
    }
    @Override
    public void onRestart(){
        super.onRestart();
        if(exitFlag == true){
            this.finish();
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
