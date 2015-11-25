package com.example.yoshiki.wakeup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by 1013067 on 2015/11/20.
 */
public class EvaluationActivity extends AppCompatActivity {

    private Common common;
    static boolean exitFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        ImageView imageView = (ImageView)findViewById(R.id.image_view);
        imageView.setImageResource(R.drawable.shihyo);

        exitFlag = false;
        common = (Common) getApplication();
        common.init_kao();

        setTitle("睡眠はどうでしたか？");
    }

    private float oldY = 0f;

    public boolean onTouchEvent(MotionEvent event){
//        Log.d("Common", "Common:"+common.smile);
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((-500) <= event.getY() - oldY && event.getY() - oldY < (-400)) {
                    common.smile = 8;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao7);
                } else if ((-400) <= event.getY() - oldY && event.getY() - oldY < (-300)) {
                    common.smile = 7;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao6);
                } else if ((-300) <= event.getY() - oldY && event.getY() - oldY < (-200)) {
                    common.smile = 6;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao5);
                } else if ((-200) <= event.getY() - oldY && event.getY() - oldY < (-100)) {
                    common.smile = 5;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao4);
                } else if ((-100) < event.getY() - oldY && event.getY() - oldY <= 100) {
                    common.smile = 4;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao3);
                }else if (100 < event.getY() - oldY && event.getY() - oldY <= 200) {
                    common.smile = 3;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao2);
                } else if (200 < event.getY() - oldY && event.getY() - oldY <= 300) {
                    common.smile = 2;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao1);
                } else if (300 < event.getY() - oldY && event.getY() - oldY <= 400) {
                    common.smile = 1;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao0);
                }
                break;
            case MotionEvent.ACTION_UP:

                if ((-500) <= event.getY() - oldY && event.getY() - oldY < (-400)) {
//                    common.smile = 8;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao7);
                } else if ((-400) <= event.getY() - oldY && event.getY() - oldY < (-300)) {
//                    common.smile = 7;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao6);
                } else if ((-300) <= event.getY() - oldY && event.getY() - oldY < (-200)) {
//                    common.smile = 6;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao5);
                } else if ((-200) <= event.getY() - oldY && event.getY() - oldY < (-100)) {
//                    common.smile = 5;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao4);
                } else if ((-100) < event.getY() - oldY && event.getY() - oldY <= 100) {
//                    common.smile = 4;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao3);
                }else if (100 < event.getY() - oldY && event.getY() - oldY <= 200) {
//                    common.smile = 3;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao2);
                } else if (200 < event.getY() - oldY && event.getY() - oldY <= 300) {
//                    common.smile = 2;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao1);
                } else if (300 < event.getY() - oldY && event.getY() - oldY <= 400) {
//                    common.smile = 1;
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    imageView.setImageResource(R.drawable.kao0);
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.button1:
                Intent intent = new Intent(this, ActionActivity.class);
                startActivity(intent);

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
