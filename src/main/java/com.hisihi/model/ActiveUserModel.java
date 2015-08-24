package com.hisihi.model;

import java.util.List;

/**
 * Created by andyYang on 2015/8/24.
 */
public class ActiveUserModel {
    private ActiveUserDataModel data;
    private String chart_name;
    private List<String> dates;

    public ActiveUserDataModel getData() {
        return data;
    }

    public void setData(ActiveUserDataModel data) {
        this.data = data;
    }

    public String getChart_name() {
        return chart_name;
    }

    public void setChart_name(String chart_name) {
        this.chart_name = chart_name;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }
}
