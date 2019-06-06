package com.jusutech.easyshopugserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jusutech.easyshopugserver.Interface.ItemClickListener;
import com.jusutech.easyshopugserver.R;

/**
 * Created by Jafix Technologies on 2/13/2019.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    public TextView txtOrderId, txtOrderPhone, txtOrderStatus, txtOrderAddress, txtOrderTotal, txtOrderReceived, txtOrderTransporter;
    public ImageView editOrder, deleteOrder;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderTotal = itemView.findViewById(R.id.order_total);
        txtOrderReceived = itemView.findViewById(R.id.order_received);
        txtOrderTransporter = itemView.findViewById(R.id.order_transporter);
        editOrder = itemView.findViewById(R.id.img_edit_order);
        deleteOrder = itemView.findViewById(R.id.img_delete_order);
//        btnDetails = (Button)itemView.findViewById(R.id.btnDetail);


        itemView.setOnClickListener(this);
//        itemView.setOnLongClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select an action");
        contextMenu.add(0, 0, getAdapterPosition(), "Update");
        contextMenu.add(0, 1, getAdapterPosition(), "Delete");
    }

    //
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
