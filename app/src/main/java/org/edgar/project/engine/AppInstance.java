package org.edgar.project.engine;

import android.app.Application;

import org.edgar.cache.CacheManager;

/**
 * Created by ChangLing on 16/3/29.
 */
public class AppInstance extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        CacheManager.getInstance().initCacheDir();

    }
}
