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

    public static Datastring data = new Datastring();

    public void onCreate(Bundle saveInstance){
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
        int a = 0;
        if (a == 1 && mClientSecret == null) {
            Intent intentSync = new Intent(TopActivity.this, HelloUpActivity.class);
            startActivity(intentSync);
            finish();
        }else if( a==1 && mClientSecret != null){
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
    private void apiCall(){
        //活動量取得
        ApiManager.getRestApiInterface().getMoveEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                GetInformation.getMoveEventsListRequestParams(),
                new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        data.setSt(GetInformation.getMoves(o));
                        Log.d(TAG, data.getSt());
                        //睡眠状態取得
                        ApiManager.getRestApiInterface().getSleepEventsList(
                                UpPlatformSdkConstants.API_VERSION_STRING,
                                GetInformation.getMoveEventsListRequestParams(),
                                new Callback<Object>() {
                                    @Override
                                    public void success(Object o, Response response) {
                                        data.setSt(data.getSt() + GetInformation.getSleeps(o));
                                        waitDialog.dismiss();
                                        waitDialog = null;
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
