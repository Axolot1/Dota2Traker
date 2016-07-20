package com.axolotl.dota2traker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by axolotl on 16/6/30.
 */
@Database(version = DotaDataBase.VERSION)
public class DotaDataBase {
    public static final int VERSION = 1;

    @Table(PlayerColumns.class)
    public static final String PLAYERS = "players";

    @Table(MatchDetailColumns.class)
    public static final String MATCHES = "matches";

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }
}
