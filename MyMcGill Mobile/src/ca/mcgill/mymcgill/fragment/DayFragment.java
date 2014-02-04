package ca.mcgill.mymcgill.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ca.mcgill.mymcgill.R;
import ca.mcgill.mymcgill.activity.ScheduleActivity;
import ca.mcgill.mymcgill.object.CourseSched;
import ca.mcgill.mymcgill.object.Day;
import ca.mcgill.mymcgill.util.Constants;

/**
 * Fragment that represents one day in the schedule
 * Author: Julien
 * Date: 01/02/14, 7:10 PM
 */
public class DayFragment extends Fragment{
    private Day mDay;
    private List<CourseSched> mCourses;

    public static DayFragment newInstance(Day day){
        DayFragment fragment = new DayFragment();

        //Put the day in the bundle
        Bundle args = new Bundle();
        args.putSerializable(Constants.DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    //Empty Constructor (Required for Fragments)
    public DayFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mDay = (Day)getArguments().get(Constants.DAY);
        //Get the courses from ScheduleActivity
        mCourses = ((ScheduleActivity)getActivity()).getCoursesForDay(mDay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        TextView dayTitle = (TextView)view.findViewById(R.id.day_title);
        dayTitle.setText(mDay.getDayString(getActivity()));

        //Get the container for the timetable
        LinearLayout timetableContainer = (LinearLayout)view.findViewById(R.id.timetable_container);
        //Get the container for the schedule
        LinearLayout scheduleContainer = (LinearLayout)view.findViewById(R.id.schedule_container);

        //Fill it up
        fillSchedule(inflater, timetableContainer, scheduleContainer);

        return view;
    }

    //Method that fills the schedule based on given data
    public void fillSchedule(LayoutInflater inflater, LinearLayout timetableContainer,
                             LinearLayout scheduleContainer){
        int currentCourseEndTime = 0;

        //Cycle through the hours
        for(int hour = 8; hour < 22; hour++){
            //Cycle through the half hours
            for(int min = 0; min < 31; min+= 30){
                CourseSched currentCourse = null;

                //Start inflating a timetable cell
                View timetableCell = inflater.inflate(R.layout.fragment_day_timetable_cell, null);

                //Put the correct time
                String hours = hour == 12 ? "12" : String.valueOf(hour % 12) ;
                String minutes = min == 0 ? "00" : "30";
                TextView time = (TextView)timetableCell.findViewById(R.id.cell_time);
                time.setText(hours + ":" + minutes);

                boolean am = hour / 12 == 0;
                TextView amView = (TextView)timetableCell.findViewById(R.id.cell_am);
                amView.setText(am ? "A.M." : "P.M.");

                //Add it to the right container
                timetableContainer.addView(timetableCell);

                //Calculate time in minutes
                int timeInMinutes = 60*hour + min;

                if(currentCourseEndTime == 0 || currentCourseEndTime == timeInMinutes){
                    currentCourseEndTime = 0;

                    //Check if there is a course at this time
                    for(CourseSched course : mCourses){
                        if(course.getStartTimeInMinutes() == timeInMinutes){
                            currentCourse = course;
                            currentCourseEndTime = course.getEndTimeInMinutes();
                            break;
                        }
                    }

                    View view;

                    //There is a course at this time
                    if(currentCourse != null){
                        //Inflate the view
                        view = inflater.inflate(R.layout.fragment_day_cell, null);

                        //Set up all of the info

                        //Find out how long this course is in terms of blocks of 30 min
                        int length = ((currentCourse.getEndHour() - currentCourse.getStartHour()) * 60 +
                                (currentCourse.getEndMinute() - currentCourse.getStartMinute())) / 30;

                        //Set the height of the view depending on this height
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                (int) getActivity().getResources().getDimension(R.dimen.cell_30min_height) * length);
                        view.setLayoutParams(lp);
                    }
                    else{
                        //Inflate the empty view
                        view = inflater.inflate(R.layout.fragment_day_cell_empty, null);
                    }

                    scheduleContainer.addView(view);
                }
            }
        }
    }
}