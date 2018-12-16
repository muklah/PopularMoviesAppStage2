package com.example.muklahhn.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Muklah H N on 21/04/2018.
 */

public class ReviewItem implements Parcelable {

    private String author;
    private String content;

    public ReviewItem(String author, String content) {
        this.author = author;
        this.content = content;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(author);
        out.writeString(content);
    }

    private ReviewItem(Parcel in) {
        this.author                 = in.readString();
        this.content                 = in.readString();
    }

    public ReviewItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ReviewItem> CREATOR = new Parcelable.Creator<ReviewItem>() {
        @Override
        public ReviewItem createFromParcel(Parcel in) {
            return new ReviewItem(in);
        }
        @Override
        public ReviewItem[] newArray(int i) {
            return new ReviewItem[i];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

}