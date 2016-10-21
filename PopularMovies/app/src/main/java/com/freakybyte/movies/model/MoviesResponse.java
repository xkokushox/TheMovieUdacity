

package com.freakybyte.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public class MoviesResponse implements Parcelable, Cloneable {
    public static final String TAG = "MoviesResponse";

    public static final Creator<MoviesResponse> CREATOR = new Creator<MoviesResponse>() {
        public MoviesResponse createFromParcel(Parcel in) {
            return new MoviesResponse(in);
        }

        public MoviesResponse[] newArray(int size) {
            return new MoviesResponse[size];
        }
    };

    @JsonProperty("page")
    private Integer page;
    @JsonProperty("results")
    private ArrayList<ResultModel> results = new ArrayList<>();
    @JsonProperty("total_results")
    private Integer totalResults;
    @JsonProperty("total_pages")
    private Integer totalPages;

    public MoviesResponse() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public MoviesResponse(Parcel in) {
        page = in.readInt();
        totalResults = in.readInt();
        totalPages = in.readInt();

        results = new ArrayList<>();
        results = in.readArrayList(ResultModel.class.getClassLoader());

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeInt(totalResults);
        dest.writeInt(totalPages);
        dest.writeList(results);
    }

    /**
     * @return The page
     */
    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    @JsonProperty("results")
    public ArrayList<ResultModel> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    @JsonProperty("results")
    public void setResults(ArrayList<ResultModel> results) {
        this.results = results;
    }

    /**
     * @return The totalResults
     */
    @JsonProperty("total_results")
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults The total_results
     */
    @JsonProperty("total_results")
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * @return The totalPages
     */
    @JsonProperty("total_pages")
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages The total_pages
     */
    @JsonProperty("total_pages")
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}