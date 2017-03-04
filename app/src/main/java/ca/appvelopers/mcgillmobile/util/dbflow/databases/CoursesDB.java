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

import java.util.ArrayList;
import java.util.List;

import ca.appvelopers.mcgillmobile.model.Course;
import ca.appvelopers.mcgillmobile.model.Course_Table;
import ca.appvelopers.mcgillmobile.model.Term;
import ca.appvelopers.mcgillmobile.util.dbflow.DBUtils;

/**
 * Database that holds a list of {@link Course}s that comprise the user's schedule
 * @author Julien Guerinet
 * @since 2.4.0
 */
@Database(name = CoursesDB.NAME, version = CoursesDB.VERSION)
public class CoursesDB {
    public static final String NAME = "Courses";
    public static final String FULL_NAME = NAME + ".db";
    static final int VERSION = 1;

    /**
     * Saves the {@link Course}s for the given {@link Term}
     *
     * @param term    {@link Term} these courses are for
     * @param courses List of {@link Course}s to save
     */
    public static void setCourses(Term term, List<Course> courses) {
        if (courses == null) {
            courses = new ArrayList<>();
        }

        // Set the term on the passed list of courses
        for (Course course : courses) {
            course.setTerm(term);
        }

        DBUtils.updateDB(Course.class, courses, Course_Table.term.eq(term), CoursesDB.class, null);
    }
}
