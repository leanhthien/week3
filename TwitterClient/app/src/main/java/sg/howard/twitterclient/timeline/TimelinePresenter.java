package sg.howard.twitterclient.timeline;

import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import androidx.annotation.NonNull;
import sg.howard.twitterclient.util.SharedPreferenceHelper;

public class TimelinePresenter implements TimelineContract.Presenter {
    TwitterApiClient client = null;
    TimelineContract.View mView;

    public TimelinePresenter(@NonNull TimelineContract.View view, TwitterSession session){
        mView= view;
        mView.setPresenter(this);
        client = new TwitterApiClient(session);

    }

    @Override
    public void startGetTimeline(int page) {
        mView.showLoading(true);
        client.getStatusesService()
                .homeTimeline(page, null, null, null, null, null, null)
                .enqueue(new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {

                        mView.showLoading(false);
                        mView.onGetStatusesSuccess(result.data);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        mView.showLoading(false);
                        mView.showError(exception.getMessage());
                    }
                });
    }

    @Override
    public String getImageProfile() {
        return SharedPreferenceHelper.getInstance().getImageProfile();
    }

    @Override
    public void start(){}

}
