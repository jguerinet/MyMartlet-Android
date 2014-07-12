package ca.appvelopers.mcgillmobile.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Julien Guerinet
 * Date: 2014-07-11 11:24 PM
 * Copyright (c) 2014 Julien Guerinet. All rights reserved.
 * Represents a place on the map
 */

@JsonIgnoreProperties(ignoreUnknown =  true)
public class Place implements Serializable{
    private static final long serialVersionUID = 1L;

    private String mName;
    private List<PlaceCategory> mCategories;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;
    private String mDetails;

    public Place(@JsonProperty("Name") String name,
                 @JsonProperty("Categories") String[] categories,
                 @JsonProperty("Address") String address,
                 @JsonProperty("Latitude") double latitude,
                 @JsonProperty("Longitude") double longitude,
                 @JsonProperty("Details") String details){
        this.mName = name;
        this.mCategories = PlaceCategory.getCategories(categories);
        this.mAddress = address;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mDetails = details;
    }

    /* GETTERS */
    public String getName(){
        return mName;
    }

    public boolean hasCategory(PlaceCategory category){
        return mCategories.contains(category);
    }

    public String getAddress(){
        return mAddress;
    }

    public double getLatitude(){
        return mLatitude;
    }

    public double getLongitude(){
        return mLongitude;
    }

    public String getDetails(){
        return mDetails;
    }
}
