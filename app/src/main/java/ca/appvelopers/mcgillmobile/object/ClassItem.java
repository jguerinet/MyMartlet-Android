package ca.appvelopers.mcgillmobile.object;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.List;

import ca.appvelopers.mcgillmobile.R;
import ca.appvelopers.mcgillmobile.util.Help;

/**
 * CourseClass
 * @author Quang
 * 
 */
public class ClassItem implements Serializable{
    private static final long serialVersionUID = 1L;

    private Term mTerm;
    private String mCourseCode;
    private String mCourseSubject;
    private String mCourseNumber;
    private String mCourseTitle;
    private int mCRN;
    private String mSection;
    private LocalTime mStartTime, mEndTime, mActualStartTime, mActualEndTime;
    private List<Day> mDays;
    private String mSectionType;
    private String mLocation;
    private String mInstructor;
    private int mCapacity;
    private int mSeatsAvailable;
    private int mSeatsRemaining;
    private int mWaitlistCapacity;
    private int mWaitlistAvailable;
    private int mWaitlistRemaining;
    private double mCredits;
    private String mDates;
    private DateTime mStartDate;
    private DateTime mEndDate;

    /**
     * Constructor for the user's already registered classes
     * @param term The term that this class is for
     * @param courseCode The full course code
     * @param courseSubject The course subject
     * @param courseNumber The course number
     * @param courseTitle The course title
     * @param crn The course CRN
     * @param section The course section
     * @param startHour The course's starting hour
     * @param startMinute The course's starting minute
     * @param endHour The course's ending hour
     * @param endMinute The course's ending minute
     * @param days The days this course is on
     * @param sectionType The section type
     * @param location The course location
     * @param instructor The course instructor
     * @param credits The number of credits
     * @param startDate The starting date for this course
     * @param endDate The ending date for this course
     */
    public ClassItem(Term term, String courseCode, String courseSubject, String courseNumber, String courseTitle, int crn,
                     String section, int startHour, int startMinute, int endHour, int endMinute,
                     List<Day> days, String sectionType, String location, String instructor, double credits,
                     String dates, DateTime startDate, DateTime endDate){
        this.mTerm = term;
        this.mCourseCode = courseCode;
        this.mCourseSubject = courseSubject;
        this.mCourseNumber = courseNumber;
        this.mCourseTitle = courseTitle;
        this.mCRN = crn;
        this.mSection = section;
        this.mActualStartTime = new LocalTime(startHour, startMinute);
        //Remove 5 minutes to the start to get round numbers
        int newStartMin = (startMinute - 5) % 60;
        if(newStartMin < 0){
            newStartMin = startMinute;
        }
        this.mStartTime = new LocalTime(startHour, newStartMin);this.mActualEndTime = new LocalTime(endHour, endMinute);
        //Add 5 minutes to the end to get round numbers, increment the hour if the minutes get set to 0s
        int endM = (endMinute + 5) % 60;
        int endH = endHour;
        if(endM == 0){
            endH ++;
        }
        this.mEndTime = new LocalTime(endH, endM);
        this.mDays = days;
        this.mSectionType = sectionType;
        this.mLocation = location;
        this.mInstructor = instructor;
        this.mCredits = credits;
        this.mDates = dates;
        this.mStartDate = startDate;
        this.mEndDate = endDate;

        //These fields are not needed (they are used for the search results)
        this.mCapacity = -1;
        this.mSeatsAvailable = -1;
        this.mSeatsRemaining = -1;
        this.mWaitlistCapacity = -1;
        this.mWaitlistAvailable = -1;
        this.mWaitlistRemaining = -1;
    }

