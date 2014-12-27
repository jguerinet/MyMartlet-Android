package ca.appvelopers.mcgillmobile.object;

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