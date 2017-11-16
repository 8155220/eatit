package uagrm.androideatit;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.sql.SQLOutput;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uagrm.androideatit.Common.Common;
import uagrm.androideatit.Database.Database;
import uagrm.androideatit.Model.MyResponse;
import uagrm.androideatit.Model.Notification;
import uagrm.androideatit.Model.Order;
import uagrm.androideatit.Model.Request;
import uagrm.androideatit.Model.Sender;
import uagrm.androideatit.Model.Token;
import uagrm.androideatit.Remote.APIService;
import uagrm.androideatit.ViewHolder.CartAdapter;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    FButton btnPlace;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    MaterialEditText edtComment, edtAddress;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Init Service
        mService = Common.getFCMService();


        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlace = (FButton) findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new requests
                if (cart.size() > 0)
                    showAlertdialog();
                else
                    Toast.makeText(Cart.this, "Tu carrito esta vacio", Toast.LENGTH_SHORT).show();

            }
        });

        loadListFood();
    }

    private void showAlertdialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Un paso mas !");
        alertDialog.setMessage("Ingresa tu direccion:");

        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment = inflater.inflate(R.layout.order_address_comment, null);

        edtAddress = (MaterialEditText) order_address_comment.findViewById(R.id.edtAddress);
        edtComment = (MaterialEditText) order_address_comment.findViewById(R.id.edtComment);

        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        "0",
                        edtComment.getText().toString(),
                        cart
                );
                //Submit to firebase
                //We will using System.CurrentMilli to key
                String order_number = String.valueOf(System.currentTimeMillis());
                requests.child(order_number)
                        .setValue(request);
                //Delete cart
                new Database(getBaseContext()).cleanCart();
                //Toast.makeText(Cart.this,"Gracias , Orden ingresada",Toast.LENGTH_SHORT).show();
                loadListFood();
//                finish();
                sendNotificationOrder(order_number);


            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void sendNotificationOrder(final String order_number) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("serverToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
                    System.out.println("TOKEN:"+serverToken.toString());
                    System.out.println("getTOKEN:"+serverToken.getToken());
                    System.out.println("PostSnapshotValue :"+postSnapshot.getValue().toString());

                    //Create raw payload to send
                    Notification notification = new Notification("SHEP", "Tienes una nueva orden :" + order_number);
                    Sender content = new Sender(serverToken.getToken(), notification);

                    System.out.println("ENTRO ANTES DE NOTIFICACION");

                    System.out.println("IMPRIMIENTO CONTENT :"+content.toString());
                    mService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    //only run when get result
                                    //System.out.println("RESPONSE :"+call.());
                                    if(response.code()==200)
                                    {
                                    System.out.println("ENTRO DESPUES DE NOTIFICACION");

                                    if (response.body().success == 1) {
                                            Toast.makeText(Cart.this, "Gracias , Orden ingresada", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(Cart.this, "Fallo !!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERRORNOTIFICATION",t.getMessage());

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        //Calculate total price
        int total = 0;
        for (Order order : cart)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        //Vamos a eliminar un item en List<order> por posicion
        cart.remove(position);
        //despues de eso, elminaremos toda la data antigua de SQLite
        new Database(this).cleanCart();
        //Finalmente, actualizaremos la nueva data de List<order> to SQLite
        for (Order item : cart)
            new Database(this).addToCart(item);
        //Refresh
        loadListFood();
    }
}
