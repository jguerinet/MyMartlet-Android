/*
 * Copyright 2014-2015 Appvelopers
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

package ca.appvelopers.mcgillmobile.model;

import java.io.Serializable;

/**
 * Author: Julien
 * Date: 16/02/14, 4:34 PM
 */
public class UserInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String mName;
    private String mId;

    public UserInfo(String name, String id){
        this.mName = name;
        this.mId = id;
    }

    public String getName(){
        return mName;
    }

    public String getId(){
        return mId;
    }
}
