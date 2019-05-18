package ro.marianperca.dogtracker;

import android.app.Application;

import ro.marianperca.dogtracker.db.AppDatabase;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BasicApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
