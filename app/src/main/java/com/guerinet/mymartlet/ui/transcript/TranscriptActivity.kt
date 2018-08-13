/*
 * Copyright 2014-2018 Julien Guerinet
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

package com.guerinet.mymartlet.ui.transcript

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import com.guerinet.mymartlet.R
import com.guerinet.mymartlet.model.Transcript
import com.guerinet.mymartlet.ui.DrawerActivity
import com.guerinet.mymartlet.util.dbflow.databases.TranscriptDB
import com.guerinet.mymartlet.util.manager.HomepageManager
import com.guerinet.mymartlet.util.retrofit.TranscriptConverter.TranscriptResponse
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.sql.language.SQLite
import kotlinx.android.synthetic.main.activity_transcript.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Shows the user's transcript
 * @author Julien Guerinet
 * @since 1.0.0
 */
class TranscriptActivity : DrawerActivity() {

    override val currentPage = HomepageManager.HomePage.TRANSCRIPT

    private val adapter = TranscriptAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transcript)
        ga.sendScreen("Transcript")

        list.apply {
            layoutManager = LinearLayoutManager(this@TranscriptActivity)
            adapter = adapter
        }
        update()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.refresh, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                refresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Refreshes the transcript
     */
    private fun refresh() {
        if (!canRefresh()) {
            return
        }

        mcGillService.transcript().enqueue(object : Callback<TranscriptResponse> {
            override fun onResponse(call: Call<TranscriptResponse>,
                    response: Response<TranscriptResponse>) {
                TranscriptDB.saveTranscript(this@TranscriptActivity, response.body()!!)
                update()
                toolbarProgress.isVisible = false
            }

            override fun onFailure(call: Call<TranscriptResponse>, t: Throwable) {
                handleError("refreshing transcript", t)
            }
        })
    }

    /**
     * Updates the view
     */
    private fun update() {
        // Reload all of the info
        val transcript = SQLite.select().from(Transcript::class).querySingle()
        if (transcript != null) {
            cgpa.text = getString(R.string.transcript_CGPA, transcript.cgpa.toString())
            credits.text = getString(R.string.transcript_credits,
                    transcript.totalCredits.toString())
        }
        adapter.update()
    }
}