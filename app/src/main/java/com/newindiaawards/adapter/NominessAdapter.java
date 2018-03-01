package com.newindiaawards.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.newindiaawards.LoginActivity;
import com.newindiaawards.NomineesActivity;
import com.newindiaawards.R;
import com.newindiaawards.apiclient.APIClient;
import com.newindiaawards.apiclient.APIInterface;
import com.newindiaawards.application.AppClass;
import com.newindiaawards.model.NomineesModel;
import com.newindiaawards.utility.MyUtility;
import com.newindiaawards.utility.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Mahesh on 19/09/17.
 */

public class NominessAdapter extends RecyclerView.Adapter<NominessAdapter.MyViewHolder> {

    private List<NomineesModel> list;
    private Context context;
    private String type;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView categoryName;
        public TextView tvCount;
        public RoundedImageView userImage;


        public MyViewHolder(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.userName);
            categoryName=(TextView) view.findViewById(R.id.categoryName);
            tvCount=(TextView) view.findViewById(R.id.likeCount);
            userImage=(RoundedImageView) view.findViewById(R.id.userImage);
        }
    }

    public NominessAdapter(List<NomineesModel> list, Context context,String type) {
        this.list = list;
        this.context = context;
        this.type=type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(type.equalsIgnoreCase("horizontal")){

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_nominees_horizontal, parent, false);


            return new MyViewHolder(itemView);

        }else{

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_nominees, parent, false);


            return new MyViewHolder(itemView);

        }


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NomineesModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.categoryName.setText(model.getCategoryName());
        holder.tvCount.setText(model.getLikeCount());
        Picasso.with(context).load(APIClient.USER_IMAGE_URL+model.getImageUrl()).resize(80,80)
                .placeholder(R.drawable.placeholder_user)
                .error(R.drawable.placeholder_user)
                .into(holder.userImage);

        holder.tvCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(AppClass.getUserId().length()>0){

                    callVoteApi(model.getId());

                }else{

                    APIClient.id=model.getId();
                    Intent i =new Intent(context, LoginActivity.class);
                    i.putExtra("id",model.getId());
                    context.startActivity(i);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void callVoteApi(String id) {

        if(!MyUtility.isConnected(context)){
            MyUtility.showAlertInternet(context);
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
                                MyUtility.showAlertMessage(context,MyUtility.FAILED_TO_GET_DATA);
                            }

                        }else{

                            MyUtility.showAlertMessage(context,MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(context,MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(context,MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(context,MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                MyUtility.showAlertMessage(context,MyUtility.INTERNET_ERROR);

            }
        });


    }


}