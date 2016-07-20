package com.axolotl.dota2traker.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by axolotl on 16/6/30.
 */

public interface PlayerColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    @Unique(onConflict = ConflictResolutionType.REPLACE)
    String STEAM64 = "steamid";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String NAME = "personaname";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String LAST_LOGOFF = "lastlogoff";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String PROFILE_URL = "profileurl";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String AVATAR_MEDIUM = "avatarmedium";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String AVATAR_FULL = "avatarfull";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String TIME_CREATED = "timecreated";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String PERSONA_STATE = "personastate";
}
