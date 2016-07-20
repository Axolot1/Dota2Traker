package com.axolotl.dota2traker;

import android.app.Application;
import android.content.Context;

import com.axolotl.dota2traker.dagger.AppModule;
import com.axolotl.dota2traker.dagger.DaggerMainComponent;
import com.axolotl.dota2traker.dagger.MainComponent;
import com.facebook.stetho.Stetho;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by axolotl on 16/6/27.
 */
public class DotaApp extends Application {

    private MainComponent mMaimComponent;
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        Stetho.initializeWithDefaults(this);
    }

    private void initializeInjector() {
        mMaimComponent = DaggerMainComponent.builder().appModule(new AppModule(this)).build();
    }

    public static MainComponent getNetComponent(Context context) {
        return ((DotaApp) context.getApplicationContext()).mMaimComponent;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.track_app);
        }
        return mTracker;
    }

    public void startTracking(){
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.track_app);
            analytics.enableAutoActivityReports(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }

    }
}
