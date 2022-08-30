package com.brianbett.urembom.retrofit;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleProduct {
    private String name;
    private int price;
    private String description;
    private String image;
    private int initialPrice;
    private String company;
    private int averageRating;
    @Expose
    private List<List<String>> colors;
    private boolean freeShipping;
    private int numOfReviews;
    @SerializedName("_id")
    private String productId;


    public List<List<String>> getColors() {
        return colors;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public int getInitialPrice() {
        return initialPrice;
    }

    public String getCompany() {
        return company;
    }

    public String getAverageRating() {
        return String.valueOf(averageRating);
    }

    public boolean isFreeShipping() {
        return freeShipping;
    }

    public int getNumOfReviews() {
        return numOfReviews;
    }
}
