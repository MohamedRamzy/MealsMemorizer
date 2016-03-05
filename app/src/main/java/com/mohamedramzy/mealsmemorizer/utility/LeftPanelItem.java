package com.mohamedramzy.mealsmemorizer.utility;

/**
 * Created by mmahfouz on 3/5/2016.
 */
public class LeftPanelItem {

    private String mTitle;
    private int mIcon;
    public LeftPanelItem(String t,int i){
        this.mTitle = t;
        this.mIcon = i;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int mIcon) {
        this.mIcon = mIcon;
    }

}