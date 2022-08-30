package com.brianbett.urembom.retrofit;

import java.util.List;

public interface ProductsInterface {
    void success(List<SingleProduct> allProducts);
    void failure(Throwable throwable);
}
