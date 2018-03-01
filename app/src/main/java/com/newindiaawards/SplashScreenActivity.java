package com.newindiaawards;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.newindiaawards.R;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import com.newindiaawards.application.AppClass;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashScreenActivity extends AppCompatActivity {
    public static final int  PERMISSION_REQUEST_CODE= 123;


    //    2E:1D:02:8E:48:A0:93:89:A8:3E:5D:22:21:20:FE:E1:3B:A0:36:D0


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);

        ImageView imgsplash=(ImageView)findViewById(R.id.imgsplash);
        Picasso.with(this).load(R.drawable.splash_image).into(imgsplash);



        permisson();
      //  getHash();

    }

    public void getHash(){

        MessageDigest md = null;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        Log.e("SecretKey = ", Base64.encodeToString(md.digest(), Base64.DEFAULT));

  }

    private void goTomain() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                    finish();
                    Intent loginIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);


            }
        }, 2000);
    }


    public  void permisson() {


   /* if (!checkPermission()) {

        requestPermission();


    } else { */

        goTomain();


  //  }
}


    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted && readAccepted && writeAccepted) {

                        goTomain();

                    } else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            showMessageOKCancel();
                        }
                    }
                }


                break;
        }
    }
    private void showMessageOKCancel() {


        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setMessage("You need to allow access to all the permissions. If permissions not showing go to your app setting and allow permission");
        ad.setCancelable(false);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                permisson();
            }
        });

        AlertDialog alertDialog=ad.create();
        alertDialog.show();
    }


    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("Orientation","onConfigurationChanged");
        MyUtility.showToast("onConfigurationChanged",this);


        int orient = getResources().getConfiguration().orientation;
        switch(orient) {
            case Configuration.ORIENTATION_LANDSCAPE:

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                Log.e("Orientation","ORIENTATION_LANDSCAPE");

                MyUtility.showToast("ORIENTATION_LANDSCAPE",this);

                break;
            case Configuration.ORIENTATION_PORTRAIT:

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Log.e("Orientation","SCREEN_ORIENTATION_PORTRAIT");
                MyUtility.showToast("SCREEN_ORIENTATION_PORTRAIT",this);

                break;
            default:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                Log.e("Orientation","SCREEN_ORIENTATION_UNSPECIFIED");
                MyUtility.showToast("SCREEN_ORIENTATION_UNSPECIFIED",this);


        }
    }

    */


}
