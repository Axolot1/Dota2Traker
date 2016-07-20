package com.axolotl.dota2traker.mvp;

import com.axolotl.dota2traker.retrofit.DotaApi;
import com.axolotl.dota2traker.retrofit.model.PlayerSumRes;
import com.axolotl.dota2traker.retrofit.model.PlayerSummaries;
import com.axolotl.dota2traker.utils.DotaUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by axolotl on 16/6/29.
 */
public class PlayerCardPresenter {
    DotaApi mDataApi;

    PlayerCardView mView;

    public PlayerCardPresenter(DotaApi mDataApi, PlayerCardView mView) {
        this.mDataApi = mDataApi;
        this.mView = mView;
    }

    public void loadDota() {

    }

    public void addPlayer(List<String> steam32s) {
        List<String> steams64 = new ArrayList<>();
        for (String id32 : steam32s) {
            steams64.add(DotaUtil.get64Id(Long.parseLong(id32)));
        }
        if (steams64.size() == 0) {
            return;
        }
//        mDataApi.getPlayerSummaries(steams64).enqueue(new Callback<PlayerSumRes>() {
//            @Override
//            public void onResponse(Call<PlayerSumRes> call, Response<PlayerSumRes> response) {
//                if(response.body() == null || response.body().getResponse() == null
//                        || response.body().getResponse().getPlayers() == null){
//                    mView.noPlayerInfo();
//                    return ;
//                }
//                List<PlayerSummaries> players = response.body().getResponse().getPlayers();
//                mView.addPlayer(players);
//            }
//
//            @Override
//            public void onFailure(Call<PlayerSumRes> call, Throwable t) {
//
//            }
//        });
    }
}
