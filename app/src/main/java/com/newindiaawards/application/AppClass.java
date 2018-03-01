package com.newindiaawards.application;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import com.newindiaawards.apiclient.models.DigitalContent;
import com.newindiaawards.utility.FontsOverride;
import com.newindiaawards.utility.Preferences;


/**
 * Created by Mahesh on 27/11/15.
 */
public class AppClass extends Application {


    private static SharedPreferences sharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences=getSharedPreferences(Preferences.MyPREFERENCES, Context.MODE_PRIVATE);
        FontsOverride.setDefaultFont(this, "DEFAULT", "Exo2-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Exo2-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "Exo2-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "Exo2-Regular.ttf");
    }

    public static void setSharedPreferences(String userId,String name,String email,String imageUrl){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Preferences.Email,email);
        editor.putString(Preferences.userId,userId);
        editor.putString(Preferences.Name,name);
        editor.putString(Preferences.userImage,imageUrl);
        editor.commit();
    }

    public static String getUserId(){
        return sharedPreferences.getString(Preferences.userId,"");
    }

    public static String getUserEmail(){
        return sharedPreferences.getString(Preferences.Email,"");
    }

    public static String getUserName(){
        return sharedPreferences.getString(Preferences.Name,"");
    }

    public static String getUserImage(){
        return sharedPreferences.getString(Preferences.userImage,"");
    }

    public static void cleanSharedPreferences(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void setInstructionShow(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(Preferences.IS_INSTR_SHOW,true);
        editor.commit();
    }


    public static void setBrandingImage(String image){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("brand_img",image);
        editor.commit();
    }

    public static String getBrandingImage(){
        return sharedPreferences.getString("brand_img","");
    }


    public static void setReward(String image){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("reward",image);
        editor.commit();
    }

    public static String getReward(){
        return sharedPreferences.getString("reward","");
    }


    public static boolean getInstructionShow(){

        return sharedPreferences.getBoolean(Preferences.IS_INSTR_SHOW,false);

    }

    public static void setDigitalContent(DigitalContent digitalContent){

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Preferences.CONTENT_NAME,digitalContent.advertise_name);
        editor.putString(Preferences.CONTENT_IMAGE_URL,digitalContent.advertise_img);
        editor.putString(Preferences.CONTENT_VIDEO_URL,digitalContent.advertise_video);
        editor.putString(Preferences.CONTENT_DICOVER_MORE_URL,digitalContent.discover_more_url);
        editor.commit();
    }

    public static DigitalContent getDigitalContent(){

        return new DigitalContent(sharedPreferences.getString(Preferences.CONTENT_NAME,""),
                sharedPreferences.getString(Preferences.CONTENT_IMAGE_URL,""),
                sharedPreferences.getString(Preferences.CONTENT_VIDEO_URL,""),
                sharedPreferences.getString(Preferences.CONTENT_DICOVER_MORE_URL,""));

    }

    public static void setLikeY4D(boolean liked){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("likey4d",liked);
        editor.commit();
    }

    public static boolean getLikeY4D(){
        return sharedPreferences.getBoolean("likey4d",false);
    }


    public static void setUserLogged(boolean liked){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("logged",liked);
        editor.commit();
    }

    public static boolean isUserLogged(){
        return sharedPreferences.getBoolean("logged",false);
    }

    public static void setParticipantData(String data){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("p_data",data);
        editor.commit();
    }

    public static String getParticipantData(){
        return sharedPreferences.getString("p_data","");
    }

}
