package com.yurets_y.payment_statistic_web.entity;

public class Client {
    private String name;
    private String railroadCode;
    private String edrpuCode;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEdrpuCode() {
        return edrpuCode;
    }

    public void setEdrpuCode(String edrpuCode) {
        this.edrpuCode = edrpuCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRailroadCode() {
        return railroadCode;
    }

    public void setRailroadCode(String railroadCode) {
        this.railroadCode = railroadCode;
    }

    public String getCodeAndName() {
        return String.format("(%s) %s", railroadCode, name);
    }

    @Override
    public String toString() {
        return String.format("%s, код: %s, адресс: %s", name, railroadCode, address);
    }
}
