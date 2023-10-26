package com.example.firebasedemope;

public class Contact {
    private  int id;
    private String name;
    private String email;
    private String company;
    private String address;
    private String url;

    public Contact() {
    }

    public Contact(int id, String name, String email, String company, String address, String url) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.company = company;
        this.address = address;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
