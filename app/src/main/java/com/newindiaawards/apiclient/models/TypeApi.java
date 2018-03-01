package com.newindiaawards.apiclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahesh on 02/12/17.
 */

public class TypeApi {

    @SerializedName("status")
    @Expose
    public Integer status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("type")
    @Expose
    public String type;

    public TypeApi(String user_id,Integer status,String message,String type){

        this.status=status;
        this.user_id=user_id;
        this.message=message;
        this.type=type;
    }

}
