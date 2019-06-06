package com.jusutech.easyshopugserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jusutech.easyshopugserver.Interface.ItemClickListener;
import com.jusutech.easyshopugserver.R;


public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView showMessage;
    //public ImageView profile_pic;
    private ItemClickListener itemClickListener;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        showMessage = (TextView) itemView.findViewById(R.id.show_message);
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
