/*
 * Copyright 2014-2016 Appvelopers
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

package ca.appvelopers.mcgillmobile.util.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.guerinet.utils.prefs.StringPreference;

import java.util.Calendar;

import javax.inject.Inject;

import ca.appvelopers.mcgillmobile.App;
import ca.appvelopers.mcgillmobile.model.prefs.UsernamePreference;
import ca.appvelopers.mcgillmobile.util.storage.Load;

/**
 * Automatically (re)starts the alarm if needed when the device is rebooted or the user opts in.
 * @author Shabbir Hussain
 * @author Julien Guerinet
 * @since 2.0.0
 */
public class BootReceiver extends BroadcastReceiver {
    /**
     * Username {@link StringPreference}
     */
    @Inject
    protected UsernamePreference usernamePref;
    /**
     * Password {@link StringPreference}
     */
    //TODO
    protected StringPreference passwordPref;

	@Override
	public void onReceive(Context context, Intent intent) {
        App.component(context).inject(this);
        setAlarm(context, usernamePref.get(), passwordPref.get());
	}

	/**
	 * Starts the alarm receiver if needed
	 *
	 * @param context The app context
	 */
	public static void setAlarm(Context context, String username, String password) {
		//If we don't need it, don't start it
		if (username == null || password == null || (!Load.seatChecker() && !Load.gradeChecker())) {
			//Make sure it's cancelled
			cancelAlarm(context);
			return;
		}

		//Get the alarm manager
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		//Set the alarm to fire at approximately 8:30 AM  according to the device's clock,
		//  and to repeat once a day.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 30);

		manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, getPendingIntent(context));
	}

	/**
	 * Cancels the alarm
	 *
	 * @param context The app context
	 */
	public static void cancelAlarm(Context context) {
		//Get the alarm manager
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		//Cancel the pending intent
		manager.cancel(getPendingIntent(context));
	}

	/**
	 * Gets the pending intent for the checker alarm
	 *
	 * @param context The app context
	 * @return The pending intent
	 */
	private static PendingIntent getPendingIntent(Context context){
		Intent intent = new Intent(context, CheckerService.class);
		return PendingIntent.getService(context, 0, intent, 0);
	}
}