package ro.marianperca.dogtracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import ro.marianperca.dogtracker.BasicApp;
import ro.marianperca.dogtracker.DataRepository;
import ro.marianperca.dogtracker.db.entity.DogLocationEntity;

public class DogLocationsViewModel extends AndroidViewModel {

    private final DataRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<DogLocationEntity>> mObservableLocations;

    public DogLocationsViewModel(Application application) {
        super(application);

        mObservableLocations = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableLocations.setValue(null);

        mRepository = ((BasicApp) application).getRepository();
        LiveData<List<DogLocationEntity>> locations = mRepository.getDogLocations();

        // observe the changes of the products from the database and forward them
        mObservableLocations.addSource(locations, mObservableLocations::setValue);
    }

    public LiveData<List<DogLocationEntity>> getDogLocations() {
        return mObservableLocations;
    }

    public void insert(DogLocationEntity location) {
        mRepository.insertLocation(location);
    }
}
