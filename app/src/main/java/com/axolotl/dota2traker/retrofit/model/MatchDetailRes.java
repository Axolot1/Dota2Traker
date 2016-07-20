package com.axolotl.dota2traker.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

/**
 * Created by axolotl on 16/6/27.
 */
public class MatchDetailRes {
    @SerializedName("result")
    @Expose
    private DetailResult result;

    public DetailResult getResult() {
        return result;
    }

    public void setResult(DetailResult result) {
        this.result = result;
    }

    public static class DetailResult {
        @SerializedName("players")
        @Expose
        public List<PlayerMatchDetail> players = new ArrayList<>();
        @SerializedName("radiant_win")
        @Expose
        public boolean radiantWin;
        @SerializedName("duration")
        @Expose
        public int duration;
        @SerializedName("pre_game_duration")
        @Expose
        public int preGameDuration;
        @SerializedName("start_time")
        @Expose
        public long startTime;
        @SerializedName("match_id")
        @Expose
        public long matchId;
        @SerializedName("match_seq_num")
        @Expose
        public long matchSeqNum;
        @SerializedName("tower_status_radiant")
        @Expose
        public int towerStatusRadiant;
        @SerializedName("tower_status_dire")
        @Expose
        public int towerStatusDire;
        @SerializedName("barracks_status_radiant")
        @Expose
        public int barracksStatusRadiant;
        @SerializedName("barracks_status_dire")
        @Expose
        public int barracksStatusDire;
        @SerializedName("cluster")
        @Expose
        public long cluster;
        @SerializedName("first_blood_time")
        @Expose
        public long firstBloodTime;
        @SerializedName("lobby_type")
        @Expose
        public int lobbyType;
        @SerializedName("human_players")
        @Expose
        public int humanPlayers;
        @SerializedName("leagueid")
        @Expose
        public int leagueid;
        @SerializedName("positive_votes")
        @Expose
        public int positiveVotes;
        @SerializedName("negative_votes")
        @Expose
        public int negativeVotes;
        @SerializedName("game_mode")
        @Expose
        public int gameMode;
        @SerializedName("flags")
        @Expose
        public int flags;
        @SerializedName("engine")
        @Expose
        public int engine;
        @SerializedName("radiant_score")
        @Expose
        public int radiantScore;
        @SerializedName("dire_score")
        @Expose
        public int direScore;
    }

    public static class PlayerMatchDetail {
        @SerializedName("account_id")
        @Expose
        public String accountId;
        @SerializedName("player_slot")
        @Expose
        public int playerSlot;
        @SerializedName("hero_id")
        @Expose
        public int heroId;
        @SerializedName("item_0")
        @Expose
        public int item0;
        @SerializedName("item_1")
        @Expose
        public int item1;
        @SerializedName("item_2")
        @Expose
        public int item2;
        @SerializedName("item_3")
        @Expose
        public int item3;
        @SerializedName("item_4")
        @Expose
        public int item4;
        @SerializedName("item_5")
        @Expose
        public int item5;
        @SerializedName("kills")
        @Expose
        public int kills;
        @SerializedName("deaths")
        @Expose
        public int deaths;
        @SerializedName("assists")
        @Expose
        public int assists;
        @SerializedName("leaver_status")
        @Expose
        public int leaverStatus;
        @SerializedName("last_hits")
        @Expose
        public int lastHits;
        @SerializedName("denies")
        @Expose
        public int denies;
        @SerializedName("gold_per_min")
        @Expose
        public int goldPerMin;
        @SerializedName("xp_per_min")
        @Expose
        public int xpPerMin;
        @SerializedName("level")
        @Expose
        public int level;
        @SerializedName("gold")
        @Expose
        public int gold;
        @SerializedName("gold_spent")
        @Expose
        public int goldSpent;
        @SerializedName("hero_damage")
        @Expose
        public long heroDamage;
        @SerializedName("tower_damage")
        @Expose
        public int towerDamage;
        @SerializedName("hero_healing")
        @Expose
        public int heroHealing;
        @SerializedName("ability_upgrades")
        @Expose
        public List<AbilityUpgrade> abilityUpgrades = new ArrayList<AbilityUpgrade>();
    }

    @Generated("org.jsonschema2pojo")
    public static class AbilityUpgrade {

        @SerializedName("ability")
        @Expose
        public int ability;
        @SerializedName("time")
        @Expose
        public int time;
        @SerializedName("level")
        @Expose
        public int level;

    }

}
