package com.newindiaawards.apiclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 22/11/17.
 */

public class Category {

    @SerializedName("status")
    @Expose
    public Integer status;

    @SerializedName("category_data")
    @Expose
    public ArrayList<CategoryData> data;


    public class CategoryData{

        @SerializedName("category_id")
        @Expose
        public String id;

        @SerializedName("category_name")
        @Expose
        public String name;

        @SerializedName("category_description")
        @Expose
        public String desc;

        @SerializedName("category_image")
        @Expose
        public String image;

    }

}
