package com.example.yoshiki.wakeup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
    get a nexturl from URl.
     */


    public static boolean netWorkCheck(Context context){
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info != null ){
            return info.isConnected();
        } else {
            return false;
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
