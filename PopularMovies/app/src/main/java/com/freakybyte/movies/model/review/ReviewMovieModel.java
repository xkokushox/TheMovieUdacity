
package com.freakybyte.movies.model.review;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewMovieModel implements Parcelable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("author")
    private String author;
    @JsonProperty("content")
    private String content;
    @JsonProperty("url")
    private String url;

    /**
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The author
     */
    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    /**
     * @param author The author
     */
    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return The content
     */
    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }

    public ReviewMovieModel() {
    }

    protected ReviewMovieModel(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<ReviewMovieModel> CREATOR = new Parcelable.Creator<ReviewMovieModel>() {
        @Override
        public ReviewMovieModel createFromParcel(Parcel source) {
            return new ReviewMovieModel(source);
        }

        @Override
        public ReviewMovieModel[] newArray(int size) {
            return new ReviewMovieModel[size];
        }
    };
}
