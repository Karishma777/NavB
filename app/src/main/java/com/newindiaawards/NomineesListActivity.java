package com.newindiaawards;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.github.yasevich.endlessrecyclerview.EndlessRecyclerView;
import com.google.gson.Gson;
import com.newindiaawards.adapter.NominessAdapter;
import com.newindiaawards.apiclient.APIClient;
import com.newindiaawards.apiclient.APIInterface;
import com.newindiaawards.apiclient.models.NomineesApiModel;
import com.newindiaawards.model.NomineesModel;
import com.newindiaawards.utility.MyUtility;
import com.newindiaawards.utility.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 02/12/17.
 */

public class NomineesListActivity extends AppCompatActivity implements EndlessRecyclerView.Pager {

    private List<NomineesModel> list=new ArrayList<>();
    private NominessAdapter adapter;


    private List<NomineesModel> listTop;
    private NominessAdapter adapterTop;

    private boolean reachPageEnd,loadItemSuccess;
    EndlessRecyclerView recyclerView;


    private static final int ITEMS_ON_PAGE = 10;
    private static final int TOTAL_PAGES = 1000;
    private static final long DELAY = 1000L;
    private boolean loading = false;
    private final Handler handler = new Handler();

    private int start=0;
    private int limit=10;

    private ProgressBar loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominees_list);
        setUpViews();


    }
    private void setUpViews() {


    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(getIntent().getStringExtra("name"));


        recyclerView=(EndlessRecyclerView)findViewById(R.id.list);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutParams);

        adapter = new NominessAdapter(list,this,"vertical");
        recyclerView.setAdapter(adapter);
        recyclerView.setProgressView(R.layout.view_more_progress);
        recyclerView.setPager(this);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent=new Intent(NomineesListActivity.this,NomineesActivity.class);
                intent.putExtra("item",list.get(position));
                startActivity(intent);
            }
        }));


        loader=(ProgressBar)findViewById(R.id.loader);

        callApiNominees();
        callApi();

    }

    private void callApi() {


        if(!MyUtility.isConnected(this)){
            MyUtility.showAlertInternet(this);
            return;
        }

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<NomineesApiModel> call3 = apiInterface.get_category_wise_participant_list(getIntent().getStringExtra("id"),start+"","10");
        call3.enqueue(new Callback<NomineesApiModel>() {
            @Override
            public void onResponse(Call<NomineesApiModel> call, Response<NomineesApiModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            NomineesApiModel model=response.body();

                            if(model.status == 1){

                                if(model.data.size() >0){


                                    for(int i =0;i<model.data.size();i++){

                                        NomineesModel item=new NomineesModel();
                                        item.setId(model.data.get(i).id);
                                        item.setName(model.data.get(i).name);
                                        item.setLastName(model.data.get(i).last_name);
                                        item.setEmail(model.data.get(i).email);
                                        item.setAboutus(model.data.get(i).about_nominee);
                                        item.setImageUrl(model.data.get(i).profile_image);
                                        item.setFacebook(model.data.get(i).facebook);
                                        item.setGoogle(model.data.get(i).google);
                                        item.setTwiiter(model.data.get(i).twitter);
                                        item.setDescription(model.data.get(i).about_award_pitch);
                                        item.setCategoryName(model.data.get(i).category_name);
                                        item.setLikeCount(model.data.get(i).like_count);
                                        list.add(item);
                                    }

                                    loadItemSuccess=true;
                                    showData();

                                }



                            }else{

                                reachPageEnd=true;
                                loadItemSuccess=false;

                                MyUtility.showAlertMessage(NomineesListActivity.this,model.message);
                            }

                        }else{

                            MyUtility.showAlertMessage(NomineesListActivity.this,MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(NomineesListActivity.this,MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(NomineesListActivity.this,MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(NomineesListActivity.this,MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<NomineesApiModel> call, Throwable t) {
                call.cancel();
                MyUtility.showAlertMessage(NomineesListActivity.this,MyUtility.INTERNET_ERROR);

            }
        });


    }

    private void showData(){
        adapter.notifyDataSetChanged();
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


    private void callApiNominees() {


        if(!MyUtility.isConnected(this)){
            MyUtility.showAlertInternet(this);
            return;
        }

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<NomineesApiModel> call3 = apiInterface.current_leading_nominees(getIntent().getStringExtra("id"));
        call3.enqueue(new Callback<NomineesApiModel>() {
            @Override
            public void onResponse(Call<NomineesApiModel> call, Response<NomineesApiModel> response) {

                loader.setVisibility(View.GONE);
                Log.e("Data:",new Gson().toJson(response.body()));

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            NomineesApiModel model=response.body();

                            if(model.status == 1){

                                if(model.data.size() >0){

                                    list=new ArrayList<>();

                                    for(int i =0;i<model.data.size();i++){

                                        NomineesModel item=new NomineesModel();
                                        item.setId(model.data.get(i).id);
                                        item.setName(model.data.get(i).name);
                                        item.setLastName(model.data.get(i).last_name);
                                        item.setEmail(model.data.get(i).email);
                                        item.setPhone(model.data.get(i).contact);
                                        item.setAboutus(model.data.get(i).about_nominee);
                                        item.setDescription(model.data.get(i).about_award_pitch);
                                        item.setFacebook(model.data.get(i).facebook);
                                        item.setGoogle(model.data.get(i).google);
                                        item.setTwiiter(model.data.get(i).twitter);
                                        item.setLikeCount(model.data.get(i).like_count);
                                        item.setImageUrl(model.data.get(i).profile_image);
                                        list.add(item);
                                    }

                                    showData();

                                }



                            }else{

                                MyUtility.showAlertMessage(NomineesListActivity.this,model.message);
                            }





                        }else{

                            MyUtility.showAlertMessage(NomineesListActivity.this,MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(NomineesListActivity.this,MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(NomineesListActivity.this,MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(NomineesListActivity.this,MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<NomineesApiModel> call, Throwable t) {
                call.cancel();
                loader.setVisibility(View.GONE);
                MyUtility.showAlertMessage(NomineesListActivity.this,MyUtility.INTERNET_ERROR);

            }
        });


    }

    private void showDataTop(){

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.listTop);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(this);
        layoutParams.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutParams);

        adapter = new NominessAdapter(list,this,"horizontal");
        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(NomineesListActivity.this,NomineesActivity.class);
                intent.putExtra("item",list.get(position));
                startActivity(intent);
            }
        }));
    }

    @Override
    public boolean shouldLoad() {
        return !loading && adapter.getItemCount() / ITEMS_ON_PAGE < TOTAL_PAGES;
    }

    @Override
    public void loadNextPage() {
        loading = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(false);
                loading = false;
                start = start + limit;
                callApi();
            }
        }, DELAY);
    }


}
