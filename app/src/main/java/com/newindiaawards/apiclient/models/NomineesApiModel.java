package com.newindiaawards.apiclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 02/12/17.
 */

public class NomineesApiModel {

    @SerializedName("status")
    @Expose
    public Integer status;

    @SerializedName("message")
    @Expose
    public String message;


    @SerializedName("participant_data")
    @Expose
    public ArrayList<ParticipantData> data;


    public class ParticipantData{

        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("first_name")
        @Expose
        public String name;

        @SerializedName("last_name")
        @Expose
        public String last_name;

        @SerializedName("about_nominee")
        @Expose
        public String about_nominee;

        @SerializedName("contact")
        @Expose
        public String contact;

        @SerializedName("email")
        @Expose
        public String email;

        @SerializedName("facebook")
        @Expose
        public String facebook;

        @SerializedName("google")
        @Expose
        public String google;

        @SerializedName("twitter")
        @Expose
        public String twitter;

        @SerializedName("profile_image")
        @Expose
        public String profile_image;

        @SerializedName("like_count")
        @Expose
        public String like_count;

        @SerializedName("about_award_pitch")
        @Expose
        public String about_award_pitch;

        @SerializedName("category_name")
        @Expose
        public String category_name;

    }

}
