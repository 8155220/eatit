package uagrm.androideatitserver.Common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import uagrm.androideatitserver.Model.Request;
import uagrm.androideatitserver.Model.User;
import uagrm.androideatitserver.Remote.APIService;
import uagrm.androideatitserver.Remote.FCMRetrofitClient;
import uagrm.androideatitserver.Remote.IGeoCoordinates;
import uagrm.androideatitserver.Remote.RetrofitClient;

/**
 * Created by Shep on 10/25/2017.
 */

public class Common {
    public static User currentUser;
    public static Request currentRequest;
    private static final String BASE_URL = "https://fcm.googleapis.com/";
    public static final String UPDATE ="Update";
    public static final String DELETE ="Delete";
    public static final int PICK_IMAGE_REQUEST = 71;

    public static final String baseUrl = "https://maps.googleapis.com";

    public static String convertCodeToStatus(String code)
    {
       if(code.equals("0"))
           return "Pendiente";
        else if(code.equals("1"))
           return "En camino";
       else
           return "Enviado";
    }
    public static IGeoCoordinates getGeoCodeService(){
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap,int newWidth,int newHeight)
    {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_8888);

        float scaleX = newWidth/(float)bitmap.getWidth();
        float scaleY = newHeight/(float)bitmap.getHeight();
        float pivotX=0,pivotY=0;

        Matrix scaleMatrix= new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;


    }
    public static APIService getFCMService()
    {
        return FCMRetrofitClient.getClient(BASE_URL).create(APIService.class);
    }


}
