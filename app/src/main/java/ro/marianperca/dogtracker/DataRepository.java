package ro.marianperca.dogtracker;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import ro.marianperca.dogtracker.db.AppDatabase;
import ro.marianperca.dogtracker.db.dao.DogLocationDao;
import ro.marianperca.dogtracker.db.entity.DogLocationEntity;

/**
 * Repository handling the work with locations
 */
public class DataRepository {

    private static DataRepository sInstance;
    private final AppDatabase mDatabase;
    private MediatorLiveData<List<DogLocationEntity>> mObservableLocations;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableLocations = new MediatorLiveData<>();

        mObservableLocations.addSource(mDatabase.dogLocationDao().loadAllLocations(),
                entries -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableLocations.postValue(entries);
                    }
                });
    }

    static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of locations from the database and get notified when the data changes.
     */
    public LiveData<List<DogLocationEntity>> getDogLocations() {
        return mObservableLocations;
    }

    public void insertLocation(DogLocationEntity location) {
        new InsertAsyncTask(mDatabase.dogLocationDao()).execute(location);
    }

    private static class InsertAsyncTask extends AsyncTask<DogLocationEntity, Void, Void> {
        private DogLocationDao mDogLocationDao;

        InsertAsyncTask(DogLocationDao dogLocationDao) {
            mDogLocationDao = dogLocationDao;
        }

        @Override
        protected Void doInBackground(DogLocationEntity... locations) {
            mDogLocationDao.insert(locations[0]);
            return null;
        }
    }
}
