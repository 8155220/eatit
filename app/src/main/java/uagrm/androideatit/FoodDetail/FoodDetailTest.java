package uagrm.androideatit.FoodDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.stepstone.apprating.listener.RatingDialogListener;

import uagrm.androideatit.Model.Food;
import uagrm.androideatit.R;

/**
 * Created by Shep on 11/16/2017.
 */

public class FoodDetailTest extends AppCompatActivity implements RatingDialogListener {
    public static final String EXTRA_NOTE_ID = "NOTE_ID";
    Food currentFood;
    FoodDetailFragment detailFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_food);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        // Get the requested product id
        String productId = getIntent().getStringExtra("FoodId");
        currentFood = (Food) getIntent().getSerializableExtra("currentFood");
        initFragment(FoodDetailFragment.newInstance(productId,currentFood));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initFragment(Fragment detailFragment) {
        // Add the ProductsDetailFragment to the layout
        this.detailFragment= (FoodDetailFragment) detailFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, detailFragment);
        transaction.commit();
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {
        this.detailFragment.onPositiveButtonClicked(i,s);
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}
