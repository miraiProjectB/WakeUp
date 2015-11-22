package com.example.yoshiki.wakeup;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import java.util.ArrayList;

public class ScatterActivity extends AppCompatActivity implements OnChartValueSelectedListener, ActionBar.OnNavigationListener,
        View.OnClickListener {
    ScatterChart sChart;
    Typeface tf;
    public ScatterDataSet set1;
    SpinnerAdapter mSpinnerAdapter;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ボタンを生成
        //ImageButton imgbtn = new ImageButton(this);
        //ImageButton imgbt = (ImageButton)findViewById(R.id.imageButton);
        //imgbt.setImageResource(R.drawable.caffeine);
        // レイアウトにボタンを追加
        /*LinearLayout layout = new LinearLayout(this);
        layout.addView(imgbt, new LinearLayout.LayoutParams( WC, WC));
        setContentView(layout);*/
        //requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルを非表示にします
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // フルスクリーン表示する
        setContentView(R.layout.activity_scatter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sChart = (ScatterChart) findViewById(R.id.chart);
        sChart.setDescription("");//グラフの説明
        tf = Typeface.create(Typeface.SERIF, Typeface.ITALIC);
        sChart.setOnChartValueSelectedListener(this);
        sChart.setDrawGridBackground(false);
        sChart.setTouchEnabled(true);
        // enable scaling and dragging
        sChart.setDragEnabled(true);
        sChart.setScaleEnabled(true);
        sChart.setMaxVisibleValueCount(200);
        sChart.setPinchZoom(true);
        //mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action_list,android.R.layout.simple_spinner_dropdown_item);
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action_list,
                android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
        Legend l = sChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setTypeface(tf);
        YAxis yl = sChart.getAxisLeft();
        yl.setTypeface(tf);
        sChart.getAxisRight().setEnabled(false);
        XAxis xl = sChart.getXAxis();
        xl.setTypeface(tf);
        xl.setDrawGridLines(false);
        ArrayList<String> xVals = new ArrayList<String>();
        //仮
        for(int i=0;i<=16000;i++)  xVals.add((i)+"");
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        yVals1.add(new Entry( 11,489));
        yVals1.add(new Entry(11,2198));
        yVals1.add(new Entry(11,2269));
        yVals1.add(new Entry( 11,7336));
        yVals1.add(new Entry(12,555));
        yVals1.add(new Entry( 12,592));
        yVals1.add(new Entry( 12,3144));
        yVals1.add(new Entry( 12,4910));
        yVals1.add(new Entry( 12,5447));
        yVals1.add(new Entry(12,5790));
        yVals1.add(new Entry( 12,5933));
        yVals1.add(new Entry( 12,8544));
        yVals1.add(new Entry( 12,13785));
        yVals1.add(new Entry( 13,2065));
        yVals1.add(new Entry( 13,2429));
        yVals1.add(new Entry(13,5823));
        yVals1.add(new Entry( 16,5378));
        yVals1.add(new Entry( 16,5871));
        yVals1.add(new Entry( 22,693));
        set1 = new ScatterDataSet(yVals1, "入眠時間と活動量");
        set1.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set1.setColor(ColorTemplate.COLORFUL_COLORS[1]);
        set1.setScatterShapeSize(8f);
        ArrayList<ScatterDataSet> dataSets = new ArrayList<ScatterDataSet>();
        dataSets.add(set1); // add the datasets
        ScatterData data = new ScatterData(xVals, dataSets);     // create a data object with the datasets
        data.setValueTypeface(tf);
        set1.setDrawValues(!set1.isDrawValuesEnabled());//データ値の表示を消す処理
        sChart.setData(data);
        sChart.animateX(1000);//指定時間のアニメーションで描画
        sChart.invalidate();/*グラフ再描画*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.Values: {
                set1.setDrawValues(!set1.isDrawValuesEnabled());//データ値の表示を消す処理
                sChart.invalidate();
                break;
            }
            case R.id.action:{
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("VAL SELECTED", "Value: " + e.getVal() + ", xIndex: " + e.getXIndex() + ", DataSet index: " + dataSetIndex);
        sChart.setDescription("入眠時間："+(int)e.getVal()+"  \n  "+"活動量："+e.getXIndex()+"");//グラフの説明
        //sChart.setData();
    }
    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub
        sChart.setDescription("");//グラフの説明
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        Log.d("TAG", "select item = " + itemPosition);
        return true;
    }

    public void onClick(View v) {
        /* .... */
    }
}
