package com.joblesscoders.pojo;

import java.util.List;

public class Contents {
    private String id,description,title,thumbnail,link,type;
    private List<String> additional_link;
    private float scale;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }

    public List<String> getAdditional_link() {
        return additional_link;
    }

    public float getScale() {
        return scale;
    }
}
