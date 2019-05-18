package ro.marianperca.dogtracker.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ro.marianperca.dogtracker.db.entity.DogLocationEntity;

@Dao
public interface DogLocationDao {
    @Query("SELECT * FROM dog_locations")
    LiveData<List<DogLocationEntity>> loadAllLocations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DogLocationEntity location);
}
