package com.axolotl.dota2traker.retrofit;

import com.axolotl.dota2traker.BuildConfig;
import com.axolotl.dota2traker.retrofit.model.FriendsListRes;
import com.axolotl.dota2traker.retrofit.model.MatchDetailRes;
import com.axolotl.dota2traker.retrofit.model.MatchHistoryResults;
import com.axolotl.dota2traker.retrofit.model.PlayerSumRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by axolotl on 16/6/27.
 */
public interface DotaApi {
    String BASE_URL = "https://api.steampowered.com/";
    String RELATION = "friend";
    String key = BuildConfig.STEAM_API_KEY;

    @GET("IDOTA2Match_570/GetMatchHistory/V001/")
    Observable<MatchHistoryResults> getMatchHistory(
            @Query("account_id") String id,
            @Query("matches_requested") int num,
            @Query("start_at_match_id") String startMatchId);

    @GET("ISteamUser/GetPlayerSummaries/v0002/")
    Observable<PlayerSumRes> getPlayerSummaries(
            @Query("steamids") String ids);



    @GET("ISteamUser/GetFriendList/v1/")
    Observable<FriendsListRes> getFriendList(
            @Query("steamid") String id,
            @Query("relationship") String relationship);

    @GET("IDOTA2Match_570/GetMatchDetails/V001/")
    Observable<MatchDetailRes> getMatchDetails(
            @Query("match_id") String id);



}
