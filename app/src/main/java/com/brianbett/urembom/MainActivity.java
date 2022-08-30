package com.brianbett.urembom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.brianbett.urembom.retrofit.Preferences;
import com.brianbett.urembom.retrofit.ProductsInterface;
import com.brianbett.urembom.retrofit.RetrofitHandler;
import com.brianbett.urembom.retrofit.SingleProduct;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Realm realm;
    RealmResults<RealmCartItem> cartItems;



    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ActionBar actionBar=getSupportActionBar();

        LayoutInflater layoutInflater=getLayoutInflater();
        @SuppressLint("InflateParams") View view=layoutInflater.inflate(R.layout.toolbar,null);
        Objects.requireNonNull(actionBar).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(view,new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//        actionBar.setDisplayHomeAsUpEnabled(true);



        Toolbar parent=(Toolbar) view.getParent();
        parent.setPadding(0,0,0,0);
        parent.setContentInsetsAbsolute(0,0);


        View logo=view.findViewById(R.id.logo);
        View searchIcon=view.findViewById(R.id.search_icon);
        EditText searchEditText=view.findViewById(R.id.search_products);
        View goToCart=view.findViewById(R.id.go_to_cart);
        View accountIcon=view.findViewById(R.id.show_account_popup);
        TextView cartItemCount=view.findViewById(R.id.cart_items_count);
        MyViewModel myViewModel=new  ViewModelProvider(MainActivity.this
        ).get(MyViewModel.class);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String searchQuery=searchEditText.getText().toString();

                RetrofitHandler.searchProducts(getApplicationContext(), searchQuery, new ProductsInterface() {
                    @Override
                    public void success(List<SingleProduct> allProducts) {
                        myViewModel.getAllProducts().setValue(allProducts);
                        synchronized (myViewModel) {
                            myViewModel.notify();
                        }
                    }

                    @Override
                    public void failure(Throwable throwable) {
                        Toast.makeText(MainActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Realm.init(getApplicationContext());
        RealmConfiguration configuration= new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        realm=Realm.getDefaultInstance();

        cartItems=realm.where(RealmCartItem.class).findAll();
        cartItemCount.setText(String.valueOf(cartItems.size()));
        cartItems.addChangeListener((RealmChangeListener<RealmResults<RealmCartItem>>) realmResults -> cartItemCount.setText(String.valueOf(realmResults.size())));

//        RealmHandler realmHandler=new RealmHandler(getApplicationContext());
//        RealmResults realmResults=realmHandler.getAllCartItems();
//        cartItemCount.setText(String.valueOf(realmResults));
//
//        realmResults.addChangeListener(new RealmChangeListener<RealmResults>() {
//            @Override

//            public void onChange(RealmResults realmResults) {
//                cartItemCount.
//            }
//        });
        goToCart.setOnClickListener(view3->{
            startActivity(new Intent(MainActivity.this,CartActivity.class));
        });
        accountIcon.setOnClickListener(view1->{
            View popupView=getLayoutInflater().inflate(R.layout.account_popup,null);
            PopupWindow popupWindow=new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,true);
            popupWindow.showAsDropDown(accountIcon,0,40,Gravity.CENTER_HORIZONTAL);
            popupWindow.setOutsideTouchable(false);
//            popupWindow.showAtLocation(accountIcon,Gravity.CENTER,0,0);
            TextView accountBtn=popupView.findViewById(R.id.account_btn);
            TextView loginBtn=popupView.findViewById(R.id.login_btn);
            String username= Preferences.getItemFromSP(getApplicationContext(),"username");
            if(username.length()>0){
                loginBtn.setText("Logout");
                accountBtn.setText(username);
            }

            accountBtn.setOnClickListener(view2 -> {
                if(accountBtn.getText().equals("Account")) {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }else {
                    startActivity(new Intent(MainActivity.this, AccountActivity.class));
                }
            });





            loginBtn.setOnClickListener(view2->{
                if(loginBtn.getText().equals("Login")) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }else{
                    Preferences.deleteItemFromSP(getApplicationContext(),"token");
                    Preferences.deleteItemFromSP(getApplicationContext(),"username");
                    popupWindow.dismiss();
                    Toast.makeText(getApplicationContext(),"Logout was successful",Toast.LENGTH_SHORT).show();
                }
            });

        });

//        showPopup();
        NavigationView navigationView= findViewById(R.id.navigation_view);

        Menu navigationMenu=navigationView.getMenu();

        HomeFragment homeFragment=new HomeFragment();


        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();

    }

    //    called when configuration changes such as orientation change
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartItems.removeAllChangeListeners();
        realm.close();
    }
}