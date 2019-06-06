package com.jusutech.easyshopugserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jusutech.easyshopugserver.Interface.ItemClickListener;
import com.jusutech.easyshopugserver.R;

public class DistributorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView distName, distAddress, distContact;
    public ImageView distImage;
    public ItemClickListener itemClickListener;

    public DistributorViewHolder(@NonNull View itemView) {
        super(itemView);
        distName = itemView.findViewById(R.id.distributor_name);
        distAddress = itemView.findViewById(R.id.distributor_address);
        distImage = itemView.findViewById(R.id.distributor_image);
        distContact = itemView.findViewById(R.id.distributor_contact);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
