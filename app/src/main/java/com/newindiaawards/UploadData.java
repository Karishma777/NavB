package com.newindiaawards;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.newindiaawards.apiclient.APIClient;
import com.newindiaawards.apiclient.APIInterface;
import com.newindiaawards.application.AppClass;
import com.newindiaawards.utility.MyUtility;
import com.newindiaawards.utility.Utility;
import com.newindiaawards.utility.Validations;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Explicate1 on 12/4/2017.
 */

public class UploadData extends AppCompatActivity implements View.OnClickListener {

    EditText speech,link;
    private Uri fileUri;
    private static final int REQUEST_CAMERA = 100;
    private static final int SELECT_FILE = 200;
    private float totalSize = 0;

    String myspeech,mylink;
    Button submit;
    ImageView imageView,imageView1,imageView2,imageView3,imageView4;
    LinearLayout linearLayout;
    CheckBox checkBox;

    List<File> addImages;
    int selectedImage=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_data);
        addImages=new ArrayList<>();
        setUpViews();
    }

    private void setUpViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CONTRIBUTION DETAILS");
        speech=(EditText)findViewById(R.id.spech);


        link=(EditText)findViewById(R.id.video_link);
        imageView=(ImageView) findViewById(R.id.imge1);
        imageView1=(ImageView)findViewById(R.id.image2);
        imageView2=(ImageView)findViewById(R.id.image3);
        imageView3=(ImageView)findViewById(R.id.image4);
        imageView4=(ImageView)findViewById(R.id.image5);
        checkBox=(CheckBox) findViewById(R.id.checkbox);

        submit=(Button) findViewById(R.id.btnSubmit);
        linearLayout=(LinearLayout) findViewById(R.id.parent);
        imageView.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        submit.setOnClickListener(this);
        checkBox.setOnClickListener(this);


    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.imge1:
                selectImage(1);

                break;

            case R.id.image2:
                selectImage(2);

                break;

            case R.id.image3:
                selectImage(3);

                break;

            case R.id.image4:
                selectImage(4);

                break;

            case R.id.image5:
                selectImage(5);

                break;

            case R.id.btnSubmit:
                MyUtility.hideKeyboard(link, this);
                myspeech = speech.getText().toString();
                mylink = link.getText().toString();

                if (myspeech.equalsIgnoreCase("")) {

                    MyUtility.showSnack(linearLayout, "Enter Your Contribution");

                } else
                if (myspeech.length() < 20) {

                    MyUtility.showSnack(linearLayout, "Enter atleast 20 words");

                } else
                if (myspeech.length() > 500) {

                    MyUtility.showSnack(linearLayout, "Valid length is 500 words");

                } else if (mylink.equalsIgnoreCase("")) {

                MyUtility.showSnack(linearLayout, "Enter Video Link");

            } else if (addImages.size()==0) {

                MyUtility.showSnack(linearLayout, "Add atleast one picture");

            }  else  if(!checkBox.isChecked()){
                    MyUtility.showSnack(linearLayout, "Please Agree the Terms And Conditions");


                }else {

                    if (MyUtility.isConnected(UploadData.this)) {

                        callAPI();

                    } else {

                        MyUtility.showToast(MyUtility.INTERNET_ERROR, UploadData.this);

                    }
                }




                    break;

        }

    }


    //................OPEN SELECT IMAGE ALERT BOX...........
    private void selectImage(final int number) {

        final CharSequence[] items = { "Choose from Gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.alert_dialog_custom_title, null);
        TextView title=(TextView)view.findViewById(R.id.alertTitle);
        title.setText("Add Photo!");
        builder.setCustomTitle(view);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                boolean result = Utility.checkPermission(UploadData.this);

                if (items[item].equals("Take Photo")) {

                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {

                    if (result)
                        galleryIntent(number);

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }


    //.................TAKE IMAGE FROM CAMERA..............
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Utility.getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    //.................CHOOSE IMAGE FROM GALLERY..............
    private void galleryIntent(int i) {


        selectedImage =i;

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                //Uri selectedImageUri = data.getData();

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();


                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),fileUri);
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
                String title=String.valueOf(System.currentTimeMillis());
                File destination = new File(mediaStorageDir.getPath(),
                        title + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();

                    addImages.add(destination);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            } else if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();

                switch (selectedImage){

                    case 1:
                        Picasso.with(this)
                                .load(selectedImageUri)
                                .resize(100,100)
                                .centerCrop()
                                .placeholder(R.drawable.icon_upload)
                                .into(imageView);
                        break;

                    case 2:
                        Picasso.with(this)
                                .load(selectedImageUri)
                                .resize(100,100)
                                .centerCrop()
                                .placeholder(R.drawable.icon_upload)
                                .into(imageView1);
                        break;

                }



                ByteArrayOutputStream bytes = new ByteArrayOutputStream();


                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),selectedImageUri);
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

                String title=String.valueOf(System.currentTimeMillis());
                File destination = new File(mediaStorageDir.getPath(),
                        title + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();

                    addImages.add(destination);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
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
        MultipartBody.Part filePart1=null;
        MultipartBody.Part filePart2=null;
        MultipartBody.Part filePart3=null;
        MultipartBody.Part filePart4=null;



        for(int i=0;i<addImages.size();i++){

            if(i==0){

                if(addImages.get(i)!=null){

                    filePart = MultipartBody.Part.createFormData("award_img[]", addImages.get(i).getName(), RequestBody.create(MediaType.parse("image/*"), addImages.get(i)));
                }

            }


            if(i==1){

                if(addImages.get(i)!=null){

                    filePart1 = MultipartBody.Part.createFormData("award_img[]", addImages.get(i).getName(), RequestBody.create(MediaType.parse("image/*"), addImages.get(i)));
                }

            }

            if(i==2){

                if(addImages.get(i)!=null){

                    filePart2 = MultipartBody.Part.createFormData("award_img[]", addImages.get(i).getName(), RequestBody.create(MediaType.parse("image/*"), addImages.get(i)));
                }

            }

            if(i==3){

                if(addImages.get(i)!=null){

                    filePart3 = MultipartBody.Part.createFormData("award_img[]", addImages.get(i).getName(), RequestBody.create(MediaType.parse("image/*"), addImages.get(i)));
                }

            }

            if(i==4){

                if(addImages.get(i)!=null){

                    filePart4 = MultipartBody.Part.createFormData("award_img[]", addImages.get(i).getName(), RequestBody.create(MediaType.parse("image/*"), addImages.get(i)));
                }

            }
        }



        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call = apiInterface.participant_media(
                toRequestBody("20"),
                toRequestBody(myspeech),
                toRequestBody(mylink),
                filePart,filePart1,filePart2,filePart3,filePart4
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

                                finish();
                                Intent loginIntent = new Intent(UploadData.this, HomeActivity.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginIntent);


                            }else{

                                MyUtility.showAlertMessage(UploadData.this, MyUtility.FAILED_TO_GET_DATA);

                            }

                            Log.e("Data:", new Gson().toJson(response.body()));

                        } else {

                            MyUtility.showAlertMessage(UploadData.this, jsonObject.get("error").toString());
                        }



                        break;

                    case 500:

                        MyUtility.showAlertMessage(UploadData.this, MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(UploadData.this, MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(UploadData.this, MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                if(dialog.isShowing()) {

                    dialog.dismiss();

                }
                MyUtility.showAlertMessage(UploadData.this, MyUtility.INTERNET_ERROR);

            }
        });


    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value); return body ;
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
