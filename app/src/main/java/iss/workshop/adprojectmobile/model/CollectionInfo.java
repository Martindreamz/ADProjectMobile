package iss.workshop.adprojectmobile.model;

public class CollectionInfo {

    private int id;
    private int clerkId;
    private String collectionTime;
    private String collectionPoint;
    private String lat;
    private String longi;

    public CollectionInfo(int id, int clerkId, String collectionTime, String collectionPoint, String lat, String longi) {
        this.id = id;
        this.clerkId = clerkId;
        this.collectionTime = collectionTime;
        this.collectionPoint = collectionPoint;
        this.lat = lat;
        this.longi = longi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getCollectionPoint() {
        return collectionPoint;
    }

    public void setCollectionPoint(String collectionPoint) {
        this.collectionPoint = collectionPoint;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }
}
