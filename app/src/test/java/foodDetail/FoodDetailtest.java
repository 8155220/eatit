package foodDetail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uagrm.androideatit.FoodDetail.FoodDetail;
import uagrm.androideatit.FoodDetail.FoodDetailContract;
import uagrm.androideatit.FoodDetail.FoodDetailPresenter;
import uagrm.androideatit.Model.Food;

import static org.mockito.Mockito.verify;

/**
 * Created by Shep on 11/16/2017.
 */

public class FoodDetailtest {

    @Mock
    private FoodDetailContract.View mView;
    private FoodDetailPresenter mPresenter;
    private Food currenFood = new Food("Keperi"
    ,"http://medifoods.my/images/menu/p2_coconut_pao.jpg"
    ,"descripcion"
    ,"44"
    ,"10"
    ,"assjlfd");
    private String foodid = "02";


    @Before
    public void setupPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mPresenter = new FoodDetailPresenter(mView,currenFood,foodid,true);
    }

    @Test
    public void showToast() {
        // When adding a new note
        mPresenter.showToast("Helloworld Test");

        // Then add note UI is shown
        verify(mView).showToast("Helloworld Test");
    }
    @Test
    public void clearDescripcion() {
        // When adding a new note
        mPresenter.clearDescripcion();
        // Then add note UI is shown
        verify(mView).clearDescripcion();
    }

    @Test
    public void showImageOnClick(){
        mPresenter.showImage();
        verify(mView).showImage();
    }
    @Test
    public void hideImageOnClick(){
        mPresenter.hideImage();
        verify(mView).hideImage();
    }



}
