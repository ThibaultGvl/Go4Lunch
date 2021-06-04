
package com.example.go4lunch.model.restaurant.details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHoursDetails {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("periods")
    @Expose
    private List<PeriodDetails> periods = null;
    @SerializedName("weekday_text")
    @Expose
    private List<String> weekdayText = null;

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<PeriodDetails> getPeriods() {
        return periods;
    }

    public void setPeriods(List<PeriodDetails> periods) {
        this.periods = periods;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }

}
