
package com.freakybyte.movies.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductionCompanyModel implements Parcelable {

    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private Integer id;

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

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeValue(this.id);
    }

    public ProductionCompanyModel() {
    }

    protected ProductionCompanyModel(Parcel in) {
        this.name = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProductionCompanyModel> CREATOR = new Parcelable.Creator<ProductionCompanyModel>() {
        @Override
        public ProductionCompanyModel createFromParcel(Parcel source) {
            return new ProductionCompanyModel(source);
        }

        @Override
        public ProductionCompanyModel[] newArray(int size) {
            return new ProductionCompanyModel[size];
        }
    };
}
