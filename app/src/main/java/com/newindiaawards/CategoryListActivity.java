package com.newindiaawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Mahesh on 05/12/17.
 */

public class CategoryListActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);
        setUpView();
    }

    private void setUpView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("AWARD CATEGORIES");

        CardView cardCatOne=(CardView)findViewById(R.id.cardCatOne);
        CardView cardCatTwo=(CardView)findViewById(R.id.cardCatTwo);
        CardView cardCatThree=(CardView)findViewById(R.id.cardCatThree);
        CardView cardCatFour=(CardView)findViewById(R.id.cardCatFour);
        CardView cardCatFive=(CardView)findViewById(R.id.cardCatFive);
        CardView cardCatSix=(CardView)findViewById(R.id.cardCatSix);
        CardView cardCatSeven=(CardView)findViewById(R.id.cardCatSeven);
        CardView cardCatEight=(CardView)findViewById(R.id.cardCatEight);
        CardView cardCatNine=(CardView)findViewById(R.id.cardCatNine);
        CardView cardCatTen=(CardView)findViewById(R.id.cardCatTen);

        cardCatOne.setOnClickListener(this);
        cardCatTwo.setOnClickListener(this);
        cardCatThree.setOnClickListener(this);
        cardCatFour.setOnClickListener(this);
        cardCatFive.setOnClickListener(this);
        cardCatSix.setOnClickListener(this);
        cardCatSeven.setOnClickListener(this);
        cardCatEight.setOnClickListener(this);
        cardCatNine.setOnClickListener(this);
        cardCatTen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.cardCatOne:
                goToList("1","Ambassadors of New India");
                break;
            case R.id.cardCatTwo:
                goToList("2","Architect of New India");
                break;
            case R.id.cardCatThree:
                goToList("3","Backbone of New India");
                break;
            case R.id.cardCatFour:
                goToList("4","Change Advocates of New India");
                break;
            case R.id.cardCatFive:
                goToList("5","Fortune hunters of New India");
                break;
            case R.id.cardCatSix:
                goToList("6","Healers of New India");
                break;
            case R.id.cardCatSeven:
                goToList("7","Special achievers of New India");
                break;
            case R.id.cardCatEight:
                goToList("8","Spirit of New India");
                break;
            case R.id.cardCatNine:
                goToList("9","Torch Bearers of New India");
                break;
            case R.id.cardCatTen:
                goToList("10","Trendsetter of New India");
                break;
        }

    }

    private void goToList(String id,String name){
        Intent intent=new Intent(CategoryListActivity.this,NomineesListActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        startActivity(intent);
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