    /**
     * Constructor for course search results
     * @param term The term that this class is for
     * @param courseCode The full course code
     * @param courseSubject The course subject
     * @param courseNumber The course number
     * @param courseTitle The course title
     * @param crn The course CRN
     * @param section The course section
     * @param startHour The course's starting hour
     * @param startMinute The course's starting minute
     * @param endHour The course's ending hour
     * @param endMinute The course's ending minute
     * @param days The days this course is on
     * @param sectionType The section type
     * @param location The course location
     * @param instructor The course instructor
     * @param credits The number of credits
     * @param capacity The course capacity
     * @param seatsAvailable The number of seats available
     * @param seatsRemaining The number of seats remaining
     * @param waitlistCapacity The waitlist capacity
     * @param waitlistAvailable The number of seats available on waitlist
     * @param waitlistRemaining The number of seats remaining on waitlist
     * @param dates The dates this course is for
     */
    public ClassItem(Term term, String courseCode, String courseSubject, String courseNumber, String courseTitle,
                     int crn, String section, int startHour, int startMinute, int endHour, int endMinute,
                     List<Day> days, String sectionType, String location, String instructor, int capacity,
                     int seatsAvailable, int seatsRemaining, int waitlistCapacity, int waitlistAvailable,
                     int waitlistRemaining, double credits, String dates){
        this.mTerm = term;
        this.mCourseCode = courseCode;
        this.mCourseSubject = courseSubject;
        this.mCourseNumber = courseNumber;
        this.mCourseTitle = courseTitle;
        this.mCRN = crn;
        this.mSection = section;
        this.mActualStartTime = new LocalTime(startHour, startMinute);
        //Remove 5 minutes to the start to get round numbers
        int newStartMin = (startMinute - 5) % 60;
        if(newStartMin < 0){
            newStartMin = startMinute;
        }
        this.mStartTime = new LocalTime(startHour, newStartMin);this.mActualEndTime = new LocalTime(endHour, endMinute);
        //Add 5 minutes to the end to get round numbers, increment the hour if the minutes get set to 0s
        int endM = (endMinute + 5) % 60;
        int endH = endHour;
        if(endM == 0){
            endH ++;
        }
        this.mEndTime = new LocalTime(endH, endM);
        this.mDays = days;
        this.mSectionType = sectionType;
        this.mLocation = location;
        this.mInstructor = instructor;
        this.mCapacity = capacity;
        this.mSeatsAvailable = seatsAvailable;
        this.mSeatsRemaining = seatsRemaining;
        this.mWaitlistCapacity = waitlistCapacity;
        this.mWaitlistAvailable = waitlistAvailable;
        this.mWaitlistRemaining = waitlistRemaining;
        this.mCredits = credits;
        this.mDates = dates;

        /* These fields are not used (they are used for the user's courses) */
        this.mStartDate = null;
        this.mEndDate = null;
    }
	/* GETTERS */

    /**
     * Get the course term
     * @return The course term
     */
    public Term getTerm(){
        return mTerm;
    }

    /**
     * Get the course code
     * @return The course code
     */
    public String getCourseCode(){
        return mCourseCode;
    }

    /**
     * Get the course subject
     * @return The course subject
     */
    public String getCourseSubject(){
        return mCourseSubject;
    }

    /**
     * Get the course number
     * @return The course number
     */
    public String getCourseNumber(){
        return mCourseNumber;
    }

    /**
     * Get the course title
     * @return The course title
     */
    public String getCourseTitle() {
        return mCourseTitle;
    }

    /**
     * Get the course CRN
     * @return The course CRN
     */
    public int getCRN(){
        return mCRN;
    }

    /**
     * Get the Section the user is in
     * @return The course section
     */
    public String getSection(){
        return mSection;
    }

    /**
     * Get the start time of the course (rounded off to the nearest half hour)
     * @return The course start time
     */
    public LocalTime getStartTime(){
        return mStartTime;
    }

    /**
     * Get the actual start time of the course
     *
     * @return The actual start time
     */
    public LocalTime getActualStartTime(){
        return this.mActualStartTime;
    }

    /**
     * Get the end time of the course (rounded off the the nearest half hour)
     * @return The course end time
     */
    public LocalTime getEndTime(){
        return mEndTime;
    }

