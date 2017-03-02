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

package ca.appvelopers.mcgillmobile.util.thread;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.guerinet.utils.Utils;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.SSLException;

import ca.appvelopers.mcgillmobile.App;
import ca.appvelopers.mcgillmobile.R;
import ca.appvelopers.mcgillmobile.model.Course;
import ca.appvelopers.mcgillmobile.model.Semester;
import ca.appvelopers.mcgillmobile.model.Statement;
import ca.appvelopers.mcgillmobile.model.Term;
import ca.appvelopers.mcgillmobile.model.Transcript;
import ca.appvelopers.mcgillmobile.model.exception.MinervaException;
import ca.appvelopers.mcgillmobile.model.retrofit.McGillService;
import ca.appvelopers.mcgillmobile.util.Constants;
import ca.appvelopers.mcgillmobile.util.dbflow.DBUtils;
import ca.appvelopers.mcgillmobile.util.dbflow.databases.CoursesDB;
import ca.appvelopers.mcgillmobile.util.dbflow.databases.StatementsDB;
import ca.appvelopers.mcgillmobile.util.manager.ScheduleManager;
import ca.appvelopers.mcgillmobile.util.manager.TranscriptManager;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Downloads the user's information
 * @author Julien Guerinet
 * @since 2.2.0
 */
public abstract class UserDownloader extends Thread {
    /**
     * App context
     */
    @Inject
    protected Context context;
    /**
     * {@link McGillService} instance
     */
    @Inject
    protected McGillService mcGillService;
    /**
     * {@link TranscriptManager} instance
     */
    @Inject
    protected TranscriptManager transcriptManager;
    /**
     * {@link ScheduleManager} instance
     */
    @Inject
    protected ScheduleManager scheduleManager;
    /**
     * True if everything should be downloaded, false otherwise (defaults to false)
     */
    protected boolean downloadEverything = false;
    /**
     * {@link IOException} that potentially occurred during the downloading of info
     */
    private IOException exception;

    /**
     * Default Constructor
     *
     * @param context App context
     */
    public UserDownloader(Context context) {
        App.component(context).inject(this);
    }

    @Override
    public void run() {
        synchronized (this) {
            //If we're not connected to the internet, don't continue
            if (!Utils.isConnected(context)) {
                return;
            }

            //Transcript
            if (downloadEverything) {
                update(context.getString(R.string.downloading_transcript));
            }

            //Set the old transcript instance to use for the semesters if ever we don't get to
            //  download the new one
            Transcript transcript = transcriptManager.get();
            try {
                transcript = mcGillService.transcript().execute().body();
                transcriptManager.set(transcript);
            } catch(IOException e) {
                handleException(e, "Transcript");
            }

            //The current term
            Term currentTerm = Term.currentTerm();

            //Go through the semesters
            for (Semester semester: transcript.getSemesters()) {
                //Get the term of this semester
                Term term = semester.getTerm();

                //If we are not downloading everything, only download it if it's the
                //  current or future term
                if (downloadEverything || term.equals(currentTerm) ||
                        term.isAfter(currentTerm)) {
                    if (downloadEverything) {
                        update(context.getString(R.string.downloading_semester,
                                term.getString(context)));
                    }

                    //Download the schedule
                    try {
                        List<Course> courses = mcGillService.schedule(term).execute().body();
                        CoursesDB.setCourses(term, courses);
                    } catch (IOException e) {
                        handleException(e, term.getId());
                    }
                }
            }

            //Ebill
            if (downloadEverything) {
                update(context.getString(R.string.downloading_ebill));
            }

            // Download the ebill
            try {
                Response<List<Statement>> response = mcGillService.ebill().execute();
                DBUtils.replaceDB(context, StatementsDB.NAME, Statement.class, response.body(),
                        null);
            } catch (IOException e) {
                handleException(e, "Ebill");
            }
            notify();
        }
    }

    /**
     * Handles any exception while downloading the user info
     *
     * @param e       Exception instance
     * @param section Section that the exception happened in
     */
    private void handleException(IOException e, String section) {
        if (e instanceof MinervaException) {
            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(new Intent(Constants.BROADCAST_MINERVA));
        } else if (!(e instanceof SSLException)) {
            // Don't log SSLExceptions
            Timber.e(new Exception("Exception: " + section, e), "");
        }
        exception = e;
    }

    /**
     * Executes the thread synchronously
     *
     * @throws IOException
     */
    public void execute() throws IOException {
        //If we are downloading this synchronously, then we're downloading everything
        downloadEverything = true;

        //Start the download
        start();

        //Wait until the thread has been fully executed
        synchronized(this) {
            try {
                wait();
            } catch(InterruptedException ignored){}
        }

        //If there's an exception, throw it
        if (exception != null) {
            throw exception;
        }
    }

    public abstract void update(String section);
}

