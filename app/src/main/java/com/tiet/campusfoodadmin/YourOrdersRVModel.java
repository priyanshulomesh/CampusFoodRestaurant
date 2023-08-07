package com.tiet.campusfoodadmin;

public class YourOrdersRVModel {
    private String name,description,total,phNO,isPending;

    public YourOrdersRVModel(String name, String description, String total,String phNO,String isPending) {
        this.name = name;
        this.description = description;
        this.total = total;
        this.phNO=phNO;
        this.isPending=isPending;
    }
    public YourOrdersRVModel() {
    }
    public String getIsPending() {
        return isPending;
    }

    public void setIsPending(String isPending) {
        this.isPending = isPending;
    }

    public void setPhNO(String phNO) {
        this.phNO = phNO;
    }

    public String getPhNO() {
        return phNO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
