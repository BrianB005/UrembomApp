
package com.brianbett.urembom.retrofit;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Product {

    @SerializedName("__v")
    private Long _V;
    @Expose
    private String _id;
    @Expose
    private Long averageRating;
    @Expose
    private String category;
    @Expose
    private List<List<String>> colors;
    @Expose
    private String company;
    @Expose
    private String createdAt;
    @Expose
    private String description;
    @Expose
    private Boolean freeShipping;
    @Expose
    private String id;
    @Expose
    private String image;
    @Expose
    private Long initialPrice;
    @Expose
    private String name;
    @Expose
    private Long numOfReviews;
    @Expose
    private Long price;
    @Expose
    private List<Review> reviews;
    @Expose
    private String updatedAt;
    @Expose
    private String user;

    public Long get_V() {
        return _V;
    }

    public void set_V(Long _V) {
        this._V = _V;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Long getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Long averageRating) {
        this.averageRating = averageRating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<List<String>> getColors() {
        return colors;
    }

    public void setColors(List<List<String>> colors) {
        this.colors = colors;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(Long initialPrice) {
        this.initialPrice = initialPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumOfReviews() {
        return numOfReviews;
    }

    public void setNumOfReviews(Long numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
