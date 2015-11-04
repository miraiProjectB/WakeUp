package com.example.yoshiki.wakeup;

import android.util.Log;

import com.google.gson.Gson;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yoshiki on 2015/10/06.
 */
public class GetInformation {
    private static final String TAG = GetInformation.class.getSimpleName();

    /*
Object -> JSONObject
 */

    public static JSONObject jsonConvert(Object o) {
        JSONObject json= new JSONObject();
        Gson gson = new Gson();
        try {
            json = new JSONObject(gson.toJson(o));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

/*
Get Move details
*/

    public static String getMoves(Object o) {
        //ArrayList<String> movesList = new ArrayList<String>();
        JSONObject jsonMove = jsonConvert(o);
        String date = null,active_time = null,bg_calories = null,wo_calories = null,
                steps = null,calories = null,bmr_day = null;
        try {
            JSONArray items = jsonMove.getJSONObject("data").getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject details = item.getJSONObject("details");
                date = item.getString("date");
                active_time = details.getString("active_time");
                bg_calories = details.getString("bg_calories");
                wo_calories = details.getString("wo_calories");
                steps = details.getString("steps");
                calories = details.getString("calories");
                bmr_day = details.getString("bmr_day");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.print("日付 : "  + date + "\n" +  active_time + "\n" +"非活動時消費カロリー" + bg_calories + "\n" +
                "運動消費カロリー : " +wo_calories + "\n" +"歩数" + steps + "\n" +"総消費カロリー" + calories + "\n" + bmr_day);
        return "日付 : "  + date + "\n" +  active_time + "\n" +"非活動時消費カロリー" + bg_calories + "\n" +
                "運動消費カロリー : " +wo_calories + "\n" +"歩数" + steps + "\n" +"総消費カロリー" + calories + "\n" + bmr_day +"\n";
    }
    /*
    Get Sleep details
     */
    public static String  getSleeps(Object o) {
        JSONObject jsonMove = jsonConvert(o);
        String date = null,duration = null;
        try {
            JSONArray items = jsonMove.getJSONObject("data").getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject details = item.getJSONObject("details");
                date = item.getString("date");
                duration = details.getString("duration");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "日付：" + date + "\n" + "睡眠合計時間：" + duration;
    }
    /*
    Get Sleep Phases
     */
    public static void getSleepPhases(){

    }
    /*
    get a nexturl from URl.
     */
    public static void getNextURL(Object o) {
        String nextUrl = "";
        try {
            nextUrl = jsonConvert(o).getJSONObject("data").getJSONObject("links").getString("next");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(nextUrl != "") {
            ApiManager.getRestApiInterface().getMoveEventsList(
                    UpPlatformSdkConstants.API_VERSION_STRING,
                    setQueryMap(nextUrl),
                    new Callback<Object>() {
                        @Override
                        public void success(Object o, Response response) {
                            getNextURL(o);
                        }
                        @Override
                        public void failure(RetrofitError error) {
                        }
                    }
            );
        }
        Log.d(TAG,nextUrl);
    }

    public static HashMap<String, Integer> setQueryMap(String query)
    {
        String[] params = query.split("&");
        HashMap<String, Integer> map = new HashMap<String,Integer>();

        String[] splitted = params[0].split("=");
        int splitted_token = Integer.parseInt(splitted[1]);
        map.put("page_token", splitted_token);

        return map;
    }
    public static HashMap<String, Integer> getMoveEventsListRequestParams() {
        HashMap<String, Integer> queryHashMap = new HashMap<String, Integer>();

        //uncomment to add as needed parameters
//        queryHashMap.put("date", "<insert-date>");
//        queryHashMap.put("page_token", 1443619654);
//        queryHashMap.put("start_time", "<insert-time>");
//        queryHashMap.put("end_time", "<insert-time>");
//        queryHashMap.put("updated_after", "<insert-time>");

        return queryHashMap;
    }

}
