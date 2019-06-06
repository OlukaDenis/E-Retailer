package com.jusutech.easyshopugserver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jusutech.easyshopugserver.Common.Common;
import com.jusutech.easyshopugserver.Interface.ItemClickListener;
import com.jusutech.easyshopugserver.Model.Product;
import com.jusutech.easyshopugserver.ViewHolder.ProductViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import mehdi.sakout.fancybuttons.FancyButton;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;

    //firebase
    FirebaseDatabase db;
    DatabaseReference itemList;
    FirebaseStorage storage;
    StorageReference storageReference;

    MaterialEditText edtName, editDescription, editPrice, editDiscount;
    FancyButton  btnUploadImage;
    ImageView btnSelectImage;
    Product newProduct;
    Uri saveUri;
    RelativeLayout rootLayout;


    String categoryId = "";
    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;
    String category_name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        category_name = getIntent().getStringExtra("CategoryName");
        setTitle(category_name);

        //firebase
        db = FirebaseDatabase.getInstance();
        itemList = db.getReference("Products");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //init
        recyclerView = (RecyclerView) findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddProductDialog();
            }
        });
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty())
            loadListProduct(categoryId);
    }

    private void showAddProductDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductList.this);
        alertDialog.setTitle("Add new product");
        alertDialog.setMessage("Please fill in the product information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_item_layout, null);

        edtName = add_menu_layout.findViewById(R.id.editName);
        editDescription = add_menu_layout.findViewById(R.id.editDescription);
        editPrice = add_menu_layout.findViewById(R.id.editPrice);
        btnSelectImage = add_menu_layout.findViewById(R.id.select_image);
        btnUploadImage = add_menu_layout.findViewById(R.id.upload_image);


        //adding onclick event on the buttons
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();//let the user choose the image and save its uri
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                //validateName();//not to accept empty category name
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_cart_plus);


        //setting the buttons
        alertDialog.setPositiveButton("ADD PRODUCT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //Creating a new category
                if (newProduct != null) {
                    itemList.push().setValue(newProduct);
                    Snackbar.make(rootLayout, "New Product " + newProduct.getName() + " has been added", Snackbar.LENGTH_SHORT).show();
                }
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), Common.PICK_IMAGE_REQUEST);


    }

    private void uploadImage() {
        if (saveUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading....");
            progressDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image uploaded succesfully",
                                    Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set a value for a new category if image uploaded, and then get the download link
                                    newProduct = new Product();
                                    newProduct.setDescription(editDescription.getText().toString());
                                    newProduct.setAddedBy(Common.currentUser.getName());
                                    newProduct.setImage(uri.toString());
                                    newProduct.setMenuid(categoryId);
                                    newProduct.setName(edtName.getText().toString());
                                    newProduct.setPrice(editPrice.getText().toString());
                                    newProduct.setAvailable("1");
                                    newProduct.setBargained("0");


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProductList.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading " + progress + "%");

                        }
                    });
        }
    }

    private void loadListProduct(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class,
                R.layout.layout_product_item,
                ProductViewHolder.class,
                itemList.orderByChild("menuid").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, final Product model, final int position) {
                viewHolder.product_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.product_image);

                viewHolder.delete_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteProduct(adapter.getRef(position).getKey());
                    }
                });

                viewHolder.edit_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(), model);
                    }
                });
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            saveUri = data.getData();
        }
    }

    private void deleteProduct(String key) {
        itemList.child(key).removeValue();
        Snackbar.make(rootLayout, "Product has been deleted", Snackbar.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final Product item) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductList.this);
        alertDialog.setTitle("Edit product");
        alertDialog.setMessage("Please fill in the product information *");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_item_layout, null);

        edtName = add_menu_layout.findViewById(R.id.editName);
        editDescription = add_menu_layout.findViewById(R.id.editDescription);
        editPrice = add_menu_layout.findViewById(R.id.editPrice);
        btnSelectImage = add_menu_layout.findViewById(R.id.select_image);
        btnUploadImage = add_menu_layout.findViewById(R.id.upload_image);

        //set default values for the product
        edtName.setText(item.getName());
        editDescription.setText(item.getDescription());
        editPrice.setText(item.getPrice());


        //adding onclick event on the buttons
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();//let the user choose the image and save its uri
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage(item);
                //validateName();//not to accept empty category name
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_cart_plus);


        //setting the buttons
        alertDialog.setPositiveButton("UPDATE PRODUCT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //Creating a new category
                    //update the information
                    item.setName(edtName.getText().toString());
                    item.setDescription(editDescription.getText().toString());
                    item.setPrice(editPrice.getText().toString());

                    itemList.child(key).setValue(item);
                    Snackbar.make(rootLayout, "Product " + item.getName() + " has been updated", Snackbar.LENGTH_SHORT).show();

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

    private void changeImage(final Product item) {

        if (saveUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading....");
            progressDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image uploaded successfully",
                                    Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set a value for a new category if image uploaded, and then get the download link
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProductList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading "+progress+"%");

                        }
                    });
        }
    }
}
