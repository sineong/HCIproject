package com.sineong.testopenweatherapi.DB;

/**
 * Created by SEOYOMI on 6/5/16.
 */

public class Criteria {

    private int id;
    private int inner_max;
    private int bottom_max;
    private int outer_min1;
    private int outer_min2;

    public Criteria(){
    }

    public Criteria(int inner_max, int bottom_max, int outer_min1, int outer_min2){
        this.inner_max = inner_max;
        this.bottom_max = bottom_max;
        this.outer_min1 = outer_min1;
        this.outer_min1 = outer_min2;
    }


    public void setInner_max(int t){
        this.inner_max = t;
    }

    public void setBottom_max(int t){
        this.bottom_max = t;
    }

    public void setOuter_min1(int t){
        this.outer_min1 = t;
    }

    public void setOuter_min2(int t){
        this.outer_min2 = t;
    }



    public int getInner_max(){
        return this.inner_max;
    }

    public int getBottom_max(){
        return this.bottom_max;
    }

    public int getOuter_min1(){
        return this.outer_min1;
    }

    public int getOuter_min2(){
        return this.outer_min2;
    }
}

