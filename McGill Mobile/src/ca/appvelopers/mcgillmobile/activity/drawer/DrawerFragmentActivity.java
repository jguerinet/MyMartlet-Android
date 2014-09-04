package ca.appvelopers.mcgillmobile.activity.drawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import ca.appvelopers.mcgillmobile.R;
import ca.appvelopers.mcgillmobile.activity.ScheduleActivity;
import ca.appvelopers.mcgillmobile.activity.base.BaseFragmentActivity;
import ca.appvelopers.mcgillmobile.activity.map.MapActivity;

public class DrawerFragmentActivity extends BaseFragmentActivity {

    public DrawerLayout drawerLayout;
    public ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        loadDrawer();
    }

    public void loadDrawer(){
        assert (getActionBar() != null);

        // R.id.drawer_layout should be in every activity with exactly the same id.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(drawerLayout != null){
            drawerLayout.setFocusableInTouchMode(false);

            //Set up the drawer toggle
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, 0, 0);

            drawerLayout.setDrawerListener(drawerToggle);

            //Set up the adapter
            DrawerAdapter drawerAdapter;
            if(this instanceof ScheduleActivity){
                drawerAdapter = new DrawerAdapter(this, drawerLayout, DrawerAdapter.SCHEDULE_POSITION);
            }
            else if(this instanceof MapActivity){
                drawerAdapter = new DrawerAdapter(this, drawerLayout, DrawerAdapter.MAP_POSITION);
            }
            else{
                drawerAdapter = new DrawerAdapter(this, drawerLayout, -1);
                Log.e("Drawer", "Drawer Adapter was null");
            }

            drawerList = (ListView) findViewById(R.id.left_drawer);
            drawerList.setAdapter(drawerAdapter);

            drawerToggle.setDrawerIndicatorEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed(){
        //Open the menu if it is not open
        if(!drawerLayout.isDrawerOpen(drawerList)){
            drawerLayout.openDrawer(drawerList);
        }
        //If it is open, ask the user if he wants to exit
        else{
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.drawer_exit))
                    .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DrawerFragmentActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
