package com.newindiaawards;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.widget.LikeView;
import com.inthecheesefactory.lib.fblike.widget.FBLikeView;
import com.newindiaawards.application.AppClass;

/**
 * Created by Mahesh on 02/12/17.
 */

public class FacebookPageLikeActivity extends AppCompatActivity {

    FBLikeView fbLikeView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.layout_fb);
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
                "https://www.facebook.com/Y4DTeam/",
                LikeView.ObjectType.PAGE);





    }


    private class MyWebViewClient extends WebViewClient {

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }
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
            gotonext();
            e.printStackTrace();
        }


    }

    private void gotonext(){

        if(getIntent().hasExtra("id")){

            finish();
            Intent intent1 = new Intent(FacebookPageLikeActivity.this, FacebookPageNavBaharatLikeActivity.class);
            intent1.putExtra("id",getIntent().getStringExtra("id"));
            startActivity(intent1);

        }else{

            finish();
            Intent intent1 = new Intent(FacebookPageLikeActivity.this, FacebookPageNavBaharatLikeActivity.class);
            startActivity(intent1);
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
}
