package sg.howard.twitterclient.welcome;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import sg.howard.twitterclient.base.BasePresenter;
import sg.howard.twitterclient.base.BaseView;
import sg.howard.twitterclient.timeline.TimelineContract;

public interface WelcomeContract {
    interface View extends BaseView<WelcomeContract.Presenter> {
        void onGetUserSuccess();
    }

    interface Presenter extends BasePresenter {

        void startGetUser();
    }
}
