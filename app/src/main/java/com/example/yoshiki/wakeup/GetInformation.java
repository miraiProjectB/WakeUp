package com.example.yoshiki.wakeup;

import android.util.Log;

import com.google.gson.Gson;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

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

        LinkedHashMap<String ,String[]> moveLists;
        moveLists = new LinkedHashMap<>();
        JSONObject jsonMove = jsonConvert(o);
        try {
            JSONArray items = jsonMove.getJSONObject("data").getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                String moveInfo [] = new String[7];
                JSONObject item = items.getJSONObject(i);
                JSONObject details = item.getJSONObject("details");
                Log.d(TAG, String.valueOf(item.getInt("date")));

                moveInfo[0] = details.getString("active_time");
                moveInfo[1] = details.getString("bg_calories");
                moveInfo[2] = details.getString("wo_calories");
                moveInfo[3] = details.getString("steps");
                moveInfo[4] = details.getString("calories");
                moveInfo[5] = details.getString("bmr_day");
                moveLists.put(String.valueOf(item.getInt("date")),moveInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    Get Sleep details
     */
    public static String  getSleeps(Object o) {
        LinkedHashMap<String ,String[]> sleepLists = new LinkedHashMap<>();
        JSONObject jsonMove = jsonConvert(o);
        try {
            JSONArray items = jsonMove.getJSONObject("data").getJSONArray("items");
            String sleepInfo [] = new String[9];
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject details = item.getJSONObject("details");
                sleepInfo[0] = item.getString("time_completed");
                sleepInfo[1] = item.getString("time_created");
                sleepInfo[2] = details.getString("awakenings");
                sleepInfo[3] = details.getString("light");
                sleepInfo[4] = details.getString("asleep_time");
                sleepInfo[5] = details.getString("awake");
                sleepInfo[6] = details.getString("rem");
                sleepInfo[7] = details.getString("duration");
                sleepInfo[8] = details.getString("awake_time");

                String st = String.valueOf(item.getInt("date"));
              //  int year = Integer.parseInt(st.substring(0,4));
                GregorianCalendar ct =new GregorianCalendar(Integer.parseInt(st.substring(0,4)),
                        Integer.parseInt(st.substring(4, 6))-1,
                        Integer.parseInt(st.substring(6, 8)));
                ct.add(Calendar.DAY_OF_MONTH,-1);

                sleepLists.put(String.valueOf(ct.get(Calendar.YEAR)) +
                        String.valueOf(ct.get(Calendar.MONTH)+1) +
                        String.valueOf(ct.get(Calendar.DAY_OF_MONTH)),
                        sleepInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "null";
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
