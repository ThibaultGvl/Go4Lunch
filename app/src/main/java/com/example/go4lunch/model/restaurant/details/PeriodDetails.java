
package com.example.go4lunch.model.restaurant.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeriodDetails {

    @SerializedName("open")
    @Expose
    private OpenDetails open;

    public OpenDetails getOpen() {
        return open;
    }

    public void setOpen(OpenDetails open) {
        this.open = open;
    }

}
