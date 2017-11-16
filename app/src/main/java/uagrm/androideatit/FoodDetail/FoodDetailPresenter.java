/*
 * Copyright 2015, The Android Open Source Project
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import uagrm.androideatit.Common.Common;
import uagrm.androideatit.Database.Database;
import uagrm.androideatit.Model.Food;
import uagrm.androideatit.Model.Order;
import uagrm.androideatit.Model.Rating;


public class FoodDetailPresenter implements FoodDetailContract.Presenter {

    //private final NotesRepository mNotesRepository;

    private final FoodDetailContract.View mFoodDetailView;


    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference ratingTbl;

    Food currentFood;
    String foodId;


    public FoodDetailPresenter(FoodDetailContract.View view, Food currentFood,String foodid) {
        mFoodDetailView = view;
        this.foodId =foodid;
        this.currentFood = currentFood;

        //Firebase
        database = FirebaseDatabase.getInstance();
        foods=database.getReference("Foods");
        ratingTbl = database.getReference("Rating");

    }
    public FoodDetailPresenter(FoodDetailContract.View view, Food currentFood,String foodid,boolean test) {
        mFoodDetailView = view;
        this.foodId =foodid;
        this.currentFood = currentFood;

       /* //Firebase
        database = FirebaseDatabase.getInstance();
        foods=database.getReference("Foods");
        ratingTbl = database.getReference("Rating");*/

    }


    public void addToCart(String foodId, String quantity, Food food) {
        new Database(mFoodDetailView.getFragmentContext()).addToCart(new Order(
                foodId,
                food.getName(),
                quantity,
                food.getPrice(),
                food.getDiscount()

        ));
        mFoodDetailView.showToast("Agregado al carrito");
    }



    @Override
    public void openNote(@Nullable String noteId) {

    }

    @Override
    public void getDetailFood() {

        mFoodDetailView.setFoodImage(currentFood.getImage());
        mFoodDetailView.setToolbarTitle(currentFood.getName());
        mFoodDetailView.setFoodPrice(currentFood.getPrice());
        mFoodDetailView.setFoodName(currentFood.getName());
        mFoodDetailView.setFoodDescripcion(currentFood.getDescription());

    }

    @Override
    public void getRatingFood() {
        com.google.firebase.database.Query foodRating = ratingTbl.orderByChild("foodId").equalTo(foodId);

        foodRating.addValueEventListener(new ValueEventListener() {
            int count=0,sum=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
                if(count!=0)
                {
                    float average = sum/count;
                    mFoodDetailView.setRatingBar(average);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void connectionError() {
        mFoodDetailView.showToast("verifique su conexion a internet");
    }

    @Override
    public boolean isConnectedToInternet() {
        return Common.isConnectedToInternet(mFoodDetailView.getFragmentContext());
    }

    @Override
    public void sendRating(final Rating rating) {
        ratingTbl.child(Common.currentUser.getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(Common.currentUser.getPhone()).exists())
                {
                    //Elminar Valor antiguo
                    ratingTbl.child(Common.currentUser.getPhone()).removeValue();
                    //Actualizar nuevo valor
                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                }
                else
                {
                    //UActualizar nuevo valor
                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                }
                //Toast.makeText(FoodDetail.this,"Gracias por enviar su rating",Toast.LENGTH_SHORT).show();
                mFoodDetailView.showToast("Gracias por enviar su rating");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void showToast(String test) {
        mFoodDetailView.showToast(test);
    }

    @Override
    public void clearDescripcion() {
        mFoodDetailView.clearDescripcion();
    }

    @Override
    public void showImage() {
        mFoodDetailView.showImage();
    }

    @Override
    public void hideImage() {
        mFoodDetailView.hideImage();
    }
}
