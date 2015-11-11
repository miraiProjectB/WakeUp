package com.example.yoshiki.wakeup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.datamodel.Datastring;
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
public class TopActivity extends Activity{

    private static final String TAG = TopActivity.class.getSimpleName();
    private static ProgressDialog waitDialog;
    private String mClientSecret;
    private String mAccessToken;

    public static Datastring data = new Datastring();
    public static ArrayList move;

    public long start1;

    public void onCreate(Bundle saveInstance){
        start1 = System.nanoTime();
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

        /*
        TODO ファイルの読み取り(ファイルは最新か？)
         */
        /*
        ファイルに今日の分がなければ更新
        */

        if (mClientSecret == null) {
            Intent intentSync = new Intent(TopActivity.this, HelloUpActivity.class);
            startActivity(intentSync);
            finish();
        }else if(mClientSecret != null){

            Log.d(TAG, "sync");
            waitProcess();
        }else{
            Log.d(TAG,"file exist & not sync");
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

        Thread thread = new Thread(new Runnable() {
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
        });
        /* show()メソッドでプログレスダイアログを表示しつつ、
        * * 別スレッドを使い、裏で重い処理を行う。
        * */
        thread.start();
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
    private void apiCall(){
        //活動量取得
        ApiManager.getRestApiInterface().getMoveEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                GetInformation.getMoveEventsListRequestParams(),
                new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        move = getMoves(o);
                        //睡眠状態取得
                        ApiManager.getRestApiInterface().getSleepEventsList(
                                UpPlatformSdkConstants.API_VERSION_STRING,
                                GetInformation.getMoveEventsListRequestParams(),
                                new Callback<Object>() {
                                    @Override
                                    public void success(Object o, Response response) {
                                        createFileString(move, getSleeps(o));
                                        waitDialog.dismiss();
                                        waitDialog = null;

                                        long end = System.nanoTime();
                                        System.out.println("Time:" + (end - start1) / 1000000f + "ms");
                                    }

                                    @Override
                                    public void failure(RetrofitError retrofitError) {
                                        waitDialog.dismiss();
                                        waitDialog = null;
                                        dialog();
                                    }
                                }
                        );
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        waitDialog.dismiss();
                        waitDialog = null;
                        dialog();

                    }
                }
        );


    }
    private void dialog(){
        new AlertDialog
                .Builder(TopActivity.this)
                .setTitle("同期に失敗しました")
                .setPositiveButton("リトライ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                apiCall();
                            }
                        })
                .setNegativeButton("スキップ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                Intent intent = new Intent(TopActivity.this, TopActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                .show();
    }
    public static ArrayList getMoves(Object o) {

        //LinkedHashMap<String ,String[]> moveLists;
        ArrayList <String[]> moveLists = new ArrayList<>();

        JSONObject jsonMove = GetInformation.jsonConvert(o);
        try {
            JSONArray items = jsonMove.getJSONObject("data").getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                String moveInfo [] = new String[7];
                JSONObject item = items.getJSONObject(i);
                JSONObject details = item.getJSONObject("details");
                int date = item.getInt("date");
               /* if(date <= 20151104){
                    break;
                }
                */
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
        return moveLists;
    }
    /*
    Get Sleep details
     */
    public static ArrayList  getSleeps(Object o) {
        ArrayList <String[]> sleepLists = new ArrayList<>();
        JSONObject jsonMove = GetInformation.jsonConvert(o);
        try {
            JSONArray items = jsonMove.getJSONObject("data").getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                String sleepInfo [] = new String[10];
                JSONObject item = items.getJSONObject(i);
                int date = item.getInt("date");
                /*if(date <= 20151104){
                    break;
                }
                */
                JSONObject details = item.getJSONObject("details");
                sleepInfo[1] = String.valueOf(item.getInt("time_completed"));
                sleepInfo[2] = String.valueOf(item.getInt("time_created"));
                sleepInfo[3] = String.valueOf(details.getInt("awakenings"));
                sleepInfo[4] = String.valueOf(details.getInt("light"));
                sleepInfo[5] = String.valueOf(details.getInt("asleep_time"));
                sleepInfo[6] = String.valueOf(details.getInt("awake"));
                sleepInfo[7] = String.valueOf(details.getInt("rem"));
                sleepInfo[8] = String.valueOf(details.getInt("duration"));
                sleepInfo[9] = String.valueOf(details.getInt("awake_time"));

                //日付をStringに変換
                String st = String.valueOf(date);
                //年、月、日に分割
                GregorianCalendar ct =new GregorianCalendar(Integer.parseInt(st.substring(0,4)),
                        Integer.parseInt(st.substring(4, 6))-1,
                        Integer.parseInt(st.substring(6, 8)));
                //一日戻す
                ct.add(Calendar.DAY_OF_MONTH, -1);

                sleepInfo [0] = String.valueOf(ct.get(Calendar.YEAR)) +
                        String.format("%02d",ct.get(Calendar.MONTH)+1) +
                        String.format("%02d",ct.get(Calendar.DAY_OF_MONTH));
                sleepLists.add(sleepInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sleepLists;
    }

    public static void createFileString(ArrayList<String[]> moveList,ArrayList<String[]> sleepList){
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
        Log.d(TAG, "Destroy");
    }
}
