package ca.mcgill.mymcgill.activity.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.mymcgill.App;
import ca.mcgill.mymcgill.R;
import ca.mcgill.mymcgill.activity.AboutActivity;
import ca.mcgill.mymcgill.activity.DesktopActivity;
import ca.mcgill.mymcgill.activity.LoginActivity;
import ca.mcgill.mymcgill.activity.MapActivity;
import ca.mcgill.mymcgill.activity.MyCoursesActivity;
import ca.mcgill.mymcgill.activity.RegistrationActivity;
import ca.mcgill.mymcgill.activity.ScheduleActivity;
import ca.mcgill.mymcgill.activity.SettingsActivity;
import ca.mcgill.mymcgill.activity.courseslist.CoursesListActivity;
import ca.mcgill.mymcgill.activity.ebill.EbillActivity;
import ca.mcgill.mymcgill.activity.inbox.InboxActivity;
import ca.mcgill.mymcgill.activity.transcript.TranscriptActivity;
import ca.mcgill.mymcgill.object.DrawerItem;
import ca.mcgill.mymcgill.util.Clear;

/**
 * Author: Shabbir
 * Date: 24/02/14, 11:46 PM
 */
public class DrawerAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<DrawerItem> mDrawerItems;
    private int mSelectedPosition;
    private int mUnreadMessages;
    private TextView mUnreadMessagesView;
    private DrawerLayout mDrawerLayout;

    //Easy way to keep track of the list order (for the OnClick)
    public static final int SCHEDULE_POSITION = 0;
    public static final int TRANSCRIPT_POSITION = 1;
    public static final int EMAIL_POSITION = 2;
    public static final int MYCOURSES_POSITION = 3;
    public static final int SEARCH_COURSES_POSITION = 4;
    public static final int WISHLIST_POSITION = 5;
    public static final int EBILL_POSITION = 6;
    public static final int MAP_POSITION = 7;
    public static final int DESKTOP_POSITION = 8;
    public static final int SETTINGS_POSITION = 9;
    public static final int LOGOUT_POSITION = 10;
    public static final int ABOUT_POSITION = 11;

    public DrawerAdapter(Activity activity, DrawerLayout drawerLayout, int selectedPosition){
        this.mActivity = activity;
        this.mDrawerLayout = drawerLayout;
        this.mDrawerItems = new ArrayList<DrawerItem>();
        this.mSelectedPosition = selectedPosition;
        this.mUnreadMessages = App.getUnreadEmails();
        generateDrawerItems();
    }

    //This will generate the drawer items
    private void generateDrawerItems(){
        //Schedule
        mDrawerItems.add(SCHEDULE_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_schedule),
                mActivity.getResources().getString(R.string.icon_schedule)));

        //Transcript
        mDrawerItems.add(TRANSCRIPT_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_transcript),
                mActivity.getResources().getString(R.string.icon_transcript)));

        //Email
        mDrawerItems.add(EMAIL_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_inbox),
                mActivity.getResources().getString(R.string.icon_email)));

        //MyCourses
        mDrawerItems.add(MYCOURSES_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_mycourses),
                mActivity.getResources().getString(R.string.icon_mycourses)));

        //Search Courses
        mDrawerItems.add(SEARCH_COURSES_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_registration),
                mActivity.getResources().getString(R.string.icon_browse_courses)));

        mDrawerItems.add(WISHLIST_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_wishlist),
                mActivity.getResources().getString(R.string.icon_browse_courses)));

        //Ebill
        mDrawerItems.add(EBILL_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_ebill),
                mActivity.getResources().getString(R.string.icon_ebill)));

        //Map
        mDrawerItems.add(MAP_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_map),
                mActivity.getResources().getString(R.string.icon_map)));

        //Desktop
        mDrawerItems.add(DESKTOP_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_desktop),
                mActivity.getResources().getString(R.string.icon_desktop)));

        //Settings
        mDrawerItems.add(SETTINGS_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_settings),
                mActivity.getResources().getString(R.string.icon_settings)));

        //Logout
        mDrawerItems.add(LOGOUT_POSITION, new DrawerItem(mActivity.getResources().getString(R.string.title_logout),
                mActivity.getResources().getString(R.string.icon_logout)));

        // About
        mDrawerItems.add(ABOUT_POSITION ,new DrawerItem(mActivity.getResources().getString(R.string.title_about),mActivity.getResources().getString(R.string.icon_question)));

    }

    @Override
    public int getCount() {
        return mDrawerItems.size();
    }

    @Override
    public DrawerItem getItem(int position) {
        return mDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_drawer, null);
        }

        //Quick Check
        assert(view != null);

        //Get the current object
        DrawerItem currentItem = mDrawerItems.get(position);

        //Set the info up
        TextView icon = (TextView)view.findViewById(R.id.drawerItem_icon);
        icon.setTypeface(App.getIconFont());
        icon.setText(currentItem.getIcon());

        TextView title = (TextView)view.findViewById(R.id.drawerItem_title);
        title.setText(currentItem.getTitle());
        
        TextView badge = (TextView)view.findViewById(R.id.drawer_email_count);
        if(position == EMAIL_POSITION){
            mUnreadMessagesView = badge;
            updateUnreadMessages();
        }
        else{
        	badge.setVisibility(View.INVISIBLE);
        }
        
        //OnClick
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(position){
                    case SCHEDULE_POSITION:
                        mActivity.startActivity(new Intent(mActivity, ScheduleActivity.class));
                        break;
                    case TRANSCRIPT_POSITION:
                        mActivity.startActivity(new Intent(mActivity, TranscriptActivity.class));
                        break;
                    case EMAIL_POSITION:
                        mActivity.startActivity(new Intent(mActivity, InboxActivity.class));
                        break;
                    case MYCOURSES_POSITION:
                        mActivity.startActivity(new Intent(mActivity, MyCoursesActivity.class));
                        break;
                    case SEARCH_COURSES_POSITION:
                        mActivity.startActivity(new Intent(mActivity, RegistrationActivity.class));
                        break;
                    case WISHLIST_POSITION:
                        mActivity.startActivity(new Intent(mActivity, CoursesListActivity.class));
                        break;
                    case EBILL_POSITION:
                        mActivity.startActivity(new Intent(mActivity, EbillActivity.class));
                        break;
                    case MAP_POSITION:
                        mActivity.startActivity(new Intent(mActivity, MapActivity.class));
                        break;
                    case DESKTOP_POSITION:
                        mActivity.startActivity(new Intent(mActivity, DesktopActivity.class));
                        break;
                    case SETTINGS_POSITION:
                        mActivity.startActivity(new Intent(mActivity, SettingsActivity.class));
                        break;
                    case LOGOUT_POSITION:
                        Clear.clearAllInfo(mActivity);
                        //Go back to LoginActivity
                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                        break;
                    case ABOUT_POSITION:
                        mActivity.startActivity(new Intent(mActivity, AboutActivity.class));
                        break;
                }
                mActivity.finish();
            }
        });

        //If it's the selected position, set its background to red and the text to white
        if(position == mSelectedPosition){
            view.setBackgroundColor(mActivity.getResources().getColor(R.color.red));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            });

            icon.setTextColor(mActivity.getResources().getColor(R.color.white));
            title.setTextColor(mActivity.getResources().getColor(R.color.white));
        }

        return view;
    }

    public void updateUnreadMessages(){
        mUnreadMessages = App.getUnreadEmails();

        if(mUnreadMessagesView != null){
            if(mUnreadMessages == 0){
                mUnreadMessagesView.setVisibility(View.INVISIBLE);
            }
            else{
                mUnreadMessagesView.setVisibility(View.VISIBLE);
                mUnreadMessagesView.setText(String.valueOf(mUnreadMessages));
            }
            notifyDataSetChanged();
        }
    }
}
