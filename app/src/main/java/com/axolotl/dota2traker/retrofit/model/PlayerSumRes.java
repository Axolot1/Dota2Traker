package com.axolotl.dota2traker.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

/**
 * Created by axolotl on 16/6/27.
 */
public class PlayerSumRes {

    @SerializedName("response")
    @Expose
    private Response response;

    /**
     *
     * @return
     * The response
     */
    public Response getResponse() {
        return response;
    }

    /**
     *
     * @param response
     * The response
     */
    public void setResponse(Response response) {
        this.response = response;
    }

    @Generated("org.jsonschema2pojo")
    public static class Response {

        @SerializedName("players")
        @Expose
        private List<PlayerSummaries> players = new ArrayList<PlayerSummaries>();

        /**
         *
         * @return
         * The players
         */
        public List<PlayerSummaries> getPlayers() {
            return players;
        }

        /**
         *
         * @param players
         * The players
         */
        public void setPlayers(List<PlayerSummaries> players) {
            this.players = players;
        }

    }
}
