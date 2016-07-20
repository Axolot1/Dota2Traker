package com.axolotl.dota2traker.adapter;

/**
 * Created by axolotl on 16/7/10.
 */
public class PlayerInMatch {
    private String steamID64;
    private int heroAvatar;
    private String playerName;
    private String playerAvatar;
    private String inBattle;
    private String damage;
    private int item0;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private String kda;

    public void setItem(int i0, int i1, int i2, int i3, int i4, int i5){
        item0 = i0;
        item1 = i1;
        item2 = i2;
        item3 = i3;
        item4 = i4;
        item5 = i5;
    }

    public String getSteamID64() {
        return steamID64;
    }

    public void setSteamID64(String steamID64) {
        this.steamID64 = steamID64;
    }

    public int getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(int heroAvatar) {
        this.heroAvatar = heroAvatar;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerAvatar() {
        return playerAvatar;
    }

    public void setPlayerAvatar(String playerAvatar) {
        this.playerAvatar = playerAvatar;
    }

    public String getInBattle() {
        return inBattle;
    }

    public void setInBattle(String inBattle) {
        this.inBattle = inBattle;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public int getItem0() {
        return item0;
    }

    public void setItem0(int item0) {
        this.item0 = item0;
    }

    public int getItem1() {
        return item1;
    }

    public void setItem1(int item1) {
        this.item1 = item1;
    }

    public int getItem2() {
        return item2;
    }

    public void setItem2(int item2) {
        this.item2 = item2;
    }

    public int getItem3() {
        return item3;
    }

    public void setItem3(int item3) {
        this.item3 = item3;
    }

    public int getItem4() {
        return item4;
    }

    public void setItem4(int item4) {
        this.item4 = item4;
    }

    public int getItem5() {
        return item5;
    }

    public void setItem5(int item5) {
        this.item5 = item5;
    }

    public String getKda() {
        return kda;
    }

    public void setKda(String kda) {
        this.kda = kda;
    }
}
