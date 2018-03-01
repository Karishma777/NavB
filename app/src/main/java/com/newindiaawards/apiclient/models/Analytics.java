package com.newindiaawards.apiclient.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahesh on 22/11/17.
 */

public class Analytics {

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("analytics_type")
    public String type;

    public Analytics(String user_id,String type){

        this.user_id=user_id;
        this.type=type;
    }


}
