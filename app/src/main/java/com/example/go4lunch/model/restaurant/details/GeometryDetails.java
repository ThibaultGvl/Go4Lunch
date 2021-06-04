
package com.example.go4lunch.model.restaurant.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryDetails {

    @SerializedName("location")
    @Expose
    private LocationDetails location;
    @SerializedName("viewport")
    @Expose
    private ViewportDetails viewport;

    public LocationDetails getLocation() {
        return location;
    }

    public void setLocation(LocationDetails location) {
        this.location = location;
    }

    public ViewportDetails getViewport() {
        return viewport;
    }

    public void setViewport(ViewportDetails viewport) {
        this.viewport = viewport;
    }

}
