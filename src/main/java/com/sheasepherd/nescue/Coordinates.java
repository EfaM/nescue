package com.sheasepherd.nescue;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Embeddable
public class Coordinates {

    @NotNull
    @NotBlank
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Column(precision = 7,  scale = 5)
    private BigDecimal latitude;

    @NotNull
    @NotBlank
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Column(precision = 8,  scale = 5)
    private BigDecimal longitude;

    public Coordinates(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Coordinates(){}

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}
