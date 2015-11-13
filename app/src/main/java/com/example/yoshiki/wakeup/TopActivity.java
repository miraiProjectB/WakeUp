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
import android.util.Log;

import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yoshiki on 2015/10/25.
 */
public class TopActivity extends Activity implements Runnable{

    private static final String TAG = TopActivity.class.getSimpleName();
    private static ProgressDialog waitDialog;
    private String mClientSecret;
    private String mAccessToken;

    public static ArrayList move;
    public static String filePath = Environment.getExternalStorageDirectory() + "/wakeup/log.csv";
    public static int latestDay;
    public static ArrayList<String[]> moveLists, sleepLists;
    public static boolean flag_s,flag_m;
    public long start1;

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


        /*
        TODO Topで表示する物
         */

    }




    private void syncProcess(){
        Calendar today = Calendar.getInstance();
        latestDay = readLatestDate();
        /*
        ファイルに今日の分がなければ更新
        */

        if(GetInformation.netWorkCheck(this.getApplicationContext())) {
            if (latestDay < Integer.parseInt(dateConvertToString(today)) && mClientSecret == null) {
                Intent intentSync = new Intent(TopActivity.this, HelloUpActivity.class);
                startActivity(intentSync);
                finish();
            } else if (mClientSecret != null) {
                start1 = System.nanoTime();
                flag_m = true;
                flag_s  = true;
                moveLists = new ArrayList<>();
                sleepLists = new ArrayList<>();
                waitProcess();
            }
        }else{
            dialog();
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

    public static void nextAccessMove(String url){
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
           //finishCheck();
        }
    }


    public static void nextAccessSleep(String url){
        if(!url.equals("")) {

            Log.d(TAG,"nextAccessS");
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
            finishCheck();
        }
    }

    public static void finishCheck(){
        if(!flag_m && !flag_s){
            fileWrite(createFileString(moveLists, sleepLists));
            waitDialog.dismiss();
            waitDialog = null;
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
                                        long end = System.nanoTime();
                                        System.out.println("Time:" + (end - start1) / 1000000f + "ms");
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

    public static String dateConvertToString(Calendar ct) {
        return String.valueOf(ct.get(Calendar.YEAR)) +
                String.format("%02d", ct.get(Calendar.MONTH) + 1) +
                String.format("%02d", ct.get(Calendar.DAY_OF_MONTH));
    }


    public static void getMoves(Object o) {

        String nexturl = new String();

        JSONObject jsonMove = GetInformation.jsonConvert(o);
        try {
            JSONArray items = jsonMove.getJSONObject("data").getJSONArray("items");
            nexturl = jsonMove.getJSONObject("data").getJSONObject("links").getString("next");
            for (int i = 0; i < items.length(); i++) {
                String moveInfo[] = new String[7];
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
                moveInfo[3] = String.valueOf(details.getInt("wo_calories"));
                moveInfo[4] = String.valueOf(details.getInt("steps"));
                moveInfo[5] = String.valueOf(details.getInt("calories"));
                moveInfo[6] = String.valueOf(details.getInt("bmr_day"));
                moveLists.add(moveInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,nexturl);
        if(flag_m){
            nextAccessMove(nexturl);
        }
        finishCheck();
    }

    /*
    Get Sleep details
     */
    public static void getSleeps(Object o) {

        Log.d(TAG,"getS");
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
                //年、月、日に分割
                GregorianCalendar ct = new GregorianCalendar(Integer.parseInt(st.substring(0, 4)),
                        Integer.parseInt(st.substring(4, 6)) - 1,
                        Integer.parseInt(st.substring(6, 8)));
                //一日戻す
                ct.add(Calendar.DAY_OF_MONTH, -1);

                sleepInfo[0] = dateConvertToString(ct);
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




    public int readLatestDate() {
        int latestDay = 1;
        String lineBuffer;
        try {
            FileInputStream in = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        lineBuffer = reader.readLine();
        if (lineBuffer != null) {
            String[] splitData = lineBuffer.split(",", 0);
            latestDay = Integer.parseInt(splitData[0]);
        }
    } catch (IOException e) {
        e.printStackTrace();
        latestDay = -1;
        return latestDay;
    }
        return latestDay;
    }

    public static String fileAllDate() {
        StringBuilder allDataBuilder= new StringBuilder("");
        try {
            FileInputStream in = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String str;
            while((str = reader.readLine()) != null){
                allDataBuilder.append(str).append("\n");
            }
            in.close();
        }catch(IOException e) {
            e.printStackTrace();
        }

        String allData = allDataBuilder.toString();
        return allData;
    }

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
        String result = saveString.toString();
        return result;
    }
    public static void fileWrite(String result){
        String s1 = fileAllDate();
        File file = new File(filePath);
        OutputStream out;
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        try {
            //out = openFileOutput("log.csv",  MODE_PRIVATE | MODE_WORLD_READABLE |  MODE_WORLD_WRITEABLE );
            //PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
            OutputStreamWriter writer2 =
                    new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8");
            //追記する
            StringBuilder buf = new StringBuilder();
            buf.append(result);
            buf.append(s1);
            String prevData = buf.toString();

            BufferedWriter bw = new BufferedWriter(writer2);
            bw.write(prevData);

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void  onResume(){
        super.onResume();
        Log.d(TAG, "Resume");

    }
    @Override
    protected  void onPause(){
        super.onPause();
        Log.d(TAG, "Pause");

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "Destroy");
    }
}
