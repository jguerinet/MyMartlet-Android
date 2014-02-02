package ca.mcgill.mymcgill.activity.transcript;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import ca.mcgill.mymcgill.R;
import ca.mcgill.mymcgill.object.Transcript;
import ca.mcgill.mymcgill.util.ApplicationClass;
import ca.mcgill.mymcgill.util.Connection;

/**
 * Author: Ryan Singzon
 * Date: 30/01/14, 6:01 PM
 */
public class TranscriptActivity extends ListActivity {

    protected TranscriptActivity transcriptActivity = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcript);

        //Start thread to retrieve transcript
        new TranscriptGetter().execute();
    }

    private class TranscriptGetter extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
            //TODO: Add loading indicator
        }

        //Retrieve content from transcript page
        @Override
        protected String doInBackground(String... params){
            return Connection.getInstance().getUrl(Connection.minervaTranscript);
        }

        //Update or create transcript object and display data
        @Override
        protected void onPostExecute(String result){
            //TextView textView = new TextView(transcriptActivity);
            //String htmlString = result;
            //textView.setText(Html.fromHtml(htmlString));
            //textView.setMovementMethod(new ScrollingMovementMethod());
            //setContentView(textView);


            //Get the transcript from the ApplicationClass
            Transcript transcript= ApplicationClass.getTranscript(result);

            //If the transcript is null, then this means that there is a problem.
            if(transcript == null){
                //Alert the user
                TextView transcriptError = (TextView)findViewById(R.id.transcript_error);
                transcriptError.setVisibility(View.VISIBLE);

                //Hide the semester TextView
                TextView semesterTitle = (TextView)findViewById(R.id.semester_title);
                semesterTitle.setVisibility(View.GONE);

                //Nothing else should be done.
                return;
            }

            //Fill out the transcript info
            TextView cgpa = (TextView)findViewById(R.id.transcript_cgpa);
            cgpa.setText(getResources().getString(R.string.transcript_cgpa, transcript.getCgpa()));

            TextView totalCredits = (TextView)findViewById(R.id.transcript_credits);
            totalCredits.setText(getResources().getString(R.string.transcript_credits, transcript.getTotalCredits()));

            //Create the adapter
            TranscriptAdapter adapter = new TranscriptAdapter(transcriptActivity, transcript);

            //Set the listview's adapter to this
            setListAdapter(adapter);

        }
    }

}
