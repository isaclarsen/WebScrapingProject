package org.example;

public class Product {
    private String link;
    private String price;

    public Product(){}

    public Product(String link, String price){
        this.link = link;
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
