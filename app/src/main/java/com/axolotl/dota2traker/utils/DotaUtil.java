package com.axolotl.dota2traker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ScrollView;

import com.axolotl.dota2traker.R;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by axolotl on 16/6/28.
 */
public class DotaUtil {

    final static String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";


    public static final HashMap<Integer, Integer> HERO = new HashMap<Integer, Integer>() {
        {
            put(1, R.drawable.antimage);
            put(2, R.drawable.axe);
            put(3, R.drawable.bane);
            put(4, R.drawable.bloodseeker);
            put(5, R.drawable.crystal_maiden);
            put(6, R.drawable.drow_ranger);
            put(7, R.drawable.earthshaker);
            put(8, R.drawable.juggernaut);
            put(9, R.drawable.mirana);
            put(10, R.drawable.morphling);
            put(11, R.drawable.nevermore);
            put(12, R.drawable.phantom_lancer);
            put(13, R.drawable.puck);
            put(14, R.drawable.pudge);
            put(15, R.drawable.razor);
            put(16, R.drawable.sand_king);
            put(17, R.drawable.storm_spirit);
            put(18, R.drawable.sven);
            put(19, R.drawable.tiny);
            put(20, R.drawable.vengefulspirit);
            put(21, R.drawable.windrunner);
            put(22, R.drawable.zuus);
            put(23, R.drawable.kunkka);
            put(25, R.drawable.lina);
            put(26, R.drawable.lion);
            put(27, R.drawable.shadow_shaman);
            put(28, R.drawable.slardar);
            put(29, R.drawable.tidehunter);
            put(30, R.drawable.witch_doctor);
            put(31, R.drawable.lich);
            put(32, R.drawable.riki);
            put(33, R.drawable.enigma);
            put(34, R.drawable.tinker);
            put(35, R.drawable.sniper);
            put(36, R.drawable.necrolyte);
            put(37, R.drawable.warlock);
            put(38, R.drawable.beastmaster);
            put(39, R.drawable.queenofpain);
            put(40, R.drawable.venomancer);
            put(41, R.drawable.faceless_void);
            put(42, R.drawable.skeleton_king);
            put(43, R.drawable.death_prophet);
            put(44, R.drawable.phantom_assassin);
            put(45, R.drawable.pugna);
            put(46, R.drawable.templar_assassin);
            put(47, R.drawable.viper);
            put(48, R.drawable.luna);
            put(49, R.drawable.dragon_knight);
            put(50, R.drawable.dazzle);
            put(51, R.drawable.rattletrap);
            put(52, R.drawable.leshrac);
            put(53, R.drawable.furion);
            put(54, R.drawable.life_stealer);
            put(55, R.drawable.dark_seer);
            put(56, R.drawable.clinkz);
            put(57, R.drawable.omniknight);
            put(58, R.drawable.enchantress);
            put(59, R.drawable.huskar);
            put(60, R.drawable.night_stalker);
            put(61, R.drawable.broodmother);
            put(62, R.drawable.bounty_hunter);
            put(63, R.drawable.weaver);
            put(64, R.drawable.jakiro);
            put(65, R.drawable.batrider);
            put(66, R.drawable.chen);
            put(67, R.drawable.spectre);
            put(69, R.drawable.doom_bringer);
            put(68, R.drawable.ancient_apparition);
            put(70, R.drawable.ursa);
            put(71, R.drawable.spirit_breaker);
            put(72, R.drawable.gyrocopter);
            put(73, R.drawable.alchemist);
            put(74, R.drawable.invoker);
            put(75, R.drawable.silencer);
            put(76, R.drawable.obsidian_destroyer);
            put(77, R.drawable.lycan);
            put(78, R.drawable.brewmaster);
            put(79, R.drawable.shadow_demon);
            put(80, R.drawable.lone_druid);
            put(81, R.drawable.chaos_knight);
            put(82, R.drawable.meepo);
            put(83, R.drawable.treant);
            put(84, R.drawable.ogre_magi);
            put(85, R.drawable.undying);
            put(86, R.drawable.rubick);
            put(87, R.drawable.disruptor);
            put(88, R.drawable.nyx_assassin);
            put(89, R.drawable.naga_siren);
            put(90, R.drawable.keeper_of_the_light);
            put(91, R.drawable.wisp);
            put(92, R.drawable.visage);
            put(93, R.drawable.slark);
            put(94, R.drawable.medusa);
            put(95, R.drawable.troll_warlord);
            put(96, R.drawable.centaur);
            put(97, R.drawable.magnataur);
            put(98, R.drawable.shredder);
            put(99, R.drawable.bristleback);
            put(100, R.drawable.tusk);
            put(101, R.drawable.skywrath_mage);
            put(102, R.drawable.abaddon);
            put(103, R.drawable.elder_titan);
            put(104, R.drawable.legion_commander);
            put(105, R.drawable.techies);
            put(106, R.drawable.ember_spirit);
            put(107, R.drawable.earth_spirit);
            put(109, R.drawable.terrorblade);
            put(110, R.drawable.phoenix);
            put(111, R.drawable.oracle);
            put(112, R.drawable.winter_wyvern);
            put(113, R.drawable.arc_warden);
        }
    };


