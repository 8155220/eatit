/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uagrm.androideatit.FoodDetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import uagrm.androideatit.Common.Common;
import uagrm.androideatit.Database.Database;
import uagrm.androideatit.Model.Food;
import uagrm.androideatit.Model.Order;
import uagrm.androideatit.Model.Rating;
import uagrm.androideatit.R;

/**
 * Main UI for the note detail screen.
 */
public class FoodDetailFragment extends Fragment implements FoodDetailContract.View,RatingDialogListener {


    public static final String ARGUMENT_NOTE_ID = "NOTE_ID";

    private FoodDetailContract.Presenter mFoodDetailPresenter;



    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart,btnRating;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;

    //Model
    Food currentFood;
    String foodId="";



    public static FoodDetailFragment newInstance(String foodId, Food food) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_NOTE_ID, foodId);
        /*FoodDetailFragment fragment = new FoodDetailFragment();
        fragment.setFoodId(foodId);
        fragment.setCurrentFood(food);
        fragment.setArguments(arguments);*/
        FoodDetailFragment fragment = new FoodDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("foodId", foodId);
        bundle.putSerializable("currentFood",food);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("PRESENTER INICALIZADO");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        foodId = getArguments().getString("foodId");
        currentFood = (Food) getArguments().getSerializable("currentFood");
        mFoodDetailPresenter = new FoodDetailPresenter(this,currentFood,foodId);

        System.out.println("Argumentos :"+getArguments().toString());
       // System.out.println("currentFood :"+currentFood.toString());

        View root = inflater.inflate(R.layout.activity_food_detail, container, false);

        //init view
        numberButton = (ElegantNumberButton)root.findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) root.findViewById(R.id.btnCart);
        btnRating = (FloatingActionButton) root.findViewById(R.id.btnRating);
        ratingBar = (RatingBar)root.findViewById(R.id.ratinBar);

        food_description = (TextView)root.findViewById(R.id.food_description);
        food_name = (TextView) root.findViewById(R.id.food_name);
        food_price = (TextView) root.findViewById(R.id.food_price);
        food_image = (ImageView) root.findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)root.findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFoodDetailPresenter.addToCart(foodId,numberButton.getNumber(),currentFood);

            }
        });

        if(!foodId.isEmpty()){
            if(mFoodDetailPresenter.isConnectedToInternet())
            {
                mFoodDetailPresenter.getDetailFood();
                mFoodDetailPresenter.getRatingFood();
                //getDetailFood(foodId);
                //getRatingFood(foodId);
            }
            else {
                mFoodDetailPresenter.connectionError();
                //Toast.makeText(FoodDetail.this,"verifique su conexion a internet",Toast.LENGTH_SHORT).show();
                //return;
            }
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        String noteId = getArguments().getString(ARGUMENT_NOTE_ID);
        //mActionsListener.openFood(noteId);
    }

    @Override
    public void setRatingBar(float average){
        this.ratingBar.setRating(average);
    }

    @Override
    public void setFoodName(String name) {
        food_name.setText(name);
    }

    @Override
    public void setFoodPrice(String price) {
        food_price.setText(price);
    }

    @Override
    public void setFoodDescripcion(String descripcion) {
        food_description.setText(descripcion);
    }

    @Override
    public void setFoodImage(String url) {
        Glide.with(this).load(url)
                .into(food_image);
    }

    @Override
    public void setToolbarTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }

    @Override
    public void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Enviar")
                .setNegativeButtonText("Cancelar")
                .setNoteDescriptions(Arrays.asList("Muy mal","No Bueno","Un poco Bueno","Muy Bueno","Excelente"))
                .setDefaultRating(1)
                .setTitle("Califica esta comida")
                .setDescription("Por favor selecciona algunas estrellas y danos tu opinion")
                .setTitleTextColor(R.color.colorPrimary)
                //.setDescription(R.color.colorPrimary)
                .setHint("Por favor escriba su comentario aqui ...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(getActivity())
                .show();

    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(),text, Toast.LENGTH_SHORT).show();
    }

    public Context getFragmentContext(){
        return getContext();
    }

    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        final Rating rating = new Rating(
                Common.currentUser.getPhone()
                ,foodId
                ,String.valueOf(value)
                ,comments);

        mFoodDetailPresenter.sendRating(rating);

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public void setCurrentFood(Food currentFood) {
        this.currentFood = currentFood;
    }

    @Override
    public void clearDescripcion() {
        food_description.setText("");
    }

    @Override
    public void hideImage(){
        food_image.setVisibility(View.GONE);
    }
    @Override
    public void showImage(){
        food_image.setVisibility(View.VISIBLE);
    }
}
