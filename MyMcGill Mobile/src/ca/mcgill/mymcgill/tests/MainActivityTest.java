package ca.mcgill.mymcgill.tests;

import ca.mcgill.mymcgill.activity.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Author: Joshua
 * Date: 29/01/14, 3:01 AM
 */
public class MainActivityTest
		extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivityTest;
    
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivityTest = getActivity();
    }
    
    public void testPrecondition() {
    	
    }
}