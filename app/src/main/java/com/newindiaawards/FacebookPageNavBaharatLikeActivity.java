package com.newindiaawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.facebook.FacebookSdk;
import com.facebook.share.widget.LikeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inthecheesefactory.lib.fblike.widget.FBLikeView;
import com.newindiaawards.apiclient.APIClient;
import com.newindiaawards.apiclient.APIInterface;
import com.newindiaawards.application.AppClass;
import com.newindiaawards.utility.MyUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 02/12/17.
 */

public class FacebookPageNavBaharatLikeActivity  extends AppCompatActivity {

    FBLikeView fbLikeView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.layout_fb_page_navbharat);
        setUpViews();

    }

    private void setUpViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("LIKE OUR PAGE");


        fbLikeView = (FBLikeView)findViewById(R.id.fbLikeView);
        fbLikeView.getLikeView().setObjectIdAndType(
                "https://www.facebook.com/enavabharat/",
                LikeView.ObjectType.PAGE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{


            if(data!=null){

                FBLikeView.onActivityResult(requestCode, resultCode, data);
                Bundle bundle1= data.getBundleExtra("com.facebook.platform.protocol.RESULT_ARGS");
                Boolean isLiked=bundle1.getBoolean("object_is_liked");

                if(isLiked){

                    Log.e("UserLiked","UserLiked");
                    AppClass.setLikeY4D(true);


                }else{

                    Log.e("UnUserLiked","UnUserLiked");
                    AppClass.setLikeY4D(false);
                }

                gotonext();

            }else{

                gotonext();
            }

        }catch (Exception e){

            e.printStackTrace();

            gotonext();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotonext(){

        if(getIntent().hasExtra("id")){


            callVoteApi(APIClient.id);


        }else{

            finish();
            Intent intent1 = new Intent(FacebookPageNavBaharatLikeActivity.this, ParticipantDetailForm.class);
            startActivity(intent1);
        }
    }


    private void callVoteApi(String id) {

        if(!MyUtility.isConnected(this)){
            MyUtility.showAlertInternet(this);
            return;
        }

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.addVote(AppClass.getUserId(),id);
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("Data:",new Gson().toJson(response.body()));


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            JsonObject model=response.body();

                            if(model !=null){




                            }else{
                                MyUtility.showAlertMessage(FacebookPageNavBaharatLikeActivity.this,MyUtility.FAILED_TO_GET_DATA);
                            }

                        }else{

                            MyUtility.showAlertMessage(FacebookPageNavBaharatLikeActivity.this,MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(FacebookPageNavBaharatLikeActivity.this,MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(FacebookPageNavBaharatLikeActivity.this,MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(FacebookPageNavBaharatLikeActivity.this,MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                MyUtility.showAlertMessage(FacebookPageNavBaharatLikeActivity.this,MyUtility.INTERNET_ERROR);

            }
        });
    }

}
