
package com.axolotl.dota2traker.retrofit.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class MatchHistoryResults {

    @SerializedName("result")
    @Expose
    private Result result;

    @Generated("org.jsonschema2pojo")
    public static class Result {

        @SerializedName("status")
        @Expose
        private int status;
        @SerializedName("num_results")
        @Expose
        private int numResults;
        @SerializedName("total_results")
        @Expose
        private int totalResults;
        @SerializedName("results_remaining")
        @Expose
        private int resultsRemaining;
        @SerializedName("matches")
        @Expose
        private List<Match> matches = new ArrayList<Match>();

        /**
         *
         * @return
         *     The status
         */
        public int getStatus() {
            return status;
        }

        /**
         *
         * @param status
         *     The status
         */
        public void setStatus(int status) {
            this.status = status;
        }

        /**
         *
         * @return
         *     The numResults
         */
        public int getNumResults() {
            return numResults;
        }

        /**
         *
         * @param numResults
         *     The num_results
         */
        public void setNumResults(int numResults) {
            this.numResults = numResults;
        }

        /**
         *
         * @return
         *     The totalResults
         */
        public int getTotalResults() {
            return totalResults;
        }

        /**
         *
         * @param totalResults
         *     The total_results
         */
        public void setTotalResults(int totalResults) {
            this.totalResults = totalResults;
        }

        /**
         *
         * @return
         *     The resultsRemaining
         */
        public int getResultsRemaining() {
            return resultsRemaining;
        }

        /**
         *
         * @param resultsRemaining
         *     The results_remaining
         */
        public void setResultsRemaining(int resultsRemaining) {
            this.resultsRemaining = resultsRemaining;
        }

        /**
         *
         * @return
         *     The matches
         */
        public List<Match> getMatches() {
            return matches;
        }

        /**
         *
         * @param matches
         *     The matches
         */
        public void setMatches(List<Match> matches) {
            this.matches = matches;
        }

    }


    /**
     * 
     * @return
     *     The result
     */
    public Result getResult() {
        return result;
    }

    /**
     * 
     * @param result
     *     The result
     */
    public void setResult(Result result) {
        this.result = result;
    }


}
