package com.brianbett.urembom;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.brianbett.urembom.retrofit.SingleProduct;

import java.util.List;

public class MyViewModel extends ViewModel {
    private MutableLiveData<List<SingleProduct>> allProducts;

    public MutableLiveData<List<SingleProduct>> getAllProducts() {
        if(allProducts==null){
            allProducts=new MutableLiveData<>();
        }
        return allProducts;
    }
}
