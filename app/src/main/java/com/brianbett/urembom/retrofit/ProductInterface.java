package com.brianbett.urembom.retrofit;

public interface ProductInterface {
    void success(ProductDetails productDetails);
    void failure(Throwable throwable);
}
