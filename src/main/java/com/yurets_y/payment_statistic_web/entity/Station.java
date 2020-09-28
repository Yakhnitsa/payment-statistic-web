package com.yurets_y.payment_statistic_web.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="railroad_stations")
public class Station extends AuditableEntity{
    @Id
    @JsonView(Views.ShortView.class)
    private Integer code;
    @JsonView(Views.ShortView.class)
    private String rusName;
    @JsonView(Views.ShortView.class)
    private String ukrName;
    private String administration;
    private String railDepartment;
    private int node;

    @Column(name = "location_x")
    private Double locationX;

    @Column(name = "location_y")
    private Double locationY;

    public Station() {
    }

    public Station(Integer code, String rusName) {
        this.code = code;
        this.rusName = rusName;
    }

    public Station(Integer code, String rusName, String ukrName, String administration, String railDepartment, int node) {
        this.code = code;
        this.rusName = rusName;
        this.ukrName = ukrName;
        this.administration = administration;
        this.railDepartment = railDepartment;
        this.node = node;
    }

    @JsonView(Views.ShortView.class)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @JsonView(Views.ShortView.class)
    public String getRusName() {
        return rusName;
    }

    public void setRusName(String rusName) {
        this.rusName = rusName;
    }

    @JsonView(Views.ShortView.class)
    public String getUkrName() {
        return ukrName;
    }

    public void setUkrName(String ukrName) {
        this.ukrName = ukrName;
    }

    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    public String getRailDepartment() {
        return railDepartment;
    }

    public void setRailDepartment(String railDepartment) {
        this.railDepartment = railDepartment;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public Double getLocationX() {
        return locationX;
    }

    public void setLocationX(Double locationX) {
        this.locationX = locationX;
    }

    public Double getLocationY() {
        return locationY;
    }

    public void setLocationY(Double locationY) {
        this.locationY = locationY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station that = (Station) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
