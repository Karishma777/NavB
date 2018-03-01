package com.newindiaawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.newindiaawards.R;
import com.newindiaawards.application.AppClass;
import com.newindiaawards.utility.MyUtility;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_home);

      //  TextView tvName=(TextView)navigationView.findViewById(R.id.userName);
      //  tvName.setText(AppClass.getUserName());

     //   TextView tvEmail=(TextView)navigationView.findViewById(R.id.userEmail);
     //   tvEmail.setText(AppClass.getUserEmail());



    }

    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;

        switch (itemId) {

            case R.id.nav_home:

                HomeFragment homePageModule = new HomeFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, homePageModule, homePageModule.getTag()).commit();
                setTitle("HOME");

                break;


            case R.id.nav_aboutus:

                MyUtility.goToUrl(this,"http://www.newindiaawards.com/about");

                break;

            case R.id.nav_org:

                MyUtility.goToUrl(this,"http://www.newindiaawards.com/organizer");

                break;


            case R.id.nav_guest:

                MyUtility.goToUrl(this,"http://www.newindiaawards.com/guest");

                break;

            case R.id.nav_faq:

                MyUtility.goToUrl(this,"http://www.newindiaawards.com/faq");

                break;

            case R.id.nav_contact:

                MyUtility.goToUrl(this,"http://www.newindiaawards.com/faq#footer");

                break;


            case R.id.nav_terms:

                MyUtility.goToUrl(this,"http://www.newindiaawards.com/term");

                break;

            case R.id.nav_privacy:

                MyUtility.goToUrl(this,"http://www.newindiaawards.com/privacy");

                break;

            case R.id.logout:

                logout();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(AppClass.getUserId().length()>0){
            getMenuInflater().inflate(R.menu.home, menu);
            return true;
        }else{

            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void logout(){

        AppClass.cleanSharedPreferences();
        AppClass.setUserLogged(false);
        finish();
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
