package com.sheasepherd.nescue.controller;

import com.sheasepherd.nescue.model.Size;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ReportForm {
    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    @NotNull
    private Size size;

    public ReportForm(){}

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
