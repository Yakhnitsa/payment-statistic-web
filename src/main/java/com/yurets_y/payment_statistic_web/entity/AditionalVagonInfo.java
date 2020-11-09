package com.yurets_y.payment_statistic_web.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;

@Entity
public class AditionalVagonInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JsonView(Views.NormalView.class)
    private Vagon vagon;

    @JsonView(Views.NormalView.class)
    private Boolean hasCert;


    public AditionalVagonInfo() {
    }

    public AditionalVagonInfo(Vagon vagon) {
        this.vagon = vagon;
    }


    public Vagon getVagon() {
        return vagon;
    }

    public void setVagon(Vagon vagon) {
        this.vagon = vagon;
    }

    public Boolean getHasCert() {
        return hasCert;
    }

    public void setHasCert(Boolean hasCert) {
        this.hasCert = hasCert;
    }
}
