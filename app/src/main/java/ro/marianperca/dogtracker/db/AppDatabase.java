package ro.marianperca.dogtracker.db;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ro.marianperca.dogtracker.db.converter.DateConverter;
import ro.marianperca.dogtracker.db.dao.DogLocationDao;
import ro.marianperca.dogtracker.db.entity.DogLocationEntity;

@Database(entities = {DogLocationEntity.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "dog_tracker";

    private static AppDatabase dbInstance;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context) {
        if (dbInstance == null) {
            synchronized (AppDatabase.class) {
                if (dbInstance == null) {
                    dbInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME).build();

                    dbInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return dbInstance;
    }

    public abstract DogLocationDao dogLocationDao();

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
