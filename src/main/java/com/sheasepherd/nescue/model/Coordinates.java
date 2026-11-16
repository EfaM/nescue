package com.sheasepherd.nescue.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
public class Coordinates {

    private static final int SCALE = 5;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Column(precision = SCALE + 2,  scale = SCALE)
    private BigDecimal latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Column(precision = SCALE + 3,  scale = SCALE)
    private BigDecimal longitude;

    public Coordinates(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude.setScale(SCALE, RoundingMode.HALF_UP);
        this.longitude = longitude.setScale(SCALE, RoundingMode.HALF_UP);
    }

    protected Coordinates(){}

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}
