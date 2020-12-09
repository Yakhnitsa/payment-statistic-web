package com.yurets_y.payment_statistic_web.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(Views.ShortView.class)
    private String name;

    @JsonView(Views.ShortView.class)

    private Integer railroadCode;

    private Integer edrpuCode;

    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getEdrpuCode() {
        return edrpuCode;
    }

    public void setEdrpuCode(Integer edrpuCode) {
        this.edrpuCode = edrpuCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("code")
    public Integer getRailroadCode() {
        return railroadCode;
    }

    public void setRailroadCode(Integer railroadCode) {
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
