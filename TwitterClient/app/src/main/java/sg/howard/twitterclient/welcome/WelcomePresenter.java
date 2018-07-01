package sg.howard.twitterclient.welcome;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import androidx.annotation.NonNull;
import sg.howard.twitterclient.util.SharedPreferenceHelper;

public class WelcomePresenter implements WelcomeContract.Presenter {

    TwitterApiClient client = null;
    WelcomeContract.View mView;

    public WelcomePresenter(@NonNull WelcomeContract.View view, TwitterSession session){
        mView= view;
        mView.setPresenter(this);
        client = new TwitterApiClient(session);
    }

    @Override
    public void startGetUser() {

        long userId =  SharedPreferenceHelper.getInstance().getUserId();

        client.getStatusesService()
                .userTimeline(userId,null,null,null,null,null,null, null, null)
                .enqueue(new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {

                        mView.showLoading(false);
                        SharedPreferenceHelper.getInstance().saveImageProfile(result.data.get(0));
                        mView.onGetUserSuccess(result.data.get(0));
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        mView.showLoading(false);
                        mView.showError(exception.getMessage());
                    }
                });


    }

    @Override
    public String getImageProfile(){
        return SharedPreferenceHelper.getInstance().getImageProfile();
    }


    @Override
    public void start() {

    }
}
