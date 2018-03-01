package com.newindiaawards;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.newindiaawards.apiclient.APIClient;
import com.newindiaawards.apiclient.APIInterface;
import com.newindiaawards.apiclient.models.TypeApi;
import com.newindiaawards.application.AppClass;
import com.newindiaawards.utility.MyUtility;
import com.newindiaawards.utility.RoundedImageView;
import com.newindiaawards.utility.Utility;
import com.newindiaawards.utility.Validations;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 02/12/17.
 */

public class ParticipantDetailForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    EditText name,lastname, email, phone, brif_details,editfb,edittwitter,editgp;
    RoundedImageView imageView;
    Spinner spinnerstate;
    String category, myname,mylastname, myemail, myphone, mydes, mycat;
    Button button;
    private Uri fileUri;
    private static final int REQUEST_CAMERA = 100;
    private static final int SELECT_FILE = 200;
    private File destination;
    private float totalSize = 0;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_details_form);
        setUpView();
    }

    private void setUpView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("NOMINEE DETAILS");

        name = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        button = (Button) findViewById(R.id.btnSubmit);
        linearLayout = (LinearLayout) findViewById(R.id.parent);
        brif_details = (EditText) findViewById(R.id.brif_details);
        imageView = (RoundedImageView) findViewById(R.id.imageregistrationproof);
        spinnerstate = (Spinner) findViewById(R.id.spinnercate);
        spinnerstate.setOnItemSelectedListener(this);
        imageView.setOnClickListener(this);
        button.setOnClickListener(this);

        editfb = (EditText) findViewById(R.id.fb_url);
        edittwitter = (EditText) findViewById(R.id.twitter_url);
        editgp = (EditText) findViewById(R.id.google_url);


        List<String> categories = new ArrayList<String>();
        categories.add("Ambassadors of New India");
        categories.add("Architech of India");
        categories.add("Backbone of New India");
        categories.add("Change Advocate of New India");
        categories.add("Fortune hunters of New India");
        categories.add("Healers of New India");
        categories.add("Special Achivers of New India");
        categories.add("Spirit of New India");
        categories.add("Torch Bearers of New India");
        categories.add("Trendsetter of New India");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerstate.setAdapter(dataAdapter);


       // if(App)
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = spinnerstate.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imageregistrationproof:
                selectImage();

                break;
            case R.id.btnSubmit:

                MyUtility.hideKeyboard(spinnerstate, this);
                myname = name.getText().toString();
                mylastname = lastname.getText().toString();
                myemail = email.getText().toString();
                myphone = phone.getText().toString();
                mydes = brif_details.getText().toString();

                if (myname.equalsIgnoreCase("")) {

                    MyUtility.showSnack(linearLayout, "Enter Valid First Name");

                } else
                if (mylastname.equalsIgnoreCase("")) {

                    MyUtility.showSnack(linearLayout, "Enter Valid Last Name");

                }else if (myphone.equalsIgnoreCase("")) {


                    MyUtility.showSnack(linearLayout, Validations.ENTER_MOBILE);


                } else if (myphone.length() < 10) {


                    MyUtility.showSnack(linearLayout, Validations.ENTER_VALID_Mobile);


                } else if (myphone.length() > 10) {


                    MyUtility.showSnack(linearLayout, Validations.ENTER_VALID_Mobile);

                } else if (myemail.equalsIgnoreCase("")) {


                    MyUtility.showSnack(linearLayout, Validations.ENTER_EMAIL);

                } else if (!myemail.matches(MyUtility.emailPattern)) {


                    MyUtility.showSnack(linearLayout, Validations.ENTER_VALID_EMAIL);

                } else if (mydes.equalsIgnoreCase("")) {

                    MyUtility.showSnack(linearLayout, "Enter About Yourself");


                } else {

                    if (MyUtility.isConnected(ParticipantDetailForm.this)) {


                        callAPI();

                    } else {

                        MyUtility.showToast(MyUtility.INTERNET_ERROR, ParticipantDetailForm.this);

                    }

                }


                break;
        }

    }

    private void callAPI() {


        if (!MyUtility.isConnected(this)) {
            MyUtility.showAlertInternet(this);
            return;
        }

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Creating Account....");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        MultipartBody.Part filePart=null;

        if(destination!=null){

            filePart = MultipartBody.Part.createFormData("profile_image", destination.getName(), RequestBody.create(MediaType.parse("image/*"), destination));
        }


        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call = apiInterface.participant_info(

                toRequestBody("20"),
                toRequestBody(category),
                toRequestBody(myname),
                toRequestBody(mylastname),
                toRequestBody(myphone),
                toRequestBody(mydes),
                toRequestBody(myemail),
                toRequestBody(editfb.getText().toString()),
                toRequestBody(edittwitter.getText().toString()),
                toRequestBody(editgp.getText().toString()),
                filePart
        );


        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("Data:", new Gson().toJson(response.body()));

                if(dialog.isShowing()){

                    dialog.dismiss();

                }


                switch (response.code()) {

                    case 200:


                            JsonObject jsonObject=response.body();


                            if (jsonObject != null) {

                                JsonArray array=jsonObject.getAsJsonArray("participant_data");

                                if(array.size()!=0){

                                    AppClass.setParticipantData(array.toString());
                                    startActivity(new Intent(ParticipantDetailForm.this,UploadData.class));


                                }else{

                                    MyUtility.showAlertMessage(ParticipantDetailForm.this, MyUtility.FAILED_TO_GET_DATA);

                                }

                                Log.e("Data:", new Gson().toJson(response.body()));

                            } else {

                                MyUtility.showAlertMessage(ParticipantDetailForm.this, jsonObject.get("error").toString());
                            }



                        break;

                    case 500:

                        MyUtility.showAlertMessage(ParticipantDetailForm.this, MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(ParticipantDetailForm.this, MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(ParticipantDetailForm.this, MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                if(dialog.isShowing()) {

                    dialog.dismiss();

                }
                MyUtility.showAlertMessage(ParticipantDetailForm.this, MyUtility.INTERNET_ERROR);

            }
        });


    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value); return body ;
    }


    //................OPEN SELECT IMAGE ALERT BOX...........
    private void selectImage() {

        final CharSequence[] items = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.alert_dialog_custom_title, null);
        TextView title=(TextView)view.findViewById(R.id.alertTitle);
        title.setText("Add Photo!");
        builder.setCustomTitle(view);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                boolean result = Utility.checkPermission(ParticipantDetailForm.this);

                if (items[item].equals("Take Photo")) {

                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {

                    if (result)

                        galleryIntent();

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    //.................CHOOSE IMAGE FROM GALLERY..............
    private void galleryIntent() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);


    }

    //.................TAKE IMAGE FROM CAMERA..............
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Utility.getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                //Uri selectedImageUri = data.getData();

                Picasso.with(this)
                        .load(fileUri)
                        /*.resize(100,100)*/
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_user)
                        .into(imageView);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();


                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), fileUri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                // External sdcard location
                File mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        getString(R.string.app_name)); //Create folder fitkitchen in SD-Card

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists()) {


                    if (!mediaStorageDir.mkdirs()) {


                    }
                }
                String title = String.valueOf(System.currentTimeMillis());
                destination = new File(mediaStorageDir.getPath(),
                        title + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();

                Picasso.with(this)
                        .load(selectedImageUri)
                        /*.resize(100,100)*/
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_user)
                        .into(imageView);


                ByteArrayOutputStream bytes = new ByteArrayOutputStream();


                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // External sdcard location
                File mediaStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        getString(R.string.app_name));//Create folder fitkitchen in SD-Card

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists()) {

                    if (!mediaStorageDir.mkdirs()) {


                    }

                }

                String title = String.valueOf(System.currentTimeMillis());
                destination = new File(mediaStorageDir.getPath(),
                        title + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
