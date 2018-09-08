package com.example.letic.cinqproject.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by letic on 07/09/2018.
 */

public class ItemList {

    @SerializedName("thumbnailUrl")
    private String photo;

    @SerializedName("title")
    private String title;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class ItemListResult {
        private List<ItemList> results;

        public List<ItemList> getResults() {
            return results;
        }
    }
}
