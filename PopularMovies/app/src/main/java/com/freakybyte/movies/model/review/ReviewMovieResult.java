
package com.freakybyte.movies.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewMovieResult {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("results")
    private ArrayList<ReviewMovieModel> results = new ArrayList<>();
    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonProperty("total_results")
    private Integer totalResults;

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
    public ArrayList<ReviewMovieModel> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    @JsonProperty("results")
    public void setResults(ArrayList<ReviewMovieModel> results) {
        this.results = results;
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

}