    /**
     * Get the actual end time of the course
     *
     * @return The actual end time
     */
    public LocalTime getActualEndTime(){
        return this.mActualEndTime;
    }

    /**
     * Get the days this course is on
     * @return The course days
     */
    public List<Day> getDays(){
        return mDays;
    }

    /**
     * Get the course section type
     * @return The course section type
     */
    public String getSectionType(){
        return mSectionType;
    }

    /**
     * Get the course's location
     * @return The course's location
     */
    public String getLocation(){
        return mLocation;
    }

    /**
     * Get the instructor for this course
     * @return Return the course's instructor
     */
    public String getInstructor(){
        return mInstructor;
    }

    /**
     * Get the course credits
     * @return The course credits
     */
    public double getCredits(){
        return mCredits;
    }

    /**
     * Get the dates this course is on
     * @return The course dates
     */
    public String getDates(){
        return mDates;
    }

    /**
     * Get the number of spots remaining
     * @return The number of spots remaining
     */
    public int getSeatsRemaining(){
        return mSeatsRemaining;
    }

    /**
     * Get the number of waitlist spots remaining
     * @return The number of waitlist spots remaining
     */
    public int getWaitlistRemaining(){
        return mWaitlistRemaining;
    }

    /**
     * Get the starting date
     * @return The starting date
     */
    public DateTime getStartDate(){
        return mStartDate;
    }

    /**
     * Get the ending date
     *
     * @return The ending date
     */
    public DateTime getEndDate(){
        return this.mEndDate;
    }

    /* SETTERS */

    /**
     * Set the start time of the course (rounded off to the nearest half hour)
     */
    public void setStartTime(LocalTime time){
        this.mStartTime = time;
    }

    /**
     * Set the end time of the course (rounded off the the nearest half hour)
     */
    public void setEndTime(LocalTime time){
        this.mEndTime = time;
    }

    /**
     * Set the days this course is on
     */
    public void setDays(List<Day> days){
        this.mDays = days;
    }

    /**
     * Set the course's location
     */
    public void setLocation(String location){
        this.mLocation = location;
    }

    /**
     * Set the instructor for this course
     */
    public void setInstructor(String instructor){
        this.mInstructor = instructor;
    }

    /**
     * Set the dates this course is on
     */
    public void setDates(String dates){
        this.mDates = dates;
    }

    /**
     * Set the number of spots remaining
     */
    public void setSeatsRemaining(int seatsRemaining){
        this.mSeatsRemaining = seatsRemaining;
    }

    /**
     * Set the number of waitlist spots remaining
     */
    public void setWaitlistRemaining(int waitlistRemaining){
        this.mWaitlistRemaining = waitlistRemaining;
    }

    /* HELPER METHODS */
    /**
     * Checks that the course is for the given date
     * @param date The given date
     * @return True if it is, false otherwise
     */
    public boolean isForDate(DateTime date){
        return !date.isBefore(mStartDate) && !date.isAfter(mEndDate);
    }

    /**
     * Get the String representing the class time
     * @return The class time in String format
     */
    public String getTimeString(Context context){
        //No time associated, therefore no time string
        if(mStartTime.getHourOfDay() == 0 && mStartTime.getMinuteOfHour() == 0){
            return "";
        }
        return context.getResources().getString(R.string.course_time,
                Help.getLongTimeString(context, mActualStartTime.getHourOfDay(), mActualStartTime.getMinuteOfHour()),
                Help.getLongTimeString(context, mActualEndTime.getHourOfDay(), mActualEndTime.getMinuteOfHour()));
    }

    /**
     * Checks to see if two classes are equal
     * @param object The course to check
     * @return True if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object object){
        if(!(object instanceof ClassItem)){
            return false;
        }
        ClassItem classItem = (ClassItem)object;

        //Check if they have the same season, year, and CRN
        return this.mCRN == classItem.mCRN && this.mTerm.equals(classItem.getTerm());
    }
}