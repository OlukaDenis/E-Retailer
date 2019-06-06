package com.jusutech.easyshopugserver;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jusutech.easyshopugserver.Common.Common;
import com.jusutech.easyshopugserver.Interface.ItemClickListener;
import com.jusutech.easyshopugserver.Model.Request;
import com.jusutech.easyshopugserver.ViewHolder.OrderViewHolder;

import java.text.NumberFormat;
import java.util.Locale;

import static com.jusutech.easyshopugserver.DistributorActivity.CHOOSE_DISTRIBUTOR;

public class OrderStatus extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    FirebaseDatabase db;
    DatabaseReference requests;
    Spinner spinner;
    EditText chooseDistributor;

    private String mDistributorName;
    private String mDistributorPhone;
    private String mDistributorAddress;

    private static final int PICK_DISTRIBUTOR = 23;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        setTitle("Order Requests");

        //firebase
        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");

        //init
        recyclerView = (RecyclerView) findViewById(R.id.list_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(); //load all the orders
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.layout_order,
                OrderViewHolder.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Request model, final int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());

                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                String totals = String.valueOf(model.getTotal());
                viewHolder.txtOrderPhone.setText(model.getContact());

                Locale locale = new Locale("en", "UG");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                int theRealTotal = Integer.parseInt(totals);
                viewHolder.txtOrderTotal.setText(numberFormat.format(theRealTotal));

                viewHolder.txtOrderReceived.setText(Common.convertCodeToReceived(model.getRecieved()));
                String transporter = model.getTransporter();
                if (transporter.equals("0")) {
                    viewHolder.txtOrderTransporter.setText("Transporter not assigned");
                } else {
                    viewHolder.txtOrderTransporter.setText(model.getTransporter());
                }

                viewHolder.editOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowUpdateDialog(adapter.getRef(position).getKey(), model);
                    }
                });

                viewHolder.deleteOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
                        alertDialog.setTitle("Attention!");
                        alertDialog.setMessage("Are you sure you want delete order?");

                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteOrder(adapter.getRef(position).getKey());
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();

                    }
                });
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent orderDetail = new Intent(OrderStatus.this, OrderDetail.class);
                        Common.currentRequest = model;
                        orderDetail.putExtra("OrderId", adapter.getRef(position).getKey());
                        startActivity(orderDetail);
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();
        adapter.notifyDataSetChanged();
    }


    private void ShowUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_update_order, null);


        spinner = (Spinner) view.findViewById(R.id.statusSpinner);
        chooseDistributor = view.findViewById(R.id.choose_distributor);
        final String defaultDistributor = chooseDistributor.getText().toString();
        chooseDistributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDistributor();
            }
        });


        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("UPDATE ORDER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedItemId()));

                item.setTransporter(mDistributorName + "," + mDistributorPhone);

                requests.child(localKey).setValue(item); //add to update size
                adapter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void pickDistributor() {

        Intent intent = new Intent(this, DistributorActivity.class);
        intent.putExtra(CHOOSE_DISTRIBUTOR, true);
        startActivityForResult(intent, PICK_DISTRIBUTOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_DISTRIBUTOR && resultCode == RESULT_OK) {

            mDistributorName = data.getStringExtra("distributorName");
            mDistributorPhone = data.getStringExtra("distributorPhone");
            mDistributorAddress = data.getStringExtra("distributorAddress");

            chooseDistributor.setText(mDistributorName + ", " + mDistributorPhone);

            Common.distributor_name = mDistributorName;
            Common.distributor_phone = mDistributorPhone;
        } else {
            Toast.makeText(this, "No distributor selected!....", Toast.LENGTH_SHORT).show();
        }
    }
}
