package ca.mcgill.mymcgill.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.mcgill.mymcgill.App;
import ca.mcgill.mymcgill.R;
import ca.mcgill.mymcgill.activity.drawer.DrawerFragmentActivity;
import ca.mcgill.mymcgill.activity.walkthrough.WalkthroughActivity;
import ca.mcgill.mymcgill.fragment.DayFragment;
import ca.mcgill.mymcgill.object.ClassItem;
import ca.mcgill.mymcgill.object.Day;
import ca.mcgill.mymcgill.object.HomePage;
import ca.mcgill.mymcgill.object.Semester;
import ca.mcgill.mymcgill.util.Connection;
import ca.mcgill.mymcgill.util.Constants;
import ca.mcgill.mymcgill.util.DialogHelper;
import ca.mcgill.mymcgill.util.Help;
import ca.mcgill.mymcgill.util.Load;
import ca.mcgill.mymcgill.util.Parser;
import ca.mcgill.mymcgill.util.Save;

/**
 * @author Nhat-Quang Dao
 * Date: 22/01/14, 9:07 PM
 * 
 * This Activity loads the schedule from https://horizon.mcgill.ca/pban1/bwskfshd.P_CrseSchd
 */
public class ScheduleActivity extends DrawerFragmentActivity {
	private List<ClassItem> mClassList;
    private ViewPager mPager;
    private FragmentManager mSupportFragmentManager;
    private Semester mCurrentSemester;
    private boolean mDoubleBackToExit;

