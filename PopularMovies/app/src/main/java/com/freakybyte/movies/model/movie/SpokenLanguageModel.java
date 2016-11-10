
package com.freakybyte.movies.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpokenLanguageModel implements Parcelable {

    @JsonProperty("iso_639_1")
    private String iso6391;
    @JsonProperty("name")
    private String name;

    /**
     * @return The iso6391
     */
    @JsonProperty("iso_639_1")
    public String getIso6391() {
        return iso6391;
    }

    /**
     * @param iso6391 The iso_639_1
     */
    @JsonProperty("iso_639_1")
    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    /**
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iso6391);
        dest.writeString(this.name);
    }

    public SpokenLanguageModel() {
    }

    protected SpokenLanguageModel(Parcel in) {
        this.iso6391 = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<SpokenLanguageModel> CREATOR = new Parcelable.Creator<SpokenLanguageModel>() {
        @Override
        public SpokenLanguageModel createFromParcel(Parcel source) {
            return new SpokenLanguageModel(source);
        }

        @Override
        public SpokenLanguageModel[] newArray(int size) {
            return new SpokenLanguageModel[size];
        }
    };
}
