
package com.brianbett.urembom.retrofit;


import com.google.gson.annotations.Expose;

public class ProductDetails {

    @Expose
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
