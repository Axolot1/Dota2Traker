package com.axolotl.dota2traker.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.axolotl.dota2traker.R;
import com.axolotl.dota2traker.retrofit.model.DotaItems;
import com.axolotl.dota2traker.retrofit.model.Item;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by axolotl on 16/4/6.
 */
@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    HashMap<Integer, String> providesDotaItems(Application application) {
        InputStream is = application.getResources().openRawResource(R.raw.items);
        Gson gson = new Gson();
        DotaItems dotaItems = gson.fromJson(new InputStreamReader(is), DotaItems.class);
        List<Item> items = dotaItems.getItems();
        HashMap<Integer, String> result = new HashMap<>();
        result.put(0, "emptyitembg");
        for (Item item : items) {
            if (item.isRecipe()) {
                result.put(item.getId(), "recipe");
            }
            result.put(item.getId(), item.getName());
        }
        return result;
    }
}
