package ro.marianperca.dogtracker.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "dog_locations")
public class DogLocationEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double latitude;
    private double longitude;
    private String address;
    private Date date;

    public DogLocationEntity() {
    }

    public DogLocationEntity(double latitude, double longitude, String address, Date date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DogLocationEntity{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", date=" + date +
                '}';
    }
}
