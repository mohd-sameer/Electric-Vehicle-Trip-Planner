package com.example.seccharge;

public class Product {
    private int id;
    private String title;
    private String shortdesc;
    private double rating;
    private double price;


    public Product(int id, String title, String shortdesc, double rating, double price) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.rating = rating;
        this.price = price;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public double getRating() {
        return rating;
    }

    public double getPrice() {
        return price;
    }


}
