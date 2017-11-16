package uagrm.androideatit.FoodDetail;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import uagrm.androideatit.Common.Common;
import uagrm.androideatit.Database.Database;
import uagrm.androideatit.Model.Food;
import uagrm.androideatit.Model.Order;
import uagrm.androideatit.Model.Rating;
import uagrm.androideatit.R;

public class FoodDetail extends AppCompatActivity
implements RatingDialogListener{

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart,btnRating;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;




    String foodId="";

    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference ratingTbl;

    Food currentFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Firebase
        database = FirebaseDatabase.getInstance().getInstance();
        foods=database.getReference("Foods");
        ratingTbl = database.getReference("Rating");

        //init view
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);
        btnRating = (FloatingActionButton) findViewById(R.id.btnRating);
        ratingBar = (RatingBar)findViewById(R.id.ratinBar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()

                ));

                Toast.makeText(FoodDetail.this,"Agregado al carrito", Toast.LENGTH_SHORT).show();

            }
        });


        food_description = (TextView)findViewById(R.id.food_description);
        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_image = (ImageView) findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Food Id from Intent
        if(getIntent()!=null){
            foodId = getIntent().getStringExtra("FoodId");
        }
        if(!foodId.isEmpty()){
            if(Common.isConnectedToInternet(getBaseContext()))
            {
                getDetailFood(foodId);
                getRatingFood(foodId);
            }
            else {
                Toast.makeText(FoodDetail.this,"verifique su conexion a internet",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getRatingFood(String foodId) {

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
                    ratingBar.setRating(average);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showRatingDialog() {
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
                .create(FoodDetail.this)
                .show();
    }

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());

                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());

                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        //Conseguir Rating y subir a firebase
        final Rating rating = new Rating(
                Common.currentUser.getPhone()
                ,foodId
                ,String.valueOf(value)
                ,comments);


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
                Toast.makeText(FoodDetail.this,"Gracias por enviar su rating",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}
