package ca.mcgill.mymcgill.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan Singzon on 30/01/14.
 *
 * This class will contain information pertaining to each semester such as current program,
 * term credits, term GPA, and full time status
 */
public class Semester implements Serializable{

    private String mSemesterName;
    private String mProgram;
    private String mBachelor;
    private int programYear;
    private int termCredits;
    private double termGPA;
    private boolean fullTime;
    private String courseString;
    private List<Course> courses;


    public Semester(String semesterName, String program, String bachelor, int programYear, int termCredits, double termGPA,
                        boolean fullTime, String courseString) {
        this.mSemesterName = semesterName;
        this.mProgram = program;
        this.mBachelor = bachelor;
        this.termCredits = termCredits;
        this.termGPA = termGPA;
        this.programYear = programYear;
        this.fullTime = fullTime;
        this.courseString = courseString;

        parseCourses(courseString);
    }


    //Takes as input a string of course information and converts it to Course objects
    private void parseCourses(String courseString){

        //TODO: Create parsing code here

        courses = new ArrayList<Course>();
    }

    //Getter for the semester name
    public String getSemesterName(){
        return mSemesterName;
    }

    //Getter for program
    public String getProgram() {
        return mProgram;
    }

    //Getter for the bachelor
    public String getBachelor(){
        return mBachelor;
    }

    //Getter for term credits
    public int getTermCredits() {
        return termCredits;
    }

    //Getter for term GPA
    public double getTermGPA() {
        return termGPA;
    }

    //Getter for program year
    public int getProgramYear() {
        return programYear;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    //Getter for the semester's courses
    public List<Course> getCourses(){
        return courses;
    }
}
