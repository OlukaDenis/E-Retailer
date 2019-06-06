package com.jusutech.easyshopugserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jusutech.easyshopugserver.Common.Common;
import com.jusutech.easyshopugserver.Interface.ItemClickListener;
import com.jusutech.easyshopugserver.R;

/**
 * Created by Junior Joseph on 1/8/2019.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {
    public TextView productName;
    public ImageView productImage, delete_category;
    private ItemClickListener itemClickListener;

    public MenuViewHolder(View itemView){
        super(itemView);

        productName = (TextView) itemView.findViewById(R.id.product_name);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        delete_category = (ImageView) itemView.findViewById(R.id.img_delete_category);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Choose an action");

        contextMenu.add(0,0,getAdapterPosition(),Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}
