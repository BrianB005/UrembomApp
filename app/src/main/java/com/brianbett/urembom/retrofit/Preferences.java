package com.brianbett.urembom.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static void saveItemToSP(Context context, String key, String value){
        SharedPreferences sharedPreferences= context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString(key,value);
        editor.apply();
    }
    public static String getItemFromSP(Context context,String key){
        SharedPreferences sharedPreferences= context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);

        return sharedPreferences.getString(key,"");
    }
    public static void deleteItemFromSP(Context context,String key){
        SharedPreferences sharedPreferences= context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).apply();
    }


}
