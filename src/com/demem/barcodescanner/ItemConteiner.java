package com.demem.barcodescanner;

public class ItemConteiner {

    private String itemName;
    private String imagePath;

    public ItemConteiner(){}

    public ItemConteiner(String itemName, String imagePath){
        this.itemName = itemName;
        this.imagePath = imagePath;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
