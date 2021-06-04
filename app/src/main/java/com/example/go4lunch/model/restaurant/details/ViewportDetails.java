
package com.example.go4lunch.model.restaurant.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewportDetails {

    @SerializedName("northeast")
    @Expose
    private NortheastDetails northeast;
    @SerializedName("southwest")
    @Expose
    private SouthwestDetails southwest;

    public NortheastDetails getNortheast() {
        return northeast;
    }

    public void setNortheast(NortheastDetails northeast) {
        this.northeast = northeast;
    }

    public SouthwestDetails getSouthwest() {
        return southwest;
    }

    public void setSouthwest(SouthwestDetails southwest) {
        this.southwest = southwest;
    }

}
