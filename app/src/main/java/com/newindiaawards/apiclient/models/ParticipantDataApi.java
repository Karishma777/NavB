package com.newindiaawards.apiclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 05/12/17.
 */

public class ParticipantDataApi {

    @SerializedName("status")
    @Expose
    public Integer status;


    @SerializedName("error")
    @Expose
    public String error;


    @SerializedName("participant_data")
    @Expose
    public ArrayList<ParticipantData> data;

    public class ParticipantData{

        @SerializedName("video_link")
        @Expose
        public String video_link;

        @SerializedName("award_image")
        @Expose
        public String award_image;

    }
}
