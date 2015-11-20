package com.example.yoshiki.wakeup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
}
