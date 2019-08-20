package com.pkg.tin_tin;

public class MenuData {
    private String menu;
    private String cost;
    private String type;
    private String Quantity;

    public MenuData(String menu, String cost, String type, String quantity) {
        this.menu = menu;
        this.cost = cost;
        this.type = type;
        Quantity = quantity;
    }

    public MenuData(String menu, String cost, String type) {
        this.menu = menu;
        this.cost = cost;
        this.type = type;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
