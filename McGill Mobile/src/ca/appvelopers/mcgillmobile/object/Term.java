package ca.appvelopers.mcgillmobile.object;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author : Julien
 * Date :  2014-06-08 7:40 PM
 * Copyright (c) 2014 Julien Guerinet. All rights reserved.
 * Represents a season and a year (aka a semester)
 */
public class Term implements Serializable {
    private static final long serialVersionUID = 1L;

    private Season mSeason;
    private int mYear;

    public Term(Season season, int year){
        this.mSeason = season;
        this.mYear = year;
    }

    /* GETTERS */

    /**
     * Get the term's season
     * @return The season
     */
    public Season getSeason(){
        return mSeason;
    }

    /**
     * Get the term's year
     * @return The year
     */
    public int getYear(){
        return mYear;
    }

    /* HELPERS */
    @Override
    public boolean equals(Object object){
        if(!(object instanceof Term)){
            return false;
        }

        Term term = (Term)object;
        return mSeason == term.getSeason() && mYear == term.getYear();
    }

    /**
     * Check if the current term is after the given term
     * @param term The semester to compare
     * @return True if the current term is after, false, otherwise
     */
    public boolean isAfter(Term term){
        //Year after
        if(mYear > term.getYear()){
            return true;
        }
        //Year Before
        else if(mYear < term.getYear()){
            return false;
        }
        //Same year
        else{
            //Check the semesters
            return Integer.valueOf(mSeason.getSeasonNumber()) >
                    Integer.valueOf(term.getSeason().getSeasonNumber());
        }
    }

    public String toString(Context context){
        return mSeason.toString(context) + " " + mYear;
    }

    /**
     * Parse a term from a String
     * @param termString The term String
     * @return The parsed term
     */
    public static Term parseTerm(String termString){
        String[] termParts = termString.split(" ");
        return new Term(Season.findSeason(termParts[0]), Integer.valueOf(termParts[1]));
    }

    public static Term dateConverter(Date date) {
        SimpleDateFormat dateConverter = new SimpleDateFormat("dd-MM-yyyy");
        String[] convertedDate = dateConverter.format(date).split("-");
        int month = Integer.parseInt(convertedDate[1]);
        int year = Integer.parseInt(convertedDate[2]);
        if (month >= 9 && month <= 12) {
            Term term = new Term(Season.FALL, year);
            return term;
        } else if (month >= 1 && month <= 4) {
            Term term = new Term(Season.WINTER, year);
            return term;
        } else {
            Term term = new Term(Season.SUMMER, year);
            return term;
        }
    }
}