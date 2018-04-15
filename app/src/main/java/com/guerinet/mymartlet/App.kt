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

package com.guerinet.mymartlet

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.guerinet.mymartlet.util.dagger.AppModule
import com.guerinet.mymartlet.util.dagger.BaseComponent
import com.guerinet.mymartlet.util.dagger.DaggerBaseComponent
import com.guerinet.suitcase.log.ProductionTree
import com.jakewharton.threetenabp.AndroidThreeTen
import com.orhanobut.hawk.Hawk
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.twitter.sdk.android.core.TwitterAuthConfig
import timber.log.Timber
import java.net.SocketTimeoutException

/**
 * Base application instance
 * @author Julien Guerinet
 * @since 1.0.0
 */
class App : MultiDexApplication() {

    lateinit var component: BaseComponent

    override fun onCreate() {
        super.onCreate()

        // Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        if (BuildConfig.REPORT_CRASHES) {
            Timber.plant(object: ProductionTree() {

                override fun log(message: String) {
                    Crashlytics.log(message)
                }

                override fun logException(t: Throwable) {
                    // Don't log socket timeouts
                    if (t !is SocketTimeoutException) {
                        Crashlytics.logException(t)
                    }
                }
            })
        }

        // Fabric, Twitter, Crashlytics
        val authConfig = TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET)
        val crashlytics = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(!BuildConfig.REPORT_CRASHES).build())
                .build()
//        Fabric.with(this, Twitter(authConfig), TweetComposer(), crashlytics)

        // Android ThreeTen
        AndroidThreeTen.init(this)

        // Dagger
        component = DaggerBaseComponent.builder()
                .appModule(AppModule(this))
                .build()

        // Hawk
        Hawk.init(this).build()

        // DBFlow
        FlowManager.init(FlowConfig.Builder(this).build())

        // FormGenerator
        val padding = resources.getDimensionPixelOffset(R.dimen.padding_small)
//        FormGenerator.set(FormGenerator.Builder()
//                .setDefaultBackground(R.drawable.transparent_redpressed)
//                .setDefaultDrawablePaddingSize(padding)
//                .setDefaultPaddingSize(padding)
//                .setDefaultIconColor(ContextCompat.getColor(this, R.color.red)))
    }

    companion object {

        /**
         * Returns the [BaseComponent] for the given app [context]
         */
        fun component(context: Context) : BaseComponent =
                (context.applicationContext as App).component

//        fun setAlarm(context: Context) {
//            BootReceiver.setAlarm(context)
//        }

//        fun cancelAlarm(context: Context) {
//            BootReceiver.cancelAlarm(context)
//        }
    }
}