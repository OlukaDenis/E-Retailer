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
 * Created by Junior Joseph on 1/14/2019.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {
    public TextView product_name;
    public ImageView product_image, delete_product, edit_product;
    private ItemClickListener itemClickListener;

    public ProductViewHolder(View itemView){
        super(itemView);

        product_name = (TextView) itemView.findViewById(R.id.item_name);
        product_image = (ImageView) itemView.findViewById(R.id.item_image);
        delete_product = (ImageView) itemView.findViewById(R.id.img_delete_product);
        edit_product = (ImageView) itemView.findViewById(R.id.img_edit_product);
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

        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }

}
