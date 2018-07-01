package sg.howard.twitterclient.welcome;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import androidx.annotation.NonNull;

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

    }

    @Override
    public void start() {

    }
}
