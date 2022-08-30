package com.brianbett.urembom;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmCartItem extends RealmObject {
    @PrimaryKey String productId;

    String productName;
    int productPrice;
    int quantity;
    RealmList<String> colors;
    String image;

    String selectedColor;

    public String getSelectedColor() {
        return selectedColor;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public RealmList<String> getColors() {
        return colors;
    }

    public RealmCartItem() {
    }

    public RealmCartItem(String productId, String productName, int productPrice, String image) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = 1;
        this.image = image;
//        this.selectedColor = selectedColor;
    }




    public String getImage() {
        return image;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setColors(RealmList<String> colors) {
        this.colors = colors;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }
}
