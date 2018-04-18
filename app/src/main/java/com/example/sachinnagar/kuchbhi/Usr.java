package com.example.sachinnagar.kuchbhi;

/**
 * Created by Sachin Nagar on 4/12/2018.
 */

public class Usr
{
    public String name;
    public String image;
    public String status;

    public Usr()
    {

    }
    public Usr(String name, String image, String status) {
        this.name = name;
        this.image = image;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
