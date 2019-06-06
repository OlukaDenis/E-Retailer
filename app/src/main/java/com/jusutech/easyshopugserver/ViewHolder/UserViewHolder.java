package com.jusutech.easyshopugserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jusutech.easyshopugserver.Interface.ItemClickListener;
import com.jusutech.easyshopugserver.R;


public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView username;
    //public ImageView profile_pic;
    private ItemClickListener itemClickListener;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        username = (TextView) itemView.findViewById(R.id.tv_username);
        // profile_pic = (ImageView) itemView.findViewById(R.id.profile_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}