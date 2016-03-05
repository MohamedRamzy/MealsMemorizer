package com.mohamedramzy.mealsmemorizer.model;

/**
 * Created by mmahfouz on 3/5/2016.
 */
public class Category {
    public static final int CAT_OTHER = 0;
    public static final int CAT_FISH = 1;
    public static final int CAT_MEAT = 2;
    public static final int CAT_CHICKEN = 3;
    public static final int CAT_VEGETABLES = 4;

    private int id;
    private String name;
    private String icon;
    public Category (int id, String name, String icon){
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
