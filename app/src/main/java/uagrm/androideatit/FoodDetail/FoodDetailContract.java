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
import android.support.annotation.Nullable;

import uagrm.androideatit.Model.Food;
import uagrm.androideatit.Model.Rating;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface FoodDetailContract {

    interface View {

        public void setFoodName(String name);

        public void setFoodPrice(String price);

        public void setFoodDescripcion(String descripcion);

        public void setFoodImage(String url);

        public void setToolbarTitle(String title);

        public void hideImage();


        public void showImage();

        public void showRatingDialog();

        public void showToast(String text);
        public void clearDescripcion();

        public Context getFragmentContext();
        public void setRatingBar(float average);
    }
    
    interface Presenter {

        void addToCart(String foodId,String number,Food food);
        void openNote(@Nullable String noteId);

        void getDetailFood();

        void getRatingFood();

        void showToast(String test);

        void clearDescripcion();

        public void showImage();
        void hideImage();

        void connectionError();

        boolean isConnectedToInternet();

        void sendRating(Rating rating);
    }
}
