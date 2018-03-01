package com.newindiaawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.inthecheesefactory.lib.fblike.widget.FBLikeView;
import com.newindiaawards.R;
import com.newindiaawards.apiclient.APIClient;
import com.newindiaawards.apiclient.APIInterface;
import com.newindiaawards.apiclient.AnalyticsAPI;
import com.newindiaawards.apiclient.models.NomineesApiModel;
import com.newindiaawards.apiclient.models.User;
import com.newindiaawards.application.AppClass;
import com.newindiaawards.model.NomineesModel;
import com.newindiaawards.utility.MyUtility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 27/10/17.
 */

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    Button fb;
    LoginButton loginButton;
    private static final int REQ_CODE = 9001;
    private CallbackManager callbackManager;
    private ProgressBar loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        ImageView imgsplash=(ImageView)findViewById(R.id.imgsplash);
        Picasso.with(this).load(R.drawable.splash_image).into(imgsplash);

        setUpViews();

    }

    private void setUpViews() {


        loader=(ProgressBar)findViewById(R.id.load);
        loader.setVisibility(View.GONE);

        fb=(Button)findViewById(R.id.fb);

        fb.setOnClickListener(this);

        loginButton = (LoginButton) findViewById(R.id.login_button);

        facebook();

        TextView textView=(TextView)findViewById(R.id.tvTerms);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    private void facebook() {

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse response) {

                                try {

                                    String name="";
                                    String email="test@facebook.com";
                                    String image="";


                                    if(jsonObject.has("name")){
                                        name=jsonObject.getString("name");
                                    }

                                    if(jsonObject.has("email")){
                                        email=jsonObject.getString("email");
                                    }

                                    if(jsonObject.has("id")){

                                        image = "http://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=large";

                                    }



                                  //LoginManager.getInstance().logOut();

                                    gotToHome(name,email,jsonObject.getString("id"),"Facebook",image);


                                } catch (JSONException e) {
                                    loader.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                loader.setVisibility(View.GONE);

            }

            @Override
            public void onError(FacebookException exception) {

                loader.setVisibility(View.GONE);

                Toast.makeText(LoginActivity.this, "error to Login Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FBLikeView.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {



            case R.id.fb:
                loader.setVisibility(View.VISIBLE);
                loginButton.performClick();

                break;

        }

    }


    private void gotToHome(final String name, String email, String openId, final String loggedBy, final String image) {

       //  Log.e("image",image);

        if(!MyUtility.isConnected(this)){
            MyUtility.showAlertInternet(this);
            loader.setVisibility(View.GONE);
            return;
         }

           APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

            Call<User> call3 = apiInterface.createUser(name,email,openId);
            call3.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {



                    switch (response.code()){

                        case 200:

                            AppClass.setUserLogged(true);


                            if(response.body()!=null) {

                                Log.e("Data:", new Gson().toJson(response.body()));


                                if(getIntent().hasExtra("id")){

                                    finish();
                                    Intent i=new Intent(LoginActivity.this,FacebookPageLikeActivity.class);
                                    i.putExtra("id",getIntent().getStringExtra("id"));
                                    startActivity(i);

                                }else{

                                    finish();
                                    Intent i=new Intent(LoginActivity.this,FacebookPageLikeActivity.class);
                                    startActivity(i);
                                }

                               /* User model = response.body();

                                if (model.statusString.equalsIgnoreCase("1")) {


                                    AppClass.setSharedPreferences(model.user_array.id, name, model.user_array.email, image);
                                    String array = new Gson().toJson(model.user_array);
                                    AppClass.setParticipantData(array);

                                    loader.setVisibility(View.GONE);

                                    if(getIntent().hasExtra("id")){

                                        finish();
                                        Intent i=new Intent(LoginActivity.this,FacebookPageLikeActivity.class);
                                        i.putExtra("id",getIntent().getStringExtra("id"));
                                        startActivity(i);

                                    }else{

                                        finish();
                                        Intent i=new Intent(LoginActivity.this,FacebookPageLikeActivity.class);
                                        startActivity(i);
                                    }

                                } else {

                                    MyUtility.showAlertMessage(LoginActivity.this, MyUtility.FAILED_TO_GET_DATA);
                                }

                                */

                            }

                            break;

                        case 500 :

                            MyUtility.showAlertMessage(LoginActivity.this,MyUtility.ERROR_500);


                            break;


                        case 401:

                            MyUtility.showAlertMessage(LoginActivity.this,MyUtility.ERROR_401);

                            break;

                        case 404:

                            MyUtility.showAlertMessage(LoginActivity.this,MyUtility.ERROR_404);

                            break;
                    }




                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                }
            });


    }


}
