package com.axolotl.dota2traker.mvp;

import com.axolotl.dota2traker.retrofit.model.PlayerSummaries;

import java.util.List;

/**
 * Created by axolotl on 16/6/29.
 */
public interface PlayerCardView {
    void showLoadingProgress();
    void setupData(List<PlayerSummaries> players);
    void noPlayerInfo();
    void addPlayer(List<PlayerSummaries> players);
}
