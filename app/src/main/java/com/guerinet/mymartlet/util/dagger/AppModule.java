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

package com.guerinet.mymartlet.util.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Base Dagger module
 * @author Julien Guerinet
 * @since 2.0.4
 */
@Module
public class AppModule {
    /**
     * App context
     */
    private final Context context;

    /**
     * Default Constructor
     *
     * @param context App context
     */
    public AppModule(Context context) {
        this.context = context;
    }

    /* PROVIDERS */

    /**
     * @return App context
     */
    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    /**
     * @param context App context
     * @return {@link SharedPreferences} instance
     */
    @Provides
    @Singleton
    SharedPreferences provideSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * @return {@link Moshi} singleton instance
     */
    @Provides
    @Singleton
    Moshi provideMoshi() {
        return new Moshi.Builder()
                .build();
    }

    /**
     * @param context App context
     * @return {@link InputMethodManager} instance
     */
    @Provides
    InputMethodManager provideInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}