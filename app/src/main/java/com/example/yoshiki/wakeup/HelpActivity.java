package com.example.yoshiki.wakeup;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("WAKE UP - ヘルプ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.help_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.mame_link:
                Intent goMame = new Intent(this, KnowledgeActivity.class);
                startActivity(goMame);
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        ScrollView sv = (ScrollView)findViewById(R.id.scrollView);

        LinearLayout linearLayout1 = (LinearLayout)findViewById(R.id.LinearLayout1);
        int  rhythm_y= (int) linearLayout1.getY();

        LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.LinearLayout2);
        int  rem_y= (int) linearLayout2.getY();

        LinearLayout linearLayout3 = (LinearLayout)findViewById(R.id.LinearLayout3);
        int  non_rem_y= (int) linearLayout3.getY();

        LinearLayout linearLayout4 = (LinearLayout)findViewById(R.id.LinearLayout4);
        int  up_y= (int) linearLayout4.getY();

        LinearLayout linearLayout5 = (LinearLayout)findViewById(R.id.LinearLayout5);
        int  graph_y= (int) linearLayout5.getY();

        LinearLayout linearLayout6 = (LinearLayout)findViewById(R.id.LinearLayout6);
        int  act_y= (int) linearLayout6.getY();


        if (itemId == R.id.rhythm) {
            sv.smoothScrollTo(0, rhythm_y);
        }else if(itemId == R.id.rem){
            sv.smoothScrollTo(0, rem_y);
        }else if(itemId == R.id.non_rem){
            sv.smoothScrollTo(0, non_rem_y);
        }else if(itemId == R.id.up){
            sv.smoothScrollTo(0, up_y);
        }else if(itemId == R.id.graph){
            sv.smoothScrollTo(0, graph_y);
        }else if(itemId == R.id.act){
            sv.smoothScrollTo(0, act_y);
        }

        return super.onOptionsItemSelected(item);
    }
}
