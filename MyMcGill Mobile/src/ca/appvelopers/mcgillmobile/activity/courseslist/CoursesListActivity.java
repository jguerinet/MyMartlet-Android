package ca.appvelopers.mcgillmobile.activity.courseslist;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ca.appvelopers.mcgillmobile.App;
import ca.appvelopers.mcgillmobile.R;
import ca.appvelopers.mcgillmobile.activity.drawer.DrawerActivity;
import ca.appvelopers.mcgillmobile.object.ClassItem;
import ca.appvelopers.mcgillmobile.object.Season;
import ca.appvelopers.mcgillmobile.util.Connection;
import ca.appvelopers.mcgillmobile.util.Constants;
import ca.appvelopers.mcgillmobile.util.DialogHelper;
import ca.appvelopers.mcgillmobile.util.GoogleAnalytics;
import ca.appvelopers.mcgillmobile.util.Parser;

/**
 * Author : Julien
 * Date :  2014-05-26 7:09 PM
 * Shows a list of courses
 */
public class CoursesListActivity extends DrawerActivity {
    public boolean wishlist;

    private List<ClassItem> mClasses;
    private ListView mListView;
    private String mRegistrationUrl;
    private String mRegistrationError = "NULL";
    private ClassAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_courseslist);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        wishlist = getIntent().getBooleanExtra(Constants.WISHLIST, true);

        super.onCreate(savedInstanceState);

        if(wishlist){
            GoogleAnalytics.sendScreen(this, "Wishlist");
        }
        else{
            GoogleAnalytics.sendScreen(this, "Search Results");
        }

        // Views
        mListView = (ListView)findViewById(R.id.courses_list);
        mListView.setEmptyView(findViewById(R.id.courses_empty));

        //Check if we need to load the wishlist
        if(wishlist){
            mClasses = App.getClassWishlist();
        }
        //If not, get the searched courses
        else{
            mClasses = Constants.searchedClassItems;
        }

        //Register button
        TextView register = (TextView)findViewById(R.id.course_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get checked courses from adapter
                List<ClassItem> registerCoursesList = mAdapter.getCheckedClasses();

                //Get term
                if (registerCoursesList.size() > 10){
                    String toastMessage = getResources().getString(R.string.courses_too_many_courses);
                    Toast.makeText(CoursesListActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                }
                else if(registerCoursesList.size() > 0){

                    //Get the registration URL
                    Season season = mAdapter.getItem(0).getSeason();
                    int year = mAdapter.getItem(0).getYear();
                    int[] crns = new int[Constants.MAX_CRNS];

                    int arrayIndex = 0;
                    for (ClassItem course : registerCoursesList){

                        crns[arrayIndex] = course.getCRN();
                        arrayIndex++;
                    }

                    mRegistrationUrl = Connection.getRegistrationURL(season, year, crns, false);

                    //Execute registration of checked classes in a new thread
                    new Registration().execute();

                }
                else{
                    String toastMessage = getResources().getString(R.string.courses_none_selected);
                    Toast.makeText(CoursesListActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Add/Remove to/from Wishlist Button
        TextView wishlist = (TextView)findViewById(R.id.course_wishlist);
        wishlist.setText(this.wishlist ? getResources().getString(R.string.courses_remove_wishlist) :
            getResources().getString(R.string.courses_add_wishlist));
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the checked list of courses from the adapter
                List<ClassItem> checkedClasses = mAdapter.getCheckedClasses();

                String toastMessage;

                //If there are none, display error message
                if(checkedClasses.isEmpty()){
                    toastMessage = getResources().getString(R.string.wishlist_error_empty);
                }
                //If we are in the wishlist, this button is to remove a course
                else if(CoursesListActivity.this.wishlist){
                    toastMessage = getResources().getString(R.string.wishlist_remove, checkedClasses.size());
                    mClasses.removeAll(checkedClasses);

                    //Save the courses to the App context
                    App.setClassWishlist(mClasses);

                    GoogleAnalytics.sendEvent(CoursesListActivity.this, "Wishlist", "Remove",
                            "" + checkedClasses.size(), null);

                    //Reload the adapter
                    loadInfo();

                }
                //If not, it's to add a course to the wishlist
                else{
                    //Get the wishlist courses
                    List<ClassItem> wishlist = App.getClassWishlist();

                    //Only add it if it's not already part of the wishlist
                    int coursesAdded = 0;
                    for(ClassItem classItem : checkedClasses){
                        if(!wishlist.contains(classItem)){
                            wishlist.add(classItem);
                            coursesAdded ++;
                        }
                    }

                    //Save the courses to the App context
                    App.setClassWishlist(wishlist);

                    GoogleAnalytics.sendEvent(CoursesListActivity.this, "Search Results", "Add to Wishlist",
                            "" + coursesAdded, null);

                    toastMessage = getResources().getString(R.string.wishlist_add, coursesAdded);
                }

                //Visual feedback of what was just done
                Toast.makeText(CoursesListActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Connects to Minerva in a new thread to register for courses
    private class Registration extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute(){
            //Show the user we are downloading new info
            setProgressBarIndeterminateVisibility(true);
        }

        //Retrieve page that contains registration status from Minerva
        @Override
        protected Boolean doInBackground(Void... params){
            String resultString = Connection.getInstance().getUrl(CoursesListActivity.this, mRegistrationUrl);

            //If result string is null, there was an error
            if(resultString == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Activity activity = CoursesListActivity.this;
                        try {
                            DialogHelper.showNeutralAlertDialog(activity, activity.getResources().getString(R.string.error),
                                    activity.getResources().getString(R.string.error_other));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                return false;
            }
            //Otherwise, check for errors
            else{
                mRegistrationError = Parser.parseRegistrationErrors(resultString);
                return true;
            }
        }

        //Update or create transcript object and display data
        @Override
        protected void onPostExecute(Boolean loadInfo){
            setProgressBarIndeterminateVisibility(false);

            if(loadInfo){
                //Display whether the user was successfully registered
                if(mRegistrationError.equals("NULL")){
                    Toast.makeText(CoursesListActivity.this, R.string.registration_success, Toast.LENGTH_LONG).show();
                }

                //Display a message if a registration error has occurred
                else{
                    Toast.makeText(CoursesListActivity.this, getResources().getString(R.string.registration_error,
                            mRegistrationError), Toast.LENGTH_LONG).show();
                }
            }
        }
    }



    @Override
    public void onResume(){
        super.onResume();
        loadInfo();
    }

    private void loadInfo(){
        mAdapter = new ClassAdapter(this, mClasses);
        mListView.setAdapter(mAdapter);
    }

    /*@Override
    public void onBackPressed(){
        startActivity(new Intent(CoursesListActivity.this, App.getHomePage().getHomePageClass()));
        super.onBackPressed();
    }*/

//    @Override
//    public void onBackPressed(){
//        super.onBackPressed();
//        overridePendingTransition(R.anim.left_in, R.anim.right_out);
//    }

    // JDAlfaro
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
                Intent intent = new Intent(this, ChangeListActivity.class);
                startActivity(intent);
                //startActivityForResult(intent, CHANGE_SEMESTER_CODE);
                return true;
            case R.id.action_refresh:
                //Start thread to retrieve schedule
                //new ScheduleGetter(mCurrentSemester.getURL()).execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CHANGE_SEMESTER_CODE){
            if(resultCode == RESULT_OK){
                //mCurrentSemester = ((Semester)data.getSerializableExtra(Constants.SEMESTER));

                //Quick Check
                //assert (mCurrentSemester != null);

               // new ScheduleGetter(mCurrentSemester.getURL()).execute();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

}