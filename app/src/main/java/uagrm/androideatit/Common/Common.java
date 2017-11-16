package uagrm.androideatit.Common;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import uagrm.androideatit.Model.User;

public class Common {
    public static User currentUser;

    public static final String DELETE = "Eliminar";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static String convertCodeToStatus(String code)
    {
        if(code.equals("0"))
            return "Pendiente";
        else if(code.equals("1"))
            return "En camino";
        else
            return "Enviado";
    }

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager!=null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info!=null)
            {
                for (int i = 0; i < info.length; i++) {
                    if(info[i].getState()==NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

}
