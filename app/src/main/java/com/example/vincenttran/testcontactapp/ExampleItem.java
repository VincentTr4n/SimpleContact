package com.example.vincenttran.testcontactapp;

public class ExampleItem {
    private String name;
    private String phone;

    public ExampleItem(String name, String text2) {
        this.name = name;
        this.phone = text2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ExampleItem{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
