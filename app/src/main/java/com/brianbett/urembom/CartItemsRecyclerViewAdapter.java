package com.brianbett.urembom;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CartItemsRecyclerViewAdapter extends RecyclerView.Adapter<CartItemsRecyclerViewAdapter.MyViewHolder> {

    List<RealmCartItem> cartItemsList;
    Context context;

    Realm realm;

    public CartItemsRecyclerViewAdapter(List<RealmCartItem> cartItemsList, Context context,Realm realm) {
        this.cartItemsList = cartItemsList;
        this.context = context;
        this.realm=realm;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView= LayoutInflater.from(context).inflate(R.layout.single_cart_item,parent,false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("1");
        spinnerArray.add("2");
        spinnerArray.add("3");
        spinnerArray.add("4");
        spinnerArray.add("5");
        spinnerArray.add("6");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.quantitySpinner.setAdapter(adapter);
//        int adapterPosition=adapter.getPosition(String.valueOf(cartItemsList.get(position).getQuantity()));
//        holder.quantitySpinner.setSelection(adapterPosition);
        holder.quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int currentQty=Integer.parseInt(String.valueOf(adapterView.getItemAtPosition(i)));
                if(currentQty!=1) {
                    final RealmCartItem cartItem = realm.where(RealmCartItem.class).equalTo("productId", cartItemsList.get(holder.getAdapterPosition()).getProductId()).findFirst();
                    realm.beginTransaction();
                    assert cartItem != null;
                    holder.quantitySpinner.setSelection(currentQty);
                    cartItem.setQuantity(currentQty);
                    realm.insertOrUpdate(cartItem);
                    realm.commitTransaction();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        holder.productName.setText(cartItemsList.get(position).getProductName());
        holder.productPrice.setText(String.valueOf(cartItemsList.get(position).getProductPrice()));
        Glide.with(context).load(Uri.parse(cartItemsList.get(position).getImage())).into(holder.productImage);


    }

    @Override
    public int getItemCount() {
        return cartItemsList.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView productImage;
        TextView productPrice,productName;
        Spinner quantitySpinner,colorsSpinner;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.cart_item_image);
            productName=itemView.findViewById(R.id.cart_item_name);
            productPrice=itemView.findViewById(R.id.cart_item_price);
            quantitySpinner=itemView.findViewById(R.id.quantity_spinner);
//            colorsSpinner=itemView.findViewById(R.id.colors_spinner);
        }
    }
    public void removeItem(int position){
        cartItemsList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();

    }
}
