package com.demem.barcodescanner;

public class ShopContainer {

    private String shopName;
    private String address;

    public ShopContainer(){}

    public ShopContainer(String shopName, String address){
        this.shopName = shopName;
        this.address = address;
    }

    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
