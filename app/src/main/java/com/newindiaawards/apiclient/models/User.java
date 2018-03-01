package com.newindiaawards.apiclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anupamchugh on 09/01/17.
 */

public class User {

   // @SerializedName("status")
   // @Expose
   // public Integer status;

    @SerializedName("status")
    @Expose
    public String statusString;


    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("user_array")
    @Expose
    public User_array user_array;


    public class User_array{


        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("first_name")
        @Expose
        public String first_name;

        @SerializedName("email")
        @Expose
        public String email;

        @SerializedName("contact")
        @Expose
        public String contact;

        @SerializedName("about_nominee")
        @Expose
        public String about_nominee;

        @SerializedName("about_award_pitch")
        @Expose
        public String about_award_pitch;

        @SerializedName("type")
        @Expose
        public String type;

    }




}
