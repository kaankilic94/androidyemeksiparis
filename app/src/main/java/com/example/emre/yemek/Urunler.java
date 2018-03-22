package com.example.emre.yemek;

import java.io.Serializable;

/**
 * Created by Emre on 12.02.2018.
 */

public class Urunler implements Serializable {
    private String productId;
    private String productName;
    private String brief;
    private String description;
    private double price;
    private String categoryId;
    private String categoryName;
    private String image;
    private String imagenormal;
    private String imagethumb;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagenormal() {
        return imagenormal;
    }

    public void setImagenormal(String imagenormal) {
        this.imagenormal = imagenormal;
    }

    public String getImagethumb() {
        return imagethumb;
    }

    public void setImagethumb(String imagethumb) {
        this.imagethumb = imagethumb;
    }
}
