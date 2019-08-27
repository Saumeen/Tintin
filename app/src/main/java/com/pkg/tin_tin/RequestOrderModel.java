package com.pkg.tin_tin;

public class RequestOrderModel {
    private String custname;
    private String custphone;
    private String type;

    public RequestOrderModel(String custname, String custphone, String type) {
        this.custname = custname;
        this.custphone = custphone;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getCustphone() {
        return custphone;
    }

    public void setCustphone(String custphone) {
        this.custphone = custphone;
    }
}
