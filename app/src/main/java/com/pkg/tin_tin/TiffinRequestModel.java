package com.pkg.tin_tin;

public class TiffinRequestModel {
    private String Menu;
    private String cost;
    private String Type;
    private String MobileNO;
    private String CustomerName;

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public TiffinRequestModel() {
    }

    public String getMenu() {
        return Menu;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMobileNO() {
        return MobileNO;
    }

    public void setMobileNO(String mobileNO) {
        MobileNO = mobileNO;
    }
}
