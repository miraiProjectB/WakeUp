package com.example.yoshiki.wakeup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yoshiki on 2015/10/25.
 */
public class TopActivity extends Activity implements Runnable {
    private static ProgressDialog waitDialog;
    private String mClientSecret;
    private String mAccessToken;

    public static String filePath = Environment.getExternalStorageDirectory() + "/wakeup/log.csv";
    public static int latestDay;
    public static int yesterday;
    public static ArrayList<String[]> moveLists, sleepLists;
    public static boolean flag_s, flag_m;
    public static boolean evalu_check = false;

    public void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.activity_top);

        Intent intent = getIntent();
        if (intent != null) {
            mClientSecret = intent.getStringExtra(UpPlatformSdkConstants.CLIENT_SECRET);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);
        if (mAccessToken != null) {
            ApiManager.getRequestInterceptor().setAccessToken(mAccessToken);
        }
        syncProcess();
    }

    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!evalu_check) {
                    Intent intent = new Intent(this, ScatterActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent_evalu =new Intent(this,EvaluationActivity.class);
                    startActivity(intent_evalu);
                    evalu_check=false;
                }

/*

                //デモ用。常に評価画面に遷移
                Intent intent_evalu =new Intent(this,EvaluationActivity.class);
                startActivity(intent_evalu);
                evalu_check=false;
                break;
*/
        }
        finish();
        return true;
    }

    private void syncProcess() {
        String today_st = dateConvertToString(Calendar.getInstance());
        yesterday = Integer.parseInt(dateConvertToString(backOneday(today_st)));
        latestDay = Process.readLatestDate();
        /*
        ファイルに今日の分がなければ更新
        */
        if (latestDay < yesterday && mClientSecret == null) {
            if (GetInformation.netWorkCheck(this.getApplicationContext())) {
                Intent intentSync = new Intent(TopActivity.this, HelloUpActivity.class);
                startActivity(intentSync);
                finish();
            }  else{
                dialog();
            }
        } else if (mClientSecret != null) {
            if (GetInformation.netWorkCheck(this.getApplicationContext())) {
                flag_m = true;
                flag_s = true;
                moveLists = new ArrayList<>();
                sleepLists = new ArrayList<>();
                waitProcess();
            }  else {
                dialog();
            }
        }
    }

    private void waitProcess() {
        // プログレスダイアログを開く処理を呼び出す。
        setWait();
    }

    private void setWait() {
        // プログレスダイアログの設定
        waitDialog = new ProgressDialog(this);
        // プログレスダイアログのメッセージを設定します
        waitDialog.setMessage("同期中...");
        // 円スタイル（くるくる回るタイプ）に設定します
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //外部タッチしても消えない
        waitDialog.setCanceledOnTouchOutside(false);
        // プログレスダイアログを表示
        waitDialog.show();

        Thread thread = new Thread(this);
        /* show()メソッドでプログレスダイアログを表示しつつ、
        * * 別スレッドを使い、裏で重い処理を行う。
        * */
        thread.start();
    }
    @Override
    public void run() {
        try {
            //ダイアログがしっかり見えるように少しだけスリープ
            // （nnn：任意のスリープ時間・ミリ秒単位）
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            //スレッドの割り込み処理を行った場合に発生、catchの実装は割愛
            e.printStackTrace();
        }
        //run内でUIの操作をしてしまうと、例外が発生する為、
        // Handlerにバトンタッチ
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            // HandlerクラスではActivityを継承してないため
            // 別の親クラスのメソッドにて処理を行うようにした。
            backProcess();
            // プログレスダイアログ終了
        }
    };

    private void backProcess() {
        // 処理待ち中に行う処理をここに実装
        apiCall();
    }

    public void nextAccessMove(String url){
        if(!url.equals("")) {
            ApiManager.getRestApiInterface().getMoveEventsList(
                    UpPlatformSdkConstants.API_VERSION_STRING,
                    GetInformation.setQueryMap(url),
                    new Callback<Object>() {
                        @Override
                        public void success(Object o, Response response) {
                            getMoves(o);
                        }
                        @Override
                        public void failure(RetrofitError error) {
                        }
                    }
            );
        }else{
            flag_m=false;
        }
    }


    public void nextAccessSleep(String url){
        if(!url.equals("")) {
            ApiManager.getRestApiInterface().getSleepEventsList(
                    UpPlatformSdkConstants.API_VERSION_STRING,
                    GetInformation.setQueryMap(url),
                    new Callback<Object>() {
                        @Override
                        public void success(Object o, Response response) {
                            getSleeps(o);
                        }
                        @Override
                        public void failure(RetrofitError error) {
                        }
                    }
            );
        }else{
            flag_s = false;
        }
    }

    public void finishCheck(){
        if(!flag_m && !flag_s){
            Process.fileWrite(createFileStringT(moveLists, sleepLists));
            waitDialog.dismiss();
            waitDialog = null;
            latestDay = Process.readLatestDate();
            if(latestDay == yesterday){
                evalu_check = true;
            }
        }
    }

    private void apiCall() {
        //活動量取得
        ApiManager.getRestApiInterface().getMoveEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                GetInformation.getMoveEventsListRequestParams(),
                new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        getMoves(o);
                        //睡眠状態取得
                        ApiManager.getRestApiInterface().getSleepEventsList(
                                UpPlatformSdkConstants.API_VERSION_STRING,
                                GetInformation.getMoveEventsListRequestParams(),
                                new Callback<Object>() {
                                    @Override
                                    public void success(Object o, Response response) {
                                        getSleeps(o);
                                    }
                                    @Override
                                    public void failure(RetrofitError retrofitError) {
                                        waitDialog.dismiss();
                                    }
                                }
                        );
                    }
                    @Override
                    public void failure(RetrofitError retrofitError) {
                        waitDialog.dismiss();
                    }
                }
        );
    }

    private void dialog() {
        new AlertDialog
                .Builder(TopActivity.this)
                .setTitle("ネットワークに接続されてません")
                .setPositiveButton("リトライ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                syncProcess();
                            }
                        })
                .setNegativeButton("スキップ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                            }
                        })
                .show();
    }

    /*
    Date型→String型
     */
    public static String dateConvertToString(Calendar ct) {
        return String.valueOf(ct.get(Calendar.YEAR)) +
                String.format("%02d", ct.get(Calendar.MONTH) + 1) +
                String.format("%02d", ct.get(Calendar.DAY_OF_MONTH));
    }
    /*
    String型　→　Date型
    一日戻す
     */
    public static GregorianCalendar backOneday(String st) {
        GregorianCalendar ct_gregor = new GregorianCalendar(Integer.parseInt(st.substring(0, 4)),
                Integer.parseInt(st.substring(4, 6)) - 1,
                Integer.parseInt(st.substring(6, 8)));
        //一日戻す
        ct_gregor.add(Calendar.DAY_OF_MONTH, -1);
        return ct_gregor;
    }

    /*
    Get move data
     */
    public void getMoves(Object o) {
        String nexturl = new String();
        JSONObject jsonMove = GetInformation.jsonConvert(o);
        try {
            JSONArray items = jsonMove.getJSONObject("data").getJSONArray("items");
            nexturl = jsonMove.getJSONObject("data").getJSONObject("links").getString("next");
            for (int i = 0; i < items.length(); i++) {
                String moveInfo[] = new String[9];
                JSONObject item = items.getJSONObject(i);
                JSONObject details = item.getJSONObject("details");
                int date = item.getInt("date");
                if (date <= latestDay) {
                    flag_m =false;
                    break;
                }
                moveInfo[0] = String.valueOf(date);
                moveInfo[1] = String.valueOf(details.getInt("active_time"));
                moveInfo[2] = String.valueOf(details.getInt("bg_calories"));
                moveInfo[3] = String.valueOf(details.getInt("wo_active_time"));
                moveInfo[4] = String.valueOf(details.getInt("wo_calories"));
                moveInfo[5] = String.valueOf(details.getInt("steps"));
                moveInfo[6] = String.valueOf(details.getInt("calories"));
                moveInfo[7] = String.valueOf(details.getInt("bmr_day"));
                moveInfo[8] = String.valueOf(details.getInt("bmr_day") + details.getInt("calories"));
                moveLists.add(moveInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(flag_m){
            nextAccessMove(nexturl);
        }
        finishCheck();
    }

    /*
    Get Sleep details
     */
    public void getSleeps(Object o) {
        JSONObject jsonSleep = GetInformation.jsonConvert(o);
        String nexturl = new String();
        try {
            JSONArray items = jsonSleep.getJSONObject("data").getJSONArray("items");
            nexturl = jsonSleep.getJSONObject("data").getJSONObject("links").getString("next");
            for (int i = 0; i < items.length(); i++) {
                String sleepInfo[] = new String[10];
                JSONObject item = items.getJSONObject(i);
                int date = item.getInt("date");
                if (date <= latestDay) {
                    flag_s  = false;
                    break;
                }
                JSONObject details = item.getJSONObject("details");
                //日付をStringに変換
                String st = String.valueOf(date);
                //年、月、日に分割,カレンダー型に変換

                sleepInfo[0] = dateConvertToString(backOneday(st));
                sleepInfo[1] = String.valueOf(item.getInt("time_completed"));
                sleepInfo[2] = String.valueOf(item.getInt("time_created"));
                sleepInfo[3] = String.valueOf(details.getInt("awakenings"));
                sleepInfo[4] = String.valueOf(details.getInt("light"));
                sleepInfo[5] = String.valueOf(details.getInt("asleep_time"));
                sleepInfo[6] = String.valueOf(details.getInt("awake"));
                sleepInfo[7] = String.valueOf(details.getInt("rem"));
                sleepInfo[8] = String.valueOf(details.getInt("duration"));
                sleepInfo[9] = String.valueOf(details.getInt("awake_time"));
                sleepLists.add(sleepInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (flag_s) {
            nextAccessSleep(nexturl);
        }
        finishCheck();
    }


    /*
    同期されているデータの最新日時の取得
     */



/*
活動量と睡眠の日付から対応付け
 */
    public static String createFileString(ArrayList<String[]> moveList,ArrayList<String[]> sleepList){
        StringBuilder saveString = new StringBuilder();
        for(int i = 0; i < moveList.size(); i++){
            String[] moveInfo = moveList.get(i);
            for(int j = 0; j < sleepList.size(); j++) {
                String[] sleepInfo = sleepList.get(j);
                if (moveInfo[0].equals(sleepInfo[0])){
                    for(String str:moveInfo) {
                        saveString.append(str).append(",");
                    }
                    for(int k = 1; k < sleepInfo.length; k++) {
                        if (k == sleepInfo.length - 1 ) {
                            saveString.append(sleepInfo[k]).append("\n");
                        }else{
                            saveString.append(sleepInfo[k]).append(",");
                        }
                    }
                }
            }
        }
        return saveString.toString();
    }

    /*
    昼寝などでダブりがある日は除外
     */
    public static String createFileStringT(ArrayList<String[]> moveList,ArrayList<String[]> sleepList){
        StringBuilder saveString = new StringBuilder();
        String a[]={"null"};
        for(int i = 0; i < moveList.size(); i++) {
            String[] moveInfo = moveList.get(i);
            for (int j = 0; j < sleepList.size(); j++) {
                ArrayList<String[]> temp = new ArrayList<>();
                String[] sleepInfo = sleepList.get(j);
                for (int p = 0; p < sleepList.size() - j; p++) {
                    if (moveInfo[0].equals(sleepList.get(j + p)[0])) {
                        temp.add((sleepList.get(j + p)));
                        sleepList.set(j+p,a);
                    } else {
                        break;
                    }
                }

                if (temp.size() == 1) {
                    for (String str : moveInfo) {
                        saveString.append(str).append(",");
                    }
                    for (int k = 1; k < sleepInfo.length; k++) {
                        if (k == sleepInfo.length - 1) {
                            saveString.append(sleepInfo[k]);
                        } else {
                            saveString.append(sleepInfo[k]).append(",");
                        }
                    }
                    if(yesterday != Integer.parseInt(moveInfo[0])){
                        saveString.append(",,,,,,,,,\n");
                    }else{
                        saveString.append("\n");
                    }
                }
            }
        }
        return saveString.toString();
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
    protected void onDestroy(){
        super.onDestroy();
    }
}
