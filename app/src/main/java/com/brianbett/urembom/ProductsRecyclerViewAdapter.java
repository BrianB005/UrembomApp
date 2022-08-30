package com.brianbett.urembom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.urembom.retrofit.SingleProduct;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.MyViewHolder> {
    private final ArrayList<SingleProduct> allProducts;
    private final Context context;
    private final Realm realm;



    public ProductsRecyclerViewAdapter(ArrayList<SingleProduct> allProducts, Context context,Realm realm) {
        this.allProducts = allProducts;
        this.context = context;
        this.realm=realm;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singleProduct= LayoutInflater.from(context).inflate(R.layout.single_product,parent,false);

        return new MyViewHolder(singleProduct);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.productPrice.setText(String.valueOf(allProducts.get(position).getPrice()));
        holder.productInitialPrice.setText(String.valueOf(allProducts.get(position).getInitialPrice()));
        holder.companyName.setText(allProducts.get(position).getCompany());
        holder.productName.setText(allProducts.get(position).getName());
        holder.productDescription.setText(allProducts.get(position).getDescription());

        Glide.with(context).load(Uri.parse(allProducts.get(position).getImage())).into(holder.productImage);

        if(Integer.parseInt(allProducts.get(position).getAverageRating())==0){
            holder.productRating.setVisibility(View.GONE);
        }else {
            holder.productRating.setVisibility(View.VISIBLE);
            holder.productRating.setRating(Float.parseFloat(allProducts.get(position).getAverageRating()));
            holder.productRating.setNumStars(Integer.parseInt(allProducts.get(position).getAverageRating()));
        }
//        animations
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.opacity));
        holder.companyName.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.opacity));
        holder.productName.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.rotate));
        holder.productDescription.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.slide_ltr));
        holder.buttons.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.slide_rtl));

        holder.itemView.setOnClickListener(view->   openProductDetailsActivity(context,allProducts.get(holder.getAdapterPosition()).getProductId()));

        holder.openDetailsBtn.setOnClickListener(view -> openProductDetailsActivity(context,allProducts.get(holder.getAdapterPosition()).getProductId()));

        holder.addToCartBtn.setOnClickListener(view -> {
            RealmCartItem cartItem=new RealmCartItem(allProducts.get(holder.getAdapterPosition()).getProductId(),
                    allProducts.get(holder.getAdapterPosition()).getName(),allProducts.get(holder.getAdapterPosition()).getPrice(),
                    allProducts.get(holder.getAdapterPosition()).getImage());

//            RealmHandler realmHandler=new RealmHandler(context);
//            realmHandler.addCartItem(cartItem,allProducts.get(holder.getAdapterPosition()).getColors().get(0));
            realm.executeTransaction(realm1->{
                realm1.insertOrUpdate(cartItem);

            });


            Toast.makeText(context,allProducts.get(holder.getAdapterPosition()).getName()+" added to cart",Toast.LENGTH_SHORT).show();
        });



    }

    @Override
    public int getItemCount() {
        return allProducts.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView productImage;
        TextView productName,companyName,productPrice,productInitialPrice,productDescription;
        LinearLayout buttons;
        MaterialButton openDetailsBtn,addToCartBtn;
        RatingBar productRating;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image);
            productName=itemView.findViewById(R.id.product_name);
            companyName=itemView.findViewById(R.id.company_name);
            productPrice=itemView.findViewById(R.id.product_price);
            productInitialPrice=itemView.findViewById(R.id.product_initial_price);
            productDescription=itemView.findViewById(R.id.product_description);
            buttons=itemView.findViewById(R.id.buttons);
            openDetailsBtn=itemView.findViewById(R.id.check_product);
            addToCartBtn=itemView.findViewById(R.id.add_to_cart);
            productRating=itemView.findViewById(R.id.product_rating);

        }
    }

    private static void openProductDetailsActivity(Context context,String productId){
        Intent intent=new Intent(context,ProductDetailsActivity.class);
        intent.putExtra("productId",productId);
        context.startActivity(intent);
    }

}
