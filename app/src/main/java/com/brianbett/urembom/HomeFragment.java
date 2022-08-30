package com.brianbett.urembom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.brianbett.urembom.retrofit.ProductsInterface;
import com.brianbett.urembom.retrofit.RetrofitHandler;
import com.brianbett.urembom.retrofit.SingleProduct;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ProductsRecyclerViewAdapter productsRecyclerViewAdapter;
    ArrayList<SingleProduct> allProductsList;
    View popupView;
    PopupWindow popupWindow;
    View rootView;
    Realm realm;
    View loader,loaderText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_home, container, false);
        popupView=getLayoutInflater().inflate(R.layout.loading_ring,container,false);
        recyclerView= rootView.findViewById(R.id.products_recycler_view);
        loader=rootView.findViewById(R.id.loader);
        loaderText=rootView.findViewById(R.id.loading_text);
        allProductsList=new ArrayList<>();
//        MyViewModel myViewModel= new ViewModelProvider(requireActivity()).get(MyViewModel.class);
//        Log.d("Size",String.valueOf(myViewModel.getCartItems().getValue()));

        Realm.init(getContext());
        RealmConfiguration configuration= new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        realm=Realm.getDefaultInstance();



        productsRecyclerViewAdapter=new ProductsRecyclerViewAdapter(allProductsList,getActivity(),realm);

        recyclerView.setAdapter(productsRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PopupWindow popupWindow= new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
//        new Handler().postDelayed(() -> {
//            Loader loader=new Loader(requireActivity(),popupView,rootView,popupWindow);
//            loader.showLoader();
//        },100);

        recyclerView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        loaderText.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.loader_text_animation));

        MyViewModel myViewModel=new ViewModelProvider(requireActivity()).get(MyViewModel.class);


        RetrofitHandler.getAllProducts(getContext() ,new ProductsInterface() {

            @Override
            public void success(List<SingleProduct>allProducts) {

                myViewModel.getAllProducts().setValue(allProducts);
            }

            @Override
            public void failure(Throwable throwable) {
                Log.d("Exception",throwable.getMessage());

                recyclerView.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
            }
        });

//        adding observer to my view model
        @SuppressLint("NotifyDataSetChanged")
        final Observer<List<SingleProduct>> productsObserver=allProducts->{
            allProductsList.addAll(allProducts);
            productsRecyclerViewAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        };
        myViewModel.getAllProducts().observe(requireActivity(),productsObserver);



        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}