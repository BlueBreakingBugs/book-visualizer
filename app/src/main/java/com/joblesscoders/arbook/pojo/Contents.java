package com.joblesscoders.arbook.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Contents implements Parcelable {
    private String _id,description,title,thumbnail,link,type;
    private List<String> additional_link;
    private float scale;

    protected Contents(Parcel in) {
        _id = in.readString();
        description = in.readString();
        title = in.readString();
        thumbnail = in.readString();
        link = in.readString();
        type = in.readString();
        additional_link = in.createStringArrayList();
        scale = in.readFloat();
    }

    public static final Creator<Contents> CREATOR = new Creator<Contents>() {
        @Override
        public Contents createFromParcel(Parcel in) {
            return new Contents(in);
        }

        @Override
        public Contents[] newArray(int size) {
            return new Contents[size];
        }
    };

    public String getId() {
        return _id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(description);
        parcel.writeString(title);
        parcel.writeString(thumbnail);
        parcel.writeString(link);
        parcel.writeString(type);
        parcel.writeStringList(additional_link);
        parcel.writeFloat(scale);
    }
}
