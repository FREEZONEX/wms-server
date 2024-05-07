package com.supos.app.vo;


import java.util.List;
import java.util.Map;

public class ShelfModel{

    private Map<String, List<String>> planeNames;      //{{A-01, A-02, A-03, A-04, A-05}, {B-01, B-02, B-03, B-04, B-05}}

    public Map<String, List<String>> getPlaneNames() {
        return planeNames;
    }

    public void setPlaneNames(Map<String, List<String>> planeNames) {
        this.planeNames = planeNames;
    }


}

