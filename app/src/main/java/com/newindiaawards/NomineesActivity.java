package com.newindiaawards;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.newindiaawards.apiclient.APIClient;
import com.newindiaawards.apiclient.APIInterface;
import com.newindiaawards.apiclient.models.NomineesApiModel;
import com.newindiaawards.apiclient.models.ParticipantDataApi;
import com.newindiaawards.application.AppClass;
import com.newindiaawards.model.NomineesModel;
import com.newindiaawards.model.ParticipantMediaModel;
import com.newindiaawards.utility.MyUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 02/12/17.
 */

public class NomineesActivity extends AppCompatActivity {

    NomineesModel item;

    List<ParticipantMediaModel> list=new ArrayList<>();

    private LinearLayout llimages;
    private RelativeLayout llvideos;

    private ImageView fb,gp,twiiter;

    private TextView lllinks;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_profile);

        item=(NomineesModel) getIntent().getSerializableExtra("item");

        setupViews();

        Log.e("ITEM",new Gson().toJson(item));
    }

    private void setupViews() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(item.getName().toUpperCase());

        TextView name = (TextView) findViewById(R.id.userName);
        name.setText(item.getName());

        TextView email = (TextView) findViewById(R.id.userEmail);
        email.setText(item.getEmail());

        TextView likeCount = (TextView) findViewById(R.id.likeCount);
        likeCount.setText(item.getLikeCount());

        TextView category = (TextView) findViewById(R.id.userCategory);
        category.setText(item.getCategoryName());


        TextView phone = (TextView) findViewById(R.id.userPhone);
        phone.setText(item.getPhone());

        TextView aboutus=(TextView)findViewById(R.id.aboutData);
        aboutus.setText(item.getAboutus());

        TextView descDatat=(TextView)findViewById(R.id.descDatat);
        descDatat.setText(item.getDescription());


        Button btnVote = (Button) findViewById(R.id.voteme);
        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // confirmDialog();
                if(AppClass.getUserId().length()>0){
                    callVoteApi();
                }else{
                    APIClient.id=item.getId();
                    Intent i =new Intent(NomineesActivity.this, LoginActivity.class);
                    i.putExtra("id",item.getId());
                    startActivity(i);
                }
            }
        });


        ImageView imageView=(ImageView)findViewById(R.id.userImage);
        Picasso.with(this).load(APIClient.USER_IMAGE_URL+item.getImageUrl()).
                resize(80,80)
                .placeholder(R.drawable.placeholder_user)
                .error(R.drawable.placeholder_user)
                .into(imageView);

        llimages=(LinearLayout)findViewById(R.id.llimages);
        llimages.setVisibility(View.GONE);

        llvideos=(RelativeLayout)findViewById(R.id.llvideos);
        llvideos.setVisibility(View.GONE);


        lllinks=(TextView)findViewById(R.id.lllinks);
        lllinks.setVisibility(View.GONE);

        fb=(ImageView)findViewById(R.id.imgfb);
        gp=(ImageView)findViewById(R.id.imggoogle);
        twiiter=(ImageView)findViewById(R.id.imgtwitter);

        if(item.getFacebook().length()>0){

            lllinks.setVisibility(View.VISIBLE);


            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MyUtility.goToUrl(NomineesActivity.this,item.getFacebook());
                }
            });

        }else{

            fb.setVisibility(View.GONE);
        }

        if(item.getGoogle().length()>0){

            lllinks.setVisibility(View.VISIBLE);


            gp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MyUtility.goToUrl(NomineesActivity.this,item.getGoogle());
                }
            });

        }else{

            gp.setVisibility(View.GONE);
        }

        if(item.getTwiiter().length()>0){

            lllinks.setVisibility(View.VISIBLE);


            twiiter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MyUtility.goToUrl(NomineesActivity.this,item.getTwiiter());
                }
            });

        }else{

            twiiter.setVisibility(View.GONE);
        }

        callApi();
    }

    private void callApi() {


        if(!MyUtility.isConnected(this)){
            MyUtility.showAlertInternet(this);
            return;
        }

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ParticipantDataApi> call3 = apiInterface.get_nominees_media(item.getId()+"");
        call3.enqueue(new Callback<ParticipantDataApi>() {
            @Override
            public void onResponse(Call<ParticipantDataApi> call, Response<ParticipantDataApi> response) {

                Log.e("Data:",new Gson().toJson(response.body()));


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ParticipantDataApi model=response.body();

                            if(model.status == 1){

                                if(model.data.size() >0){

                                    for(int i=0;i<model.data.size();i++){

                                        ParticipantMediaModel model1=new ParticipantMediaModel();
                                        model1.setImage(model.data.get(i).award_image);
                                        model1.setVideo(model.data.get(i).video_link);
                                        list.add(model1);

                                    }

                                    bindData();
                                }

                            }else{
                                MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.FAILED_TO_GET_DATA);
                            }

                        }else{


                            MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                      //  MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.ERROR_500);


                        break;


                    case 401:

                       // MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.ERROR_401);

                        break;

                    case 404:

                      //  MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<ParticipantDataApi> call, Throwable t) {
                call.cancel();
                MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.INTERNET_ERROR);

            }
        });


    }

    private void bindData() {

        llimages.setVisibility(View.VISIBLE);


        ImageView  imageView=(ImageView) findViewById(R.id.imge1);
        ImageView  imageView1=(ImageView)findViewById(R.id.image2);
        ImageView imageView2=(ImageView)findViewById(R.id.image3);
        ImageView  imageView3=(ImageView)findViewById(R.id.image4);
        ImageView imageView4=(ImageView)findViewById(R.id.image5);

        for(int i=0;i<list.size();i++){

            if(i==0){

                if(list.get(i).getVideo().length()>0){
                    llvideos.setVisibility(View.VISIBLE);
                }else{
                    llvideos.setVisibility(View.GONE);
                }

                Picasso.with(this)
                        .load(APIClient.AWARD_IMAGE_URL+list.get(i).getImage())
                        .resize(100,100)
                        .centerCrop()
                        .placeholder(R.drawable.icon_upload)
                        .into(imageView);
            }


            if(i==1){

                Picasso.with(this)
                        .load(APIClient.AWARD_IMAGE_URL+list.get(i).getImage())
                        .resize(100,100)
                        .centerCrop()
                        .placeholder(R.drawable.icon_upload)
                        .into(imageView);
            }


            if(i==2){

                Picasso.with(this)
                        .load(APIClient.AWARD_IMAGE_URL+list.get(i).getImage())
                        .resize(100,100)
                        .centerCrop()
                        .placeholder(R.drawable.icon_upload)
                        .into(imageView1);
            }


            if(i==3){

                Picasso.with(this)
                        .load(APIClient.AWARD_IMAGE_URL+list.get(i).getImage())
                        .resize(100,100)
                        .centerCrop()
                        .placeholder(R.drawable.icon_upload)
                        .into(imageView2);
            }


            if(i==4){

                Picasso.with(this)
                        .load(APIClient.AWARD_IMAGE_URL+list.get(i).getImage())
                        .resize(100,100)
                        .centerCrop()
                        .placeholder(R.drawable.icon_upload)
                        .into(imageView3);
            }


            if(i==5){

                Picasso.with(this)
                        .load(APIClient.AWARD_IMAGE_URL+list.get(i).getImage())
                        .resize(100,100)
                        .centerCrop()
                        .placeholder(R.drawable.icon_upload)
                        .into(imageView4);
            }
        }


        TextView videolink=(TextView) findViewById(R.id.tvvideolink);
        videolink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(list.size()>=1){

                    MyUtility.goToUrl(NomineesActivity.this,list.get(0).getVideo());
                }

            }
        });


    }


    private void confirmDialog() {

        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.alert_dialog_custom_title, null);
        TextView title=(TextView)view.findViewById(R.id.alertTitle);
        title.setText("CONFIRM");
        ad.setCustomTitle(view);

        ad.setMessage("Do you want to continue as a Voter? You can't undo this action.");
        ad.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                //
            }
        });

        ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        AlertDialog alert=ad.create();
        alert.show();


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

    private void callVoteApi() {


        if(!MyUtility.isConnected(this)){
            MyUtility.showAlertInternet(this);
            return;
        }

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.addVote(AppClass.getUserId(),item.getId());
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


                                MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.FAILED_TO_GET_DATA);
                            }

                        }else{

                            MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                MyUtility.showAlertMessage(NomineesActivity.this,MyUtility.INTERNET_ERROR);

            }
        });


    }

}
