package com.jusutech.easyshopugserver.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jusutech.easyshopugserver.Model.Order;
import com.jusutech.easyshopugserver.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jafix Technologies on 2/13/2019.
 */

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name, quantity, price, discount;
    public ImageView image;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.product_name);
        quantity = (TextView)itemView.findViewById(R.id.product_quantity);
        price = (TextView)itemView.findViewById(R.id.product_price);
        image = (ImageView)itemView.findViewById(R.id.product_image);

    }
}
public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {
    List<Order> myOrders;
    Context context;

    public OrderDetailAdapter(List<Order> myOrders, Context context) {
        this.myOrders = myOrders;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_order_detail,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText(String.format("Name: %s", order.getName()));
        holder.quantity.setText(String.format("Quantity: %s", order.getQuantity()));
        holder.price.setText(String.format("Price: %s", order.getPrice()));
        Picasso.with(context).load(order.getImage()).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }


}
