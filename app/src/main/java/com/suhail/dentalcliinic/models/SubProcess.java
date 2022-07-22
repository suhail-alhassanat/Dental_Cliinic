package com.suhail.dentalcliinic.models;

public class SubProcess {
  private String name;
  private String type;
  private float price;

    public SubProcess() {
    }

    public SubProcess(String name, String type, float price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
