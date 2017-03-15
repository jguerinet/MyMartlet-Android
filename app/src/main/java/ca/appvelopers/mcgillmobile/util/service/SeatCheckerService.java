/*
 * Copyright 2014-2017 Julien Guerinet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.appvelopers.mcgillmobile.util.service;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ca.appvelopers.mcgillmobile.R;
import timber.log.Timber;

/**
 * Runs the required checker tasks
 * @author Shabbir Hussain
 * @author Julien Guerinet
 * @since 2.0.0
 */
public class SeatCheckerService extends IntentService {

	public SeatCheckerService() {
		super("SeatCheckerService");
	}

	/**
	 * Here is where the actual logic goes when the alarm is called
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Timber.i("Service started");

		//TODO Do this better: merge with WishlistActivity ? When do we send a notification ?
//		List<Course> wishlistClasses = App.getWishlist();
//
//		//Refresh
//		//Sort ClassItems into Courses
//        List<TranscriptCourse> coursesList = new ArrayList<>();
//        for(Course wishlistClass : wishlistClasses){
//
//            boolean courseExists = false;
//            //Check if course exists in list
//            for(TranscriptCourse addedCourse : coursesList){
//                if(addedCourse.getCourseCode().equals(wishlistClass.getCode())){
//                    courseExists = true;
//                }
//            }
//            //Add course if it has not already been added
//            if(!courseExists){
//                coursesList.add(new TranscriptCourse(wishlistClass.getTerm(), wishlistClass.getCode(),
//                        wishlistClass.getTitle(), wishlistClass.getCredits(), "N/A", "N/A"));
//            }
//        }
//
//        //For each course, obtain its Minerva registration page
//        for(TranscriptCourse course : coursesList){
//
//            //Get the course registration URL
//            String courseCode[] = course.getCourseCode().split(" ");
//            String courseSubject = "";
//            String courseNumber = "";
//
//            //Check that the course code has been split successfully
//            if(courseCode.length > 1){
//                courseSubject = courseCode[0];
//                courseNumber = courseCode[1];
//            } else{
//                //TODO: Return indication of failure
//                return;
//            }
//
//            String registrationUrl = new Connection.SearchURLBuilder(course.getTerm(), courseSubject)
//		            .courseNumber(courseNumber)
//		            .build();
//
//            //error check the url
//            if(registrationUrl==null){
//            	continue;
//            }
//
//	        String classesString = null;
//	        try{
//		        classesString = Connection.getInstance().get(registrationUrl);
//	        } catch(Exception e){
//		        //TODO
//		        e.printStackTrace();
//	        }
//
//	        //TODO: Figure out a way to parse only some course sections instead of re-parsing all course sections for a given Course
//            //This parses all ClassItems for a given course
//            List<Course> updatedClassList = Parser.parseClassResults(course.getTerm(), classesString);
//
//            //Update the course object with an updated class size
//            for(Course updatedClass : updatedClassList){
//
//                for(Course wishlistClass : wishlistClasses){
//
//                    if(wishlistClass.getCRN() == updatedClass.getCRN()){
//                        wishlistClass.setDays(updatedClass.getDays());
//                        wishlistClass.setStartTime(updatedClass.getRoundedStartTime());
//                        wishlistClass.setEndTime(updatedClass.getRoundedEndTime());
//                        wishlistClass.setDates(updatedClass.getDateString());
//                        wishlistClass.setInstructor(updatedClass.getInstructor());
//                        wishlistClass.setLocation(updatedClass.getLocation());
//                        wishlistClass.setSeatsRemaining(updatedClass.getSeatsRemaining());
//                        wishlistClass.setWaitlistRemaining(updatedClass.getWaitlistRemaining());
//                    }
//                }
//            }
//        }
//
//		//check if any classes have open spots
//		for(Course wantedClass : wishlistClasses){
//			if(wantedClass.getSeatsRemaining()>0){
//				Intent intent = new Intent(this, SplashActivity.class)
//						.putExtra(Constants.HOMEPAGE, HomepageManager.WISHLIST)
//						.putExtra(Constants.TERM, wantedClass.getTerm());
//				//show notification
//				createNotification(intent, "A spot has opened up for the class: " +
//						wantedClass.getTitle(), SEATS_ID);
//				return;
//			}
//		}
	}

	/**
	 * Generates a local notification which will redirect the user to the right portion of the app
	 *  when clicked
	 *
	 * @param intent  The intent to use when clicked
	 * @param message The message to display
	 * @param id      The notification Id (to update any existing ones)
	 */
	private void createNotification(Intent intent, String message, int id){
		//Get the notification manager
		NotificationManager manager =
				(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
		        .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
		        .setContentText(message)
		        .setContentIntent(pendingIntent);

        manager.notify(id, builder.build());
	}
}