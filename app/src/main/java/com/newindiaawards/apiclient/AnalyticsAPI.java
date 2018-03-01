package com.newindiaawards.apiclient;

import com.newindiaawards.apiclient.models.Analytics;
import com.newindiaawards.application.AppClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 22/11/17.
 */

public class AnalyticsAPI {

    public static String IMAGE_CAPTURED_AT_FIRST_TRY="image_captured_first_try";
    public static String IMAGE_CAPTURED_AT_SECOND_TRY="image_captured_second_try";
    public static String IMAGE_CAPTURED_AT_HIGHER_SPEED="image_captured_higher_speed";
    public static String AT_DISCOVER_MORE="discover_more_url_hit";
    public static String AT_VIDEO_="playing_video";
    public static String AT_SHARE_IMAGE="at_image_share";
    public static String IMAGE_SHARED="image_shared";
    public static String AT_COUPON_SCREEN="at_coupon_screen";
    public static String LOGGED_BY_FACEBOOK="logged_by_facebook";
    public static String LOGGED_BY_GOOGLE="logged_by_google";


    public static void analytics(String type){

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Analytics> call3 = apiInterface.addAnalytics(new Analytics(AppClass.getUserId(),type));
        call3.enqueue(new Callback<Analytics>() {
            @Override
            public void onResponse(Call<Analytics> call, Response<Analytics> response) {

            }

            @Override
            public void onFailure(Call<Analytics> call, Throwable t) {
                call.cancel();
            }
        });

    }
}
