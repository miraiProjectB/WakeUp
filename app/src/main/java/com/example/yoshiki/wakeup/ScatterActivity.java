package com.example.yoshiki.wakeup;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.lang.System.out;

public class ScatterActivity extends AppCompatActivity implements OnChartValueSelectedListener, ActionBar.OnNavigationListener,
        View.OnClickListener {
    ScatterChart sChart;
    Typeface tf;
    public ScatterDataSet set1,set2,set3;
    SpinnerAdapter mSpinnerAdapter;
    ActionBar actionBar;
    TextView sleeptv,txtView;
    private PopupWindow mPopupWindow;
    private Common common;
    View popupView;
    int itemPosition=0, duration=366,
            sort_flg=0, //ソートするなら１
            sort_menu=0,//ソートする項目
            sort_lv=0;//ソートのレベル
    double Correlation=0;
    String Correlation_description="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        sChart.setDragEnabled(true); // enable scaling and dragging
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
        sChart.animateX(1000);//指定時間のアニメーションで描画
        XAxis xl = sChart.getXAxis();
        xl.setTypeface(tf);
        xl.setDrawGridLines(false);
        sleeptv = (TextView) findViewById(R.id.label_sleep);

        common = (Common) getApplication();
        common.init_top_select();
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
            case R.id.help_call: {
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        //Log.i("VAL SELECTED", "Value: " + e.getVal() + ", xIndex: " + e.getXIndex() + ", DataSet index: " + dataSetIndex);
        sChart.setDescription("睡眠："+(int)e.getVal()+"  \n  "+"活動量："+e.getXIndex()+"");//グラフの説明
    }
    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub
        sChart.setDescription("");//グラフの説明
    }

    @Override
    public boolean onNavigationItemSelected(int Position, long itemId) {
        //Log.d("TAG", "select item = " + itemPosition);
        fileRead(itemPosition = Position, duration);
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.year:
                fileRead(itemPosition,duration=366);
                Toast.makeText(ScatterActivity.this, "年表示", Toast.LENGTH_LONG).show();

                if(common.year_count == 1) {
                    Button ybutton = (Button) findViewById(R.id.year);
                    ybutton.setBackgroundResource(R.drawable.button_design);
                    common.year_count = 0;
                }else if(common.year_count == 0){
                    Button ybutton = (Button) findViewById(R.id.year);
                    ybutton.setBackgroundResource(R.drawable.button_design2);
                    common.month_count = 0;
                    Button mbutton = (Button) findViewById(R.id.month);
                    mbutton.setBackgroundResource(R.drawable.button_design);
                    common.week_count = 0;
                    Button wbutton = (Button) findViewById(R.id.week);
                    wbutton.setBackgroundResource(R.drawable.button_design);
                }
                break;
            case R.id.month:
                fileRead(itemPosition,duration=31);
                Toast.makeText(ScatterActivity.this, "月表示", Toast.LENGTH_LONG).show();
                if(common.month_count == 1) {
                    Button button = (Button) findViewById(R.id.month);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.month_count = 0;
                }else if(common.month_count == 0){
                    Button button = (Button) findViewById(R.id.month);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.month_count = 1;
                    common.year_count = 0;
                    Button ybutton = (Button) findViewById(R.id.year);
                    ybutton.setBackgroundResource(R.drawable.button_design);
                    common.week_count = 0;
                    Button wbutton = (Button) findViewById(R.id.week);
                    wbutton.setBackgroundResource(R.drawable.button_design);
                }
                break;
            case R.id.week:
                fileRead(itemPosition, duration=7);
                Toast.makeText(ScatterActivity.this, "週表示", Toast.LENGTH_LONG).show();
                if(common.week_count == 1) {
                    Button button = (Button) findViewById(R.id.week);
                    button.setBackgroundResource(R.drawable.button_design);
                    common.week_count = 0;
                }else if(common.week_count == 0){
                    Button button = (Button) findViewById(R.id.week);
                    button.setBackgroundResource(R.drawable.button_design2);
                    common.week_count = 1;
                    common.year_count = 0;
                    Button ybutton = (Button) findViewById(R.id.year);
                    ybutton.setBackgroundResource(R.drawable.button_design);
                    common.month_count = 0;
                    Button mbutton = (Button) findViewById(R.id.month);
                    mbutton.setBackgroundResource(R.drawable.button_design);
                }
                break;
            case R.id.fab:
                mPopupWindow = new PopupWindow(ScatterActivity.this);
                popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);
                mPopupWindow.setContentView(popupView);
                mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));// 背景設定
                mPopupWindow.setOutsideTouchable(true); // タップ時に他のViewでキャッチされないための設定
                mPopupWindow.setFocusable(true);
                float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());// 表示サイズの設定 今回は幅300dp
                // mPopupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.MATCH_PARENT);
                mPopupWindow.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                mPopupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
                mPopupWindow.showAsDropDown(v, -1500, -1150);
                break;
            case R.id.cafein1 :
                sort_flg=1; sort_menu=19;sort_lv=1;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.cafein2 :
                sort_flg=1; sort_menu=19;sort_lv=2;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.cafein3 :
                sort_flg=1; sort_menu=19;sort_lv=3;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.bath1 :
                sort_flg=1; sort_menu=20;sort_lv=1;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.bath2 :
                sort_flg=1; sort_menu=20;sort_lv=2;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.bath3 :
                sort_flg=1; sort_menu=20;sort_lv=3;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.beer1 :
                sort_flg=1; sort_menu=21;sort_lv=1;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.beer2 :
                sort_flg=1; sort_menu=21;sort_lv=2;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.beer3 :
                sort_flg=1; sort_menu=21;sort_lv=3;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.sport1 :
                sort_flg=1; sort_menu=22;sort_lv=1;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.sport2 :
                sort_flg=1; sort_menu=22;sort_lv=2;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.sport3:
                sort_flg=1; sort_menu=22;sort_lv=3;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.degital1 :
                sort_flg=1; sort_menu=23;sort_lv=0;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.light1 :
                sort_flg=1; sort_menu=24;sort_lv=0;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.food1 :
                sort_flg=1; sort_menu=25;sort_lv=0;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.smoke1 :
                sort_flg=1; sort_menu=26;sort_lv=0;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
            case R.id.next_button:
                sort_flg=0;
                fileRead(itemPosition, duration);
                mPopupWindow.dismiss();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        super.onDestroy();
    }

    private void fileRead(int itemPosition, int count){//散布図を表示
        String str;
        int sleep=0,sleep2,sleep3,calories,calmax=0,i,j=0,ct=0;
        ArrayList<ScatterDataSet> dataSets = new ArrayList<ScatterDataSet>();
        set1=null;set2=null;set3=null;//yVals1=null;yVals2=null;yVals3=null;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
        try {
            FileInputStream in = new FileInputStream(Environment.getExternalStorageDirectory() + "/wakeup/log.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((str = reader.readLine()) != null) {
                String[] str_line = str.split(",", -1);
                out.println(str_line[sort_menu]);
                if(sort_flg==0 || (sort_flg==1 && str_line[sort_menu]== String.valueOf(sort_lv))) {
                    calories = Integer.parseInt(str_line[8]);//活動量
                    switch (itemPosition) {   //睡眠データ
                        case 0://入眠時間
                            sleep = (Integer.parseInt(str_line[13]) - Integer.parseInt(str_line[10])) / 60;
                            break;
                        case 1://レム
                            sleep = Integer.parseInt(str_line[15]) / 60;
                            break;
                        case 2://深い眠り
                            sleep = Integer.parseInt(str_line[12]) / 60;
                            break;
                        case 3://浅い眠り
                            sleep = (Integer.parseInt(str_line[16]) - Integer.parseInt(str_line[12]) - Integer.parseInt(str_line[15])
                                    - Integer.parseInt(str_line[14])) / 60;
                            break;
                        case 4://眠り合算
                            sleep = Integer.parseInt(str_line[15]) / 60;
                            sleep2 = Integer.parseInt(str_line[12]) / 60;
                            sleep3 = (Integer.parseInt(str_line[16]) - Integer.parseInt(str_line[11]) - Integer.parseInt(str_line[14])
                                    - Integer.parseInt(str_line[14])) / 60;
                            yVals2.add(new Entry(sleep2, calories));
                            yVals3.add(new Entry(sleep3, calories));
                            x.add(calories);
                            y.add(sleep2);
                            x.add(calories);
                            y.add(sleep3);
                            break;
                    }
                    yVals1.add(new Entry(sleep, calories));
                    x.add(calories);
                    y.add(sleep);
                    if (calmax < calories) calmax = calories;
                    if (count >= 7) ct++;
                    if (count == 366 && ct >= 366) break;
                    else if (count == 31 && ct >= 31) break;
                    else if (count == 7 && ct >= 7) break;
                }////////////////////////////
            }
            switch(itemPosition){
                case 0:
                    set1 = new ScatterDataSet(yVals1, "入眠までの時間");
                    sleeptv.setText("入眠までの時間(分)");
                    break;
                case 1:
                    set1 = new ScatterDataSet(yVals1, "レム睡眠");
                    sleeptv.setText("レム睡眠(分)");
                    break;
                case 2:
                    set1 = new ScatterDataSet(yVals1, "深い眠り");
                    sleeptv.setText("深い眠り(分)");
                    break;
                case 3:
                    set1 = new ScatterDataSet(yVals1, "浅い眠り");
                    sleeptv.setText("浅い眠り(分)");
                    break;
                case 4:
                    set1 = new ScatterDataSet(yVals1, "レム睡眠");
                    set2 = new ScatterDataSet(yVals2, "深い眠り");
                    set2.setScatterShape(ScatterChart.ScatterShape.SQUARE);
                    set2.setColor(Color.rgb(255, 20, 20));//red
                    set3 = new ScatterDataSet(yVals3, "浅い眠り");
                    set3.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);
                    set3.setColor(Color.rgb(20, 255, 20));//green
                    set2.setScatterShapeSize(8f);
                    set3.setScatterShapeSize(8f);
                    set2.setDrawValues(!set2.isDrawValuesEnabled());//データ値の表示を消す処理
                    set3.setDrawValues(!set3.isDrawValuesEnabled());//データ値の表示を消す処理
                    sleeptv.setText("睡眠(分)");
                    break;
            }
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { //縦画面だったら
                txtView = (TextView) findViewById(R.id.txtView);//散布図表示用TextView
                Correlation = PearsonsCorrelation(x, y); // ピアソン相関を計算
                txtView.setText("相関係数:" + String.format("%.2f", Correlation) + " " + Correlation_description);//相関係数表示
            }
            set1.setScatterShapeSize(8f);
            set1.setDrawValues(!set1.isDrawValuesEnabled());//データ値の表示を消す処理
            set1.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            set1.setColor(Color.rgb(0, 116, 232));//blue
            dataSets.add(set1); // add the datasets
            if(itemPosition==4){
                dataSets.add(set2);
                dataSets.add(set3);
            }
            ArrayList<String> xVals = new ArrayList<String>();
            for(i=0;i<=calmax;i+=1000) ;
            for(;j<i;j++) xVals.add((j)+"");
            ScatterData data = new ScatterData(xVals, dataSets);
            data.setValueTypeface(tf);
            sChart.setData(data);
            sChart.invalidate();/*グラフ再描画*/
            in.close();
        } catch (FileNotFoundException e ) {	// 読み込むファイルが見つからない場合の例外処理
            e.printStackTrace();
        } catch (IOException e) {	// ファイルの読み込みエラーの場合の例外処理
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {	// 配列の参照失敗の場合の例外処理
            e.printStackTrace();
        }
    }

    //ピアソン相関係数を求める
    public double PearsonsCorrelation( ArrayList<Integer> x,  ArrayList<Integer> y ) {
        int n=x.size(); //xとyの組数
        double  xt=0,yt=0,x2t=0,y2t=0,xyt=0,xh=0,yh,xs,ys,xsd,ysd,r;
        for( int  i=0; i<n; i++)  {
            xt += x.get(i);   yt += y.get(i);
            x2t += x.get(i) * x.get(i);    y2t += y.get(i) * y.get(i);
            xyt += x.get(i) * y.get(i);
        }
        xh = xt/n;
        yh = yt/n;
        xsd=x2t/n-xh*xh;
        ysd=y2t/n-yh*yh;
        xs = Math.sqrt(xsd);
        ys = Math.sqrt(ysd);
        r=(xyt/n-xh*yh)/(xs*ys);
        if( Math.abs(r) <=0) Correlation_description="相関なし";
        else if( Math.abs(r) <0.2) Correlation_description="ほとんど相関なし";
        else if( Math.abs(r) <0.4) Correlation_description="弱い相関あり";
        else if( Math.abs(r) <0.7) Correlation_description="やや相関あり";
        else Correlation_description="かなり強い相関がある";
        return r;
    }

}
