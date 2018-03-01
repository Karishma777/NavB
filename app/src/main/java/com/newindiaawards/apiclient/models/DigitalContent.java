package com.newindiaawards.apiclient.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anupamchugh on 09/01/17.
 */

public class DigitalContent {


    @SerializedName("advertise_name")
    public String advertise_name;
    @SerializedName("advertise_img")
    public String advertise_img;
    @SerializedName("advertise_video")
    public String advertise_video;
    @SerializedName("discover_more_url")
    public String discover_more_url;

    public DigitalContent(String name, String img,String video,String discover_more_url) {
        this.advertise_name = name;
        this.advertise_img = img;
        this.advertise_video=video;
        this.discover_more_url=discover_more_url;
    }

}
