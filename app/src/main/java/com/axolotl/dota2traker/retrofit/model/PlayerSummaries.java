package com.axolotl.dota2traker.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by axolotl on 16/6/27.
 */
public class PlayerSummaries {
    @SerializedName("steamid")
    @Expose
    private String steamid;

    @SerializedName("communityvisibilitystate")
    @Expose
    private int communityvisibilitystate;

    @SerializedName("profilestate")
    @Expose
    private int profilestate;

    @SerializedName("personaname")
    @Expose
    private String personaname;

    @SerializedName("lastlogoff")
    @Expose
    private int lastlogoff;

    @SerializedName("profileurl")
    @Expose
    private String profileurl;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("avatarmedium")
    @Expose
    private String avatarmedium;

    @SerializedName("avatarfull")
    @Expose
    private String avatarfull;

    @SerializedName("personastate")
    @Expose
    private int personastate;

    @SerializedName("primaryclanid")
    @Expose
    private String primaryclanid;

    @SerializedName("timecreated")
    @Expose
    private int timecreated;

    @SerializedName("personastateflags")
    @Expose
    private int personastateflags;

    public String getSteamid() {
        return steamid;
    }

    public void setSteamid(String steamid) {
        this.steamid = steamid;
    }

    public int getCommunityvisibilitystate() {
        return communityvisibilitystate;
    }

    public void setCommunityvisibilitystate(int communityvisibilitystate) {
        this.communityvisibilitystate = communityvisibilitystate;
    }

    public int getProfilestate() {
        return profilestate;
    }

    public void setProfilestate(int profilestate) {
        this.profilestate = profilestate;
    }

    public String getPersonaname() {
        return personaname;
    }

    public void setPersonaname(String personaname) {
        this.personaname = personaname;
    }

    public int getLastlogoff() {
        return lastlogoff;
    }

    public void setLastlogoff(int lastlogoff) {
        this.lastlogoff = lastlogoff;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarmedium() {
        return avatarmedium;
    }

    public void setAvatarmedium(String avatarmedium) {
        this.avatarmedium = avatarmedium;
    }

    public String getAvatarfull() {
        return avatarfull;
    }

    public void setAvatarfull(String avatarfull) {
        this.avatarfull = avatarfull;
    }

    public int getPersonastate() {
        return personastate;
    }

    public void setPersonastate(int personastate) {
        this.personastate = personastate;
    }

    public String getPrimaryclanid() {
        return primaryclanid;
    }

    public void setPrimaryclanid(String primaryclanid) {
        this.primaryclanid = primaryclanid;
    }

    public int getTimecreated() {
        return timecreated;
    }

    public void setTimecreated(int timecreated) {
        this.timecreated = timecreated;
    }

    public int getPersonastateflags() {
        return personastateflags;
    }

    public void setPersonastateflags(int personastateflags) {
        this.personastateflags = personastateflags;
    }
}
