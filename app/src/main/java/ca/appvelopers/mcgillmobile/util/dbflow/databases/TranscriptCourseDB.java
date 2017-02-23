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

package ca.appvelopers.mcgillmobile.util.dbflow.databases;

import com.raizlabs.android.dbflow.annotation.Database;

import ca.appvelopers.mcgillmobile.model.transcript.TranscriptCourse;

/**
 * Database that holds a list of {@link TranscriptCourse}s
 * @author Julien Guerinet
 * @since 2.4.0
 */
@Database(name = TranscriptCourseDB.NAME, version = TranscriptCourseDB.VERSION)
public class TranscriptCourseDB {
    static final String NAME = "TranscriptCourses";
    static final int VERSION = 1;
}
