package com.axolotl.dota2traker.event;

/**
 * Created by axolotl on 16/7/7.
 */
public class LoadedEvent {
    private String steamId64;
    private String imgUrl;
    private String name;


    public LoadedEvent() {
    }

    public String getSteamId64() {
        return steamId64;
    }

    public void setSteamId64(String steamId64) {
        this.steamId64 = steamId64;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