    private DotaUtil() {
    }


    public static String get64Id(long id) {
        long add = 76561197960265728L;
        return String.valueOf(id + add);
    }

    public static String get32Id(long id) {
        long add = 76561197960265728L;
        return String.valueOf(id - add);
    }

//    The user's status
//            0
//    Offline (Also set when the profile is Private)
//    1
//    Online
//    2
//    Busy
//    3
//    Away
//    4
//    Snooze
//    5
//    Looking to trade
//    6
//    Looking to play

    public static String getStatus(int status, Context ctx) {
        switch (status) {
            case 0:
                return ctx.getString(R.string.offline);
            case 1:
                return ctx.getString(R.string.online);
            case 2:
                return ctx.getString(R.string.busy);
            case 3:
                return ctx.getString(R.string.away);
            case 4:
                return ctx.getString(R.string.snooze);
            case 5:
                return ctx.getString(R.string.looktotrade);
            case 6:
                return ctx.getString(R.string.looktoplay);
        }
        return " ";
    }

    public static String getLobby(int lobby, Context ctx) {
        switch (lobby) {
            case 0:
                return ctx.getString(R.string.lobby0);
            case 1:
                return ctx.getString(R.string.lobbby1);
            case 2:
                return ctx.getString(R.string.lobby2);
            case 3:
                return ctx.getString(R.string.lobby3);
            case 4:
                return ctx.getString(R.string.lobby4);
            case 5:
                return ctx.getString(R.string.lobby5);
            case 6:
                return ctx.getString(R.string.lobby6);
            case 7:
                return ctx.getString(R.string.lobby7);
            case 8:
                return ctx.getString(R.string.lobby8);
            default:
                return ctx.getString(R.string.lobby9);
        }
    }

    public static boolean isRadiant(int slot) {
        if (slot < 5) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

//    0 - None
//    1 - All Pick
//    2 - Captain's Mode
//            3 - Random Draft
//    4 - Single Draft
//    5 - All Random
//    6 - Intro
//    7 - Diretide
//    8 - Reverse Captain's Mode
//            9 - The Greeviling
//    10 - Tutorial
//    11 - Mid Only
//    12 - Least Played
//    13 - New Player Pool
//    14 - Compendium Matchmaking
//    16 - Captains Draft


    public static String getGameMode(int i, Context ctx) {
        switch (i) {
            case 1:
                return ctx.getString(R.string.allpick);
            case 2:
                return ctx.getString(R.string.catain_mode);
            case 3:
                return ctx.getString(R.string.random_draft);
            case 4:
                return ctx.getString(R.string.single_draft);
            case 5:
                return ctx.getString(R.string.all_random);
            case 6:
                return ctx.getString(R.string.intro);
            case 7:
                return ctx.getString(R.string.diretide);
            case 8:
                return ctx.getString(R.string.re_cap_mode);
            case 9:
                return ctx.getString(R.string.greeviling);
            case 10:
                return ctx.getString(R.string.tutorial);
            case 11:
                return ctx.getString(R.string.mid_only);
            case 12:
                return ctx.getString(R.string.least_play);
            case 13:
                return ctx.getString(R.string.new_player_pool);
            case 14:
                return ctx.getString(R.string.compendium);
            case 16:
                return ctx.getString(R.string.catains_draft);
            default:
                return ctx.getString(R.string.none);
        }
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Bitmap getScreenShot(NestedScrollView iv) {
        Bitmap bitmap = Bitmap.createBitmap(
                iv.getChildAt(0).getWidth(),
                iv.getChildAt(0).getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        iv.getChildAt(0).draw(c);
        return bitmap;
    }


    public static File store(Bitmap bm, String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            LogUtils.log(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return file;
    }
}



