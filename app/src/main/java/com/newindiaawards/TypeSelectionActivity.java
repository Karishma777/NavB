package com.newindiaawards;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.newindiaawards.apiclient.APIClient;
import com.newindiaawards.apiclient.APIInterface;
import com.newindiaawards.apiclient.models.TypeApi;
import com.newindiaawards.application.AppClass;
import com.newindiaawards.utility.MyUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 02/12/17.
 */

public class TypeSelectionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        setUpViews();
    }

    private void setUpViews() {

        Button btnVoter=(Button)findViewById(R.id.voter);
        btnVoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                voterDialog();
            }
        });


        Button btnParticipant=(Button)findViewById(R.id.participant);
        btnParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 participantDialog();
            }
        });

    }

    private void participantDialog() {

        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.alert_dialog_custom_title, null);
        TextView title=(TextView)view.findViewById(R.id.alertTitle);
        title.setText("CONFIRM");
        ad.setCustomTitle(view);

        ad.setMessage("Do you want to continue as a Participant? You can't undo this action.");
        ad.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                callApi("1");

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

    private void voterDialog() {

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
                callApi("2");
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

    private void callApi(final String type) {


        if(!MyUtility.isConnected(this)){
            MyUtility.showAlertInternet(this);
            return;
        }

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<TypeApi> call3 = apiInterface.addUserType(new TypeApi(AppClass.getUserId(),0,"",type));
        call3.enqueue(new Callback<TypeApi>() {
            @Override
            public void onResponse(Call<TypeApi> call, Response<TypeApi> response) {

                Log.e("Data:",new Gson().toJson(response.body()));


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            TypeApi model=response.body();

                            if(model.status == 1){

                                if(type.equalsIgnoreCase("1")){

                                    // go to participant



                                }else{

                                    //go to  voter

                                }


                            }
                            else{

                                MyUtility.showAlertMessage(TypeSelectionActivity.this,model.message);
                            }





                        }else{

                            MyUtility.showAlertMessage(TypeSelectionActivity.this,MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(TypeSelectionActivity.this,MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(TypeSelectionActivity.this,MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(TypeSelectionActivity.this,MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<TypeApi> call, Throwable t) {
                call.cancel();
                MyUtility.showAlertMessage(TypeSelectionActivity.this,MyUtility.INTERNET_ERROR);

            }
        });


    }

}
