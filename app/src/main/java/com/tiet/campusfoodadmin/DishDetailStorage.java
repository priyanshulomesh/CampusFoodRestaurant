package com.tiet.campusfoodadmin;

import android.net.Uri;

import java.io.Serializable;

public class DishDetailStorage implements Serializable {
    private String image,dishName,dishDescription,dishPrice;


    public DishDetailStorage(String image, String dishName, String dishDescription, String dishPrice) {
        this.image = image;
        this.dishName = dishName;
        this.dishDescription = dishDescription;
        this.dishPrice = dishPrice;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public void setDishPrice(String dishPrice) {
        this.dishPrice = dishPrice;
    }

    public DishDetailStorage()
    {
//required for calls to DataSnapshot(read from database)
    }

    public String getImage() {
        return image;
    }

    public String getDishName() {
        return dishName;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public String getDishPrice() {
        return dishPrice;
    }
}
