package com.brianbett.urembom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.brianbett.urembom.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartItemsRecyclerView;
    CartItemsRecyclerViewAdapter recyclerViewAdapter;
    List<RealmCartItem> cartItemsList;
    CoordinatorLayout coordinatorLayout;
    MaterialButton clearCartBtn,checkOutBtn ,backToProductsBtn;
    TextView subtotalsView,totalsView,taxView,shippingFeeView;
    View notEmptyCart;
    View emptyCart;

    Realm realm;
    RealmResults<RealmCartItem> cartItems;

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ColorDrawable colorDrawable=new ColorDrawable(Color.parseColor("#F037A5"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        cartItemsRecyclerView=findViewById(R.id.cart_item_recycler_view);
        coordinatorLayout=findViewById(R.id.coordinator_layout);

        clearCartBtn=findViewById(R.id.clear_cart);
        checkOutBtn=findViewById(R.id.check_out);
        backToProductsBtn=findViewById(R.id.back_to_products);
        subtotalsView=findViewById(R.id.subtotals);
        totalsView=findViewById(R.id.total_cost);
        shippingFeeView=findViewById(R.id.shipping_fee);
        taxView=findViewById(R.id.tax);



//        RealmHandler realmHandler=new RealmHandler(getApplicationContext());
//
////        realm.beginTransaction();
////        cartItemList=new ArrayList<>(realm.where(RealmCartItem.class).findAll());
//        List<RealmCartItem> cartItems=realmHandler.getAllCartItems();
        Realm.init(getApplicationContext());
        RealmConfiguration configuration= new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        realm=Realm.getDefaultInstance();

        cartItems=realm.where(RealmCartItem.class).findAll();

        cartItems.addChangeListener(realmResults ->{
            if(realmResults.size()==0){
                emptyCart.setVisibility(View.VISIBLE);
                notEmptyCart.setVisibility(View.GONE);
                emptyCart.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.opacity));
            }else {
                updateTotals(realmResults);
                cartItemsList.addAll(realmResults);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });


        emptyCart=findViewById(R.id.empty_cart);
        notEmptyCart=findViewById(R.id.cart_container);
        clearCartBtn.setOnClickListener(view->{
            realm.executeTransaction((realm->realm.delete(RealmCartItem.class)));
            emptyCart.setVisibility(View.VISIBLE);
            notEmptyCart.setVisibility(View.GONE);
        });
        backToProductsBtn.setOnClickListener(view->{
            startActivity(new Intent(CartActivity.this,MainActivity.class));
            finish();
        });
        if(cartItems.size()==0){
            emptyCart.setVisibility(View.VISIBLE);
            notEmptyCart.setVisibility(View.GONE);

        }
        cartItemsList=new ArrayList<>(cartItems);
        recyclerViewAdapter=new CartItemsRecyclerViewAdapter(cartItemsList,getApplicationContext(),realm);
        cartItemsRecyclerView.setAdapter(recyclerViewAdapter);
        enableSwipeToDelete();
        updateTotals(cartItemsList);


    }
    private void enableSwipeToDelete(){
        SwipeToDelete swipeToDelete=new SwipeToDelete(CartActivity.this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int currentAdapterPosition=viewHolder.getAdapterPosition();
                String productId=cartItems.get(currentAdapterPosition).getProductId();
                final RealmCartItem cartItem=realm.where(RealmCartItem.class).equalTo("productId",productId).findFirst();
//                realm.executeTransaction(realm -> {
//                    assert cartItem != null;
//                    cartItem.deleteFromRealm();
//                });
                realm.beginTransaction();
                assert cartItem != null;
                cartItem.deleteFromRealm();
                realm.commitTransaction();

                recyclerViewAdapter.removeItem(currentAdapterPosition);
                recyclerViewAdapter.notifyItemRemoved(currentAdapterPosition);
            }
        };
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(swipeToDelete);
        itemTouchHelper.attachToRecyclerView(cartItemsRecyclerView);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateTotals(List<RealmCartItem> currentCartItems){
        int subtotal =0;
        int shippingFee;
        int taxAmount;
        int cartTotal;

        for (int i=0;i<currentCartItems.size();i++){
            int productTotal=currentCartItems.get(i).getQuantity()*currentCartItems.get(i).getProductPrice();
            subtotal+=productTotal;
        }

        shippingFee=(int) (0.05*subtotal);
        taxAmount=(int) (0.03*subtotal);
        cartTotal=subtotal+shippingFee+taxAmount;
        subtotalsView.setText("Kshs. "+ subtotal);
        shippingFeeView.setText("Kshs. "+ shippingFee);
        taxView.setText("Kshs. "+ taxAmount);
        totalsView.setText("Kshs. "+ cartTotal);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartItems.removeAllChangeListeners();
        realm.close();
    }


}