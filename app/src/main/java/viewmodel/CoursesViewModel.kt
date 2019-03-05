/*
 * Copyright 2014-2019 Julien Guerinet
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

package com.guerinet.mymartlet.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.guerinet.mymartlet.model.Course
import com.guerinet.mymartlet.util.prefs.DefaultTermPref
import com.guerinet.mymartlet.util.room.daos.CourseDao

/**
 * [ViewModel] for the list of [Course]s
 * @author Julien Guerinet
 * @since 2.0.0
 */
class CoursesViewModel(defaultTermPref: DefaultTermPref, private val courseDao: CourseDao) : BaseViewModel() {

    /** Observable current term */
    val term = defaultTermPref.termLiveData()

    /** Observable list of all courses */
    private val courses = courseDao.getCourses()

    /** Observable list of [Course]s for the current [term] */
    val termCourses = MediatorLiveData<List<Course>>().apply {

        addSource(term) {
            // Update the courses when the term changes
            updateCourses()
        }

        addSource(courses) {
            // Update the courses when the courses are updated
            updateCourses()
        }
    }

    /**
     * Updates the list of [Course]s for this term
     */
    private fun updateCourses() {
        val courses = this.courses.value ?: listOf()
        val term = this.term.value ?: error("Missing term")
        termCourses.postValue(courses.filter { it.term == term })
    }
}