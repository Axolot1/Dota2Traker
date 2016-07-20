
package com.axolotl.dota2traker.retrofit.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Match {

    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("match_seq_num")
    @Expose
    private long matchSeqNum;
    @SerializedName("start_time")
    @Expose
    private long startTime;
    @SerializedName("lobby_type")
    @Expose
    private int lobbyType;
    @SerializedName("radiant_team_id")
    @Expose
    private int radiantTeamId;
    @SerializedName("dire_team_id")
    @Expose
    private int direTeamId;
    @SerializedName("players")
    @Expose
    private List<Player> players = new ArrayList<Player>();

    @Generated("org.jsonschema2pojo")
    public class Player {

        @SerializedName("account_id")
        @Expose
        private long accountId;
        @SerializedName("player_slot")
        @Expose
        private int playerSlot;
        @SerializedName("hero_id")
        @Expose
        private int heroId;

        /**
         *
         * @return
         *     The accountId
         */
        public long getAccountId() {
            return accountId;
        }

        /**
         *
         * @param accountId
         *     The account_id
         */
        public void setAccountId(long accountId) {
            this.accountId = accountId;
        }

        /**
         *
         * @return
         *     The playerSlot
         */
        public int getPlayerSlot() {
            return playerSlot;
        }

        /**
         *
         * @param playerSlot
         *     The player_slot
         */
        public void setPlayerSlot(int playerSlot) {
            this.playerSlot = playerSlot;
        }

        /**
         *
         * @return
         *     The heroId
         */
        public int getHeroId() {
            return heroId;
        }

        /**
         *
         * @param heroId
         *     The hero_id
         */
        public void setHeroId(int heroId) {
            this.heroId = heroId;
        }

    }

    /**
     * 
     * @return
     *     The matchId
     */
    public String getMatchId() {
        return matchId;
    }

    /**
     * 
     * @param matchId
     *     The match_id
     */
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    /**
     * 
     * @return
     *     The matchSeqNum
     */
    public long getMatchSeqNum() {
        return matchSeqNum;
    }

    /**
     * 
     * @param matchSeqNum
     *     The match_seq_num
     */
    public void setMatchSeqNum(long matchSeqNum) {
        this.matchSeqNum = matchSeqNum;
    }

    /**
     * 
     * @return
     *     The startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The start_time
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     * @return
     *     The lobbyType
     */
    public int getLobbyType() {
        return lobbyType;
    }

    /**
     * 
     * @param lobbyType
     *     The lobby_type
     */
    public void setLobbyType(int lobbyType) {
        this.lobbyType = lobbyType;
    }

    /**
     * 
     * @return
     *     The radiantTeamId
     */
    public int getRadiantTeamId() {
        return radiantTeamId;
    }

    /**
     * 
     * @param radiantTeamId
     *     The radiant_team_id
     */
    public void setRadiantTeamId(int radiantTeamId) {
        this.radiantTeamId = radiantTeamId;
    }

    /**
     * 
     * @return
     *     The direTeamId
     */
    public int getDireTeamId() {
        return direTeamId;
    }

    /**
     * 
     * @param direTeamId
     *     The dire_team_id
     */
    public void setDireTeamId(int direTeamId) {
        this.direTeamId = direTeamId;
    }

    /**
     * 
     * @return
     *     The players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * 
     * @param players
     *     The players
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}