    private static final int CHANGE_SEMESTER_CODE = 100;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_schedule);
        super.onCreate(savedInstanceState);

        mCurrentSemester = App.getDefaultSemester();

        mClassList = new ArrayList<ClassItem>();

        //ViewPager stuff
        mSupportFragmentManager = getSupportFragmentManager();
        mPager = (ViewPager)findViewById(R.id.pager);

        //Load the stored info
        loadInfo();

        //Start thread to get schedule
        new ScheduleGetter(mCurrentSemester.getURL()).execute();

        //Check if this is the first time the user is using the app
        if(Load.isFirstOpen(this)){
        //Show him the walkthrough if it is
        startActivity(new Intent(this, WalkthroughActivity.class));
        //Save the fact that the walkthrough has been seen at least once
        Save.saveFirstOpen(this);
        }
    }

    @Override
    public void onBackPressed(){
        if(App.getHomePage() != HomePage.SCHEDULE){
            startActivity(new Intent(ScheduleActivity.this, App.getHomePage().getHomePageClass()));
            super.onBackPressed();
        }
        else{
            if (mDoubleBackToExit) {
                super.onBackPressed();
                return;
            }
            this.mDoubleBackToExit = true;
            Toast.makeText(this, R.string.back_toaster_message, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDoubleBackToExit = false;
                }
            }, 2000);
        }
    }

    //Method that returns a list of courses for a given day
    public List<ClassItem> getClassesForDay(Day day){
        List<ClassItem> courses = new ArrayList<ClassItem>();

        //Go through the list of courses, find which ones have the same day
        for(ClassItem course : mClassList){
            if(course.getDays().contains(day)){
                courses.add(course);
            }
        }
        return courses;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_schedule_land);
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

            LinearLayout timetableContainer = (LinearLayout) findViewById(R.id.timetable_container);
            fillTimetable(inflater, timetableContainer);

            LinearLayout scheduleContainer = (LinearLayout)findViewById(R.id.schedule_container);
            for(int i = 0; i < 7; i ++){
                LinearLayout coursesLayout = new LinearLayout(this);
                coursesLayout.setOrientation(LinearLayout.VERTICAL);
                coursesLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.cell_landscape_width),
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                fillSchedule(inflater, coursesLayout, Day.getDay(i));
                scheduleContainer.addView(coursesLayout);

                //Line
                View line = new View(this);
                line.setBackgroundColor(getResources().getColor(R.color.black));
                line.setLayoutParams(new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.line),
                        ViewGroup.LayoutParams.MATCH_PARENT));
                scheduleContainer.addView(line);
            }


        }
        else{
            setContentView(R.layout.activity_schedule);
            mPager = (ViewPager)findViewById(R.id.pager);
            loadInfo();
        }

        loadDrawer();
    }

    private void loadInfo(){
        //Title
        setTitle(mCurrentSemester.getSemesterName(this));

        //Clear the current course list
        mClassList.clear();
        for(ClassItem classItem : App.getClasses()){
            if(classItem.isForSemester(mCurrentSemester)){
                mClassList.add(classItem);
            }
        }

        SchedulePagerAdapter adapter = new SchedulePagerAdapter(mSupportFragmentManager);
        mPager.setAdapter(adapter);

        //Open it to the right day
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        switch (day){
	        case Calendar.MONDAY:
	            mPager.setCurrentItem(500003);
	            break;
	        case Calendar.TUESDAY:
	            mPager.setCurrentItem(500004);
	            break;
	        case Calendar.WEDNESDAY:
	            mPager.setCurrentItem(500005);
	            break;
	        case Calendar.THURSDAY:
	            mPager.setCurrentItem(500006);
	            break;
	        case Calendar.FRIDAY:
	            mPager.setCurrentItem(500007);
	            break;
	        case Calendar.SATURDAY:
	            mPager.setCurrentItem(500008);
	            break;
	        case Calendar.SUNDAY:
	            mPager.setCurrentItem(500009);
	            break;
        }
    }

    private void fillTimetable(LayoutInflater inflater, LinearLayout timetableContainer){
        //Empty view for the days
        //Day name
        View dayView = inflater.inflate(R.layout.fragment_day_name, null);

        //Black line
        View dayViewLine = dayView.findViewById(R.id.day_line);
        dayViewLine.setVisibility(View.VISIBLE);

        timetableContainer.addView(dayView);

        //Cycle through the hours
        for(int hour = 8; hour < 22; hour++){
            //Start inflating a timetable cell
            View timetableCell = inflater.inflate(R.layout.item_day_timetable, null);

            //Quick check
            assert(timetableCell != null);

            //Put the correct time
            TextView time = (TextView)timetableCell.findViewById(R.id.cell_time);
            time.setText(Help.getShortTimeString(this, hour));

            //Add it to the right container
            timetableContainer.addView(timetableCell);
        }
    }

    //Method that fills the schedule based on given data
    private void fillSchedule(LayoutInflater inflater,
                              LinearLayout scheduleContainer, Day currentDay){
        //This will be used of an end time of a course when it is added to the schedule container
        LocalTime currentCourseEndTime = null;

        List<ClassItem> classItems = getClassesForDay(currentDay);

        //Day name
        View dayView = inflater.inflate(R.layout.fragment_day_name, null);
        TextView dayViewTitle = (TextView)dayView.findViewById(R.id.day_name);
        dayViewTitle.setText(currentDay.getDayString(this));

        scheduleContainer.addView(dayView);

        //Cycle through the hours
        for(int hour = 8; hour < 22; hour++){
            //Cycle through the half hours
            for(int min = 0; min < 31; min+= 30){
                //Initialize the current course to null
                ClassItem currentClass = null;

                //Get the current time
                LocalTime currentTime = new LocalTime(hour, min);

                //if currentCourseEndTime = null (no course is being added) or it is equal to
                //the current time in min (end of a course being added) we need to add a new view
                if(currentCourseEndTime == null || currentCourseEndTime.equals(currentTime)){
                    //Reset currentCourseEndTime
                    currentCourseEndTime = null;

                    //Check if there is a course at this time
                    for(ClassItem course : classItems){
                        //If there is, set the current course to that time, and calculate the
                        //ending time of this course
                        if(course.getStartTime().equals(currentTime)){
                            currentClass = course;
                            currentCourseEndTime = course.getEndTime();
                            break;
                        }
                    }

                    View scheduleCell;

                    //There is a course at this time
                    if(currentClass != null){
                        //Inflate the right view
                        scheduleCell = inflater.inflate(R.layout.item_day_class, null);

                        //Quick check
                        assert(scheduleCell != null);

                        //Set up all of the info
                        TextView courseName = (TextView)scheduleCell.findViewById(R.id.course_code);
                        courseName.setText(currentClass.getCourseCode());

                        TextView courseType = (TextView)scheduleCell.findViewById(R.id.course_type);
                        courseType.setText(currentClass.getSectionType());

                        TextView  courseTime = (TextView)scheduleCell.findViewById(R.id.course_time);
                        courseTime.setText(currentClass.getTimeString(this));

                        TextView courseLocation = (TextView)scheduleCell.findViewById(R.id.course_location);
                        courseLocation.setText(currentClass.getLocation());

                        //Find out how long this course is in terms of blocks of 30 min
                        int length = Minutes.minutesBetween(currentClass.getStartTime(), currentClass.getEndTime()).getMinutes() / 30;

                        //Set the height of the view depending on this height
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                (int) this.getResources().getDimension(R.dimen.cell_30min_height) * length);
                        scheduleCell.setLayoutParams(lp);

                        //OnClick: CourseActivity (for                                                                                                                                                                                                  b                                                                                                                                               a detailed description of the course)
                        scheduleCell.setClickable(false);
                    }
                    else{
                        //Inflate the empty view
                        scheduleCell = inflater.inflate(R.layout.item_day_empty, null);

                        //Quick check
                        assert(scheduleCell != null);
                    }

                    //Add the given view to the schedule container
                    scheduleContainer.addView(scheduleCell);
                }
            }
        }
    }

    private class ScheduleGetter extends AsyncTask<Void, Void, Boolean> {
        private String scheduleURL;

        public ScheduleGetter(String url){
            scheduleURL = url;
        }

        @Override
        protected void onPreExecute(){
            //Show the user we are refreshing
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String scheduleString;
            final Activity activity = ScheduleActivity.this;

  			scheduleString = Connection.getInstance().getUrl(ScheduleActivity.this, scheduleURL);

            if(scheduleString == null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogHelper.showNeutralAlertDialog(activity, activity.getResources().getString(R.string.error),
                                activity.getResources().getString(R.string.error_other));
                    }
                });
                return false;
            }
            //Empty String: no need for an alert dialog but no need to reload
            else if(TextUtils.isEmpty(scheduleString)){
                return false;
            }

            //Get the new schedule
            Parser.parseClassList(mCurrentSemester.getSeason(),
                    mCurrentSemester.getYear(), scheduleString);

            return true;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Boolean loadInfo) {
            if(loadInfo){
                //Reload the adapter
                loadInfo();
            }

            setProgressBarIndeterminateVisibility(false);
        }
    }

    private class SchedulePagerAdapter extends FragmentStatePagerAdapter{
        public SchedulePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Day currentDay = Day.getDay(i%7);
            return DayFragment.newInstance(currentDay);
        }

        @Override
        public int getCount() {
            return 1000000;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.refresh, menu);

    	// change semester menu item
    	menu.add(Menu.NONE, Constants.MENU_ITEM_CHANGE_SEMESTER, Menu.NONE, R.string.schedule_change_semester);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Opens the context menu    
            case Constants.MENU_ITEM_CHANGE_SEMESTER:
            	Intent intent = new Intent(this, ChangeSemesterActivity.class);
                startActivityForResult(intent, CHANGE_SEMESTER_CODE);
            	return true;
            case R.id.action_refresh:
                //Start thread to retrieve schedule
                new ScheduleGetter(mCurrentSemester.getURL()).execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CHANGE_SEMESTER_CODE){
            if(resultCode == RESULT_OK){
                mCurrentSemester = ((Semester)data.getSerializableExtra(Constants.SEMESTER));

                //Quick Check
                assert (mCurrentSemester != null);

                new ScheduleGetter(mCurrentSemester.getURL()).execute();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
