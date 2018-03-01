package com.newindiaawards.apiclient;

import com.google.gson.JsonObject;
import com.newindiaawards.apiclient.models.Analytics;
import com.newindiaawards.apiclient.models.DigitalContent;
import com.newindiaawards.apiclient.models.Category;
import com.newindiaawards.apiclient.models.NomineesApiModel;
import com.newindiaawards.apiclient.models.ParticipantDataApi;
import com.newindiaawards.apiclient.models.TypeApi;
import com.newindiaawards.apiclient.models.User;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public  interface APIInterface {


    @POST("facebook_login")
    @FormUrlEncoded
    Call<User> createUser(@Field("name") String name,@Field("email") String email,@Field("facebook_id") String facebook_id);

    @GET("get_category_wise_participant_list?")
    Call<NomineesApiModel> get_category_wise_participant_list(@Query("category_id") String id,@Query("start") String start,@Query("limit")String limit);


    @GET("current_leading_nominees?")
    Call<NomineesApiModel> current_leading_nominees(@Query("category_id") String id);


    @GET("get_category_list?")
    Call<Category> getCategories();

    @POST("user_analytics")
    Call<Analytics> addAnalytics(@Body Analytics analytics);


    @POST("add_user_type")
    Call<TypeApi> addUserType(@Body TypeApi typeApi);

    @Multipart
    @POST("participant_info")
    Call<JsonObject> participant_info(@Part("user_id") RequestBody user_id,
                                      @Part("category_id") RequestBody category_id,
                                      @Part("first_name") RequestBody first_name,
                                      @Part("last_name") RequestBody last_name,
                                      @Part("contact") RequestBody contact,
                                      @Part("about_nominee") RequestBody about_nominee,
                                      @Part("email") RequestBody email,
                                      @Part("facebook") RequestBody facebook,
                                      @Part("twitter") RequestBody twitter,
                                      @Part("google") RequestBody google,
                                      @Part MultipartBody.Part file);

    @Multipart
    @POST("add_photos_videos_awardpitch")
    Call<JsonObject> participant_media(@Part("user_id") RequestBody user_id,
                                      @Part("about_award_pitch") RequestBody about_award_pitch,
                                      @Part("video_link[]") RequestBody video_link,
                                      @Part MultipartBody.Part file, @Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3, @Part MultipartBody.Part file4);



    @GET("get_nominees_media?")
    Call<ParticipantDataApi> get_nominees_media(@Query("user_id") String id);


    @POST("add_vote")
    @FormUrlEncoded
    Call<JsonObject> addVote(@Field("user_id") String user_id,@Field("participant_id") String participant_id);


}
