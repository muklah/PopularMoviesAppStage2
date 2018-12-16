package com.example.muklahhn.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Muklah H N on 10/01/2018.
 */

public class TrailerItem implements Parcelable {

    private String id;
    private String name;
    private String key;

    public TrailerItem(String id, String name, String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(name);
        out.writeString(key);
    }

    private TrailerItem(Parcel in) {
        this.id                 = in.readString();
        this.name                 = in.readString();
        this.key                 = in.readString();
    }

    public TrailerItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TrailerItem> CREATOR = new Parcelable.Creator<TrailerItem>() {
        @Override
        public TrailerItem createFromParcel(Parcel in) {
            return new TrailerItem(in);
        }
        @Override
        public TrailerItem[] newArray(int i) {
            return new TrailerItem[i];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

}