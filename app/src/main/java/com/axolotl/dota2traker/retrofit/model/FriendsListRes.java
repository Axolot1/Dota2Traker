package com.axolotl.dota2traker.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

/**
 * Created by axolotl on 16/6/27.
 */
public class FriendsListRes {
    @SerializedName("friendslist")
    @Expose
    private Friendslist friendslist;

    /**
     *
     * @return
     * The friendslist
     */
    public Friendslist getFriendslist() {
        return friendslist;
    }

    /**
     *
     * @param friendslist
     * The friendslist
     */
    public void setFriendslist(Friendslist friendslist) {
        this.friendslist = friendslist;
    }

    @Generated("org.jsonschema2pojo")
    public static class Friendslist {

        @SerializedName("friends")
        @Expose
        private List<Friend> friends = new ArrayList<Friend>();

        /**
         *
         * @return
         * The friends
         */
        public List<Friend> getFriends() {
            return friends;
        }

        /**
         *
         * @param friends
         * The friends
         */
        public void setFriends(List<Friend> friends) {
            this.friends = friends;
        }

    }

    @Generated("org.jsonschema2pojo")
    public static class Friend {

        @SerializedName("steamid")
        @Expose
        private String steamid;
        @SerializedName("relationship")
        @Expose
        private String relationship;
        @SerializedName("friend_since")
        @Expose
        private int friendSince;

        /**
         *
         * @return
         * The steamid
         */
        public String getSteamid() {
            return steamid;
        }

        /**
         *
         * @param steamid
         * The steamid
         */
        public void setSteamid(String steamid) {
            this.steamid = steamid;
        }

        /**
         *
         * @return
         * The relationship
         */
        public String getRelationship() {
            return relationship;
        }

        /**
         *
         * @param relationship
         * The relationship
         */
        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        /**
         *
         * @return
         * The friendSince
         */
        public int getFriendSince() {
            return friendSince;
        }

        /**
         *
         * @param friendSince
         * The friend_since
         */
        public void setFriendSince(int friendSince) {
            this.friendSince = friendSince;
        }

    }

}
