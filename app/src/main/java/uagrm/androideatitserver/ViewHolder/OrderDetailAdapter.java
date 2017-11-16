package uagrm.androideatitserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import uagrm.androideatitserver.Model.Order;
import uagrm.androideatitserver.R;

/**
 * Created by Shep on 10/28/2017.
 */

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView name,quantity,price,discount;


    public MyViewHolder(View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.product_name);
        quantity = (TextView)itemView.findViewById(R.id.product_quantity);
        price = (TextView)itemView.findViewById(R.id.product_price);
        discount = (TextView)itemView.findViewById(R.id.product_discount);

     }
}

public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Order> myOrders;

    public OrderDetailAdapter (List<Order> myOrders){
        this.myOrders=myOrders;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText(String.format("Nombre : %s",order.getProductName()));
        holder.quantity.setText(String.format("Cantidad : %s",order.getQuantity()));
        holder.price.setText(String.format("Precio : %s",order.getPrice()));
        holder.discount.setText(String.format("Descuento : %s",order.getDiscount()));

    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
