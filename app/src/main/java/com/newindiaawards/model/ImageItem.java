package com.newindiaawards.model;

import java.io.Serializable;

/**
 * Created by Mahesh on 21/11/17.
 */

public class ImageItem implements Serializable {

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private String imageString;
    private boolean isSelected;
}
