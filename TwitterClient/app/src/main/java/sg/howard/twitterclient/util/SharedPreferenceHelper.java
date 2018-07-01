package sg.howard.twitterclient.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

/** Singletone class helper to handle shared preference, based on this tutorial below
 * https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
 */
public class SharedPreferenceHelper {
    private SharedPreferences mSharedPreference = null;
    private Context mContext = null;
    @SuppressLint("StaticFieldLeak")
    private static SharedPreferenceHelper instance = null;

    private SharedPreferenceHelper() {
    }

    public static SharedPreferenceHelper getInstance(){
        if(instance == null){
            synchronized(SharedPreferenceHelper.class){
                if(instance == null)
                instance = new SharedPreferenceHelper();
            }
        }
        return instance;
    }

     public SharedPreferenceHelper setContext(Context context)  {
        mContext = context.getApplicationContext();
        return this;
    }

    public SharedPreferences getSharedPreferences() {
        mSharedPreference = mContext.getSharedPreferences("TwitterClient", Context.MODE_PRIVATE);
        return mSharedPreference;
    }
    public SharedPreferences.Editor editor(){
        return mSharedPreference.edit();
    }

    public void saveUser(Result<TwitterSession> result){

        if (result != null && result.data != null) {
            getSharedPreferences().edit().putString("userName",  result.data.getUserName() != null ? result.data.getUserName() : "").apply();
            getSharedPreferences().edit().putLong("userId",  result.data.getUserId()).apply();

        }
    }

    public void saveImageProfile(Tweet tweet) {

        if (tweet != null && tweet.user.profileImageUrl != null) {
            String url = tweet.user.profileImageUrl;
            int pos;
            if (url.contains("_normal.jpeg")) {
                pos = url.indexOf("_normal.jpeg");
                url = url.substring(0,pos)+".jpeg";
            }
            else if (url.contains("_normal.png")) {
                pos = url.indexOf("_normal.png");
                url = url.substring(0,pos)+".png";
            }
            else if (url.contains("_normal.jpg")) {
                pos = url.indexOf("_normal.jpg");
                url = url.substring(0,pos)+".jpg";
            }
            getSharedPreferences().edit().putString("userImage", url ).apply();
        }
    }

    public long getUserId() {
        return getSharedPreferences().getLong("userId",0);
    }

    public String getImageProfile() {
        return getSharedPreferences().getString("userImage","");
    }

}
