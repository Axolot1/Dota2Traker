package com.axolotl.dota2traker.data;

import android.content.ContentResolver;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by axolotl on 16/6/30.
 */
@ContentProvider(authority = DotaProvider.AUTHORITY, database = DotaDataBase.class)
public class DotaProvider {
    public static final String AUTHORITY = "com.axolotl.dota2traker";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String PLAYERS = "players";
        String MATCHES = "matches";
        String FROM_PLAYER = "fromPlayer";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = DotaDataBase.PLAYERS)
    public static class Players {
        @ContentUri(
                path = Path.PLAYERS,
                type = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Path.PLAYERS)
        public static final Uri CONTENT_URI = buildUri(Path.PLAYERS);

        @InexactContentUri(
                name = "PLAYER_ID",
                path = Path.PLAYERS + "/*",
                type = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Path.PLAYERS,
                whereColumn = PlayerColumns.STEAM64,
                pathSegment = 1
        )
        public static Uri withSteamId64(String id64) {
            return buildUri(Path.PLAYERS, id64);
        }
    }

    @TableEndpoint(table = DotaDataBase.MATCHES)
    public static class Matches {
        @ContentUri(
                path = Path.MATCHES,
                type = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Path.MATCHES)
        public static final Uri CONTENT_URI = buildUri(Path.MATCHES);

        @InexactContentUri(
                name = "MATCHES_ID",
                path = Path.MATCHES + "/*",
                type = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Path.MATCHES,
                whereColumn = MatchDetailColumns.MATCH_ID,
                pathSegment = 1
        )
        public static Uri withMatchId(String matchId) {
            return buildUri(Path.MATCHES, matchId);
        }

//        @InexactContentUri(
//                name = "MATCHES_FROM_PLAYER",
//                path = Path.MATCHES + "/" + Path.FROM_PLAYER + "/*",
//                type = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Path.MATCHES,
//                whereColumn = MatchDetailColumns.STEAM_ID,
//                pathSegment = 2
//        )
//        public static Uri withPlayerId(String playerId) {
//            return buildUri(Path.MATCHES, Path.FROM_PLAYER, playerId);
//        }


    }
}
