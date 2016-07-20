package com.axolotl.dota2traker.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by axolotl on 16/7/4.
 */
public interface MatchDetailColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    @References(table = DotaDataBase.PLAYERS, column = PlayerColumns.STEAM64)
    String STEAM_ID = "steamid";

    @DataType(DataType.Type.TEXT)
    @NotNull
    @Unique(onConflict = ConflictResolutionType.REPLACE)
    String MATCH_ID = "match_id";

    @DataType(DataType.Type.REAL)
    @NotNull
    String START_TIME = "start_time";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String LOBBY_TYPE = "lobby_type";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String RADIANT_WIN = "radiant_win";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String DURATION = "duration";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String GAME_MODE = "game_mode";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String MATCH_SEQ_NUMBER = "match_seq_num";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String RADIANT_SCORE = "radiant_score";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String DIRE_SCORE = "dire_score";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String PLAYERS_MSG = "players_msg";



//    "radiant_win": false,
//            "duration": 2492,
//            "pre_game_duration": 75,
//            "start_time": 1467455502,
//            "match_id": 2476418799,
//            "match_seq_num": 2165114635,
//            "tower_status_radiant": 4,
//            "tower_status_dire": 1958,
//            "barracks_status_radiant": 3,
//            "barracks_status_dire": 63,
//            "cluster": 225,
//            "first_blood_time": 185,
//            "lobby_type": 0,
//            "human_players": 10,
//            "leagueid": 0,
//            "positive_votes": 0,
//            "negative_votes": 0,
//            "game_mode": 1,
//            "flags": 1,
//            "engine": 1,
//            "radiant_score": 38,
//            "dire_score": 38

}
