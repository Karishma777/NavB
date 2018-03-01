package com.newindiaawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.newindiaawards.application.AppClass;

import java.util.HashMap;

/**
 * Created by Mahesh on 01/12/17.
 */

public class HomeFragment  extends Fragment implements ViewPagerEx.OnPageChangeListener,View.OnClickListener {


   // private SwipeRefreshLayout swipeRefreshLayout;

    private View rootView;
    private SliderLayout mainSlider;
    private SliderLayout sponserSlider;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_categories, container, false);
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {


        CardView cardCatOne=(CardView)rootView.findViewById(R.id.cardCatOne);
        CardView cardCatTwo=(CardView)rootView.findViewById(R.id.cardCatTwo);
        CardView cardCatThree=(CardView)rootView.findViewById(R.id.cardCatThree);
        CardView cardCatFour=(CardView)rootView.findViewById(R.id.cardCatFour);
        CardView cardCatFive=(CardView)rootView.findViewById(R.id.cardCatFive);
        CardView cardCatSix=(CardView)rootView.findViewById(R.id.cardCatSix);
        CardView cardCatSeven=(CardView)rootView.findViewById(R.id.cardCatSeven);
        CardView cardCatEight=(CardView)rootView.findViewById(R.id.cardCatEight);
        CardView cardCatNine=(CardView)rootView.findViewById(R.id.cardCatNine);
        CardView cardCatTen=(CardView)rootView.findViewById(R.id.cardCatTen);

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



        mainSlider = (SliderLayout)rootView.findViewById(R.id.slider);
        mainSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mainSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mainSlider.setCustomAnimation(new DescriptionAnimation());
        mainSlider.setDuration(4000);
        mainSlider.addOnPageChangeListener(this);

        sponserSlider = (SliderLayout)rootView.findViewById(R.id.slidersponser);
        sponserSlider.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);
        sponserSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sponserSlider.setCustomAnimation(new DescriptionAnimation());
        sponserSlider.setDuration(4000);
        sponserSlider.addOnPageChangeListener(this);

        loadSlider();
        loadSponserSlider();


        final FloatingActionButton actionA = (FloatingActionButton) rootView.findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(AppClass.getUserId().length()>0){

                    startActivity(new Intent(getActivity(),ParticipantDetailForm.class));

                }else{
                    Intent i =new Intent(getActivity(),LoginActivity.class);
                    startActivity(i);
                }

            }
        });


        final FloatingActionButton actionB = (FloatingActionButton) rootView.findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),CategoryListActivity.class));

            }
        });


    }

    private void loadSlider(){


        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Slider 1",R.drawable.slider_one);
        file_maps.put("Slider 2",R.drawable.slider_two);


        for(String name : file_maps.keySet()){

            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            textSliderView
                    // .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mainSlider.addSlider(textSliderView);
        }
    }

    private void loadSponserSlider(){

//FlipHorizontal
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Slider 1",R.drawable.sponsor_one);
        file_maps.put("Slider 2",R.drawable.sponsor_two);


        for(String name : file_maps.keySet()){

            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            textSliderView
                    // .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            sponserSlider.addSlider(textSliderView);
        }
    }


    /*

    private void callApi() {


        if(!MyUtility.isConnected(getActivity())){
           // swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

       APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Category> call3 = apiInterface.getCategories();
        call3.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {

                Log.e("Data:",new Gson().toJson(response.body()));


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            Category model=response.body();

                            if(model.status == 1){

                                if(model.data.size() >0){

                                    list=new ArrayList<>();

                                    for(int i =0;i<model.data.size();i++){

                                        CategoryModel item=new CategoryModel();
                                        item.setName(model.data.get(i).name);
                                        item.setId(model.data.get(i).id);
                                        list.add(item);
                                    }

                                    showData();

                                }



                            }else{

                                MyUtility.showAlertMessage(getActivity(),MyUtility.NO_DATA_FOUND);
                            }





                        }else{

                            MyUtility.showAlertMessage(getActivity(),MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                call.cancel();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });


    }

    */

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
        Intent intent=new Intent(getContext(),NomineesListActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        startActivity(intent);
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


    private void logout(){

        AppClass.cleanSharedPreferences();
        AppClass.setUserLogged(false);
        Intent intent = new Intent(getContext(), SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
