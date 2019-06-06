package com.jusutech.easyshopugserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jusutech.easyshopugserver.Common.Common;
import com.jusutech.easyshopugserver.ViewHolder.OrderDetailAdapter;
import com.squareup.picasso.Picasso;

public class OrderDetail extends AppCompatActivity {

    TextView order_id, order_phone,order_address,order_total, personName;
    String order_id_value="";
    RecyclerView listItems;
    RecyclerView.LayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setTitle("Order Details");

        order_id = (TextView)findViewById(R.id.order_id);
        order_phone = (TextView)findViewById(R.id.order_phone);
        order_address = (TextView)findViewById(R.id.order_address);
        order_total = (TextView)findViewById(R.id.order_total);
        personName = (TextView)findViewById(R.id.person_name);

        listItems = (RecyclerView)findViewById(R.id.lstItems);
        listItems.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listItems.setLayoutManager(layoutManager);

        if(getIntent()!= null)
            order_id_value = getIntent().getStringExtra("OrderId");

        //set value
        order_id.setText(order_id_value);
        order_phone.setText(Common.currentRequest.getContact());
        order_total.setText(String.valueOf(Common.currentRequest.getTotal()));
        order_address.setText(Common.currentRequest.getAddress());
        personName.setText(Common.currentRequest.getName());





        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getOrders(), getApplicationContext());
        listItems.setAdapter(adapter);

    }
}
