package sg.howard.twitterclient.welcome;

import com.twitter.sdk.android.core.models.Tweet;

import sg.howard.twitterclient.base.BasePresenter;
import sg.howard.twitterclient.base.BaseView;

public interface WelcomeContract {
    interface View extends BaseView<WelcomeContract.Presenter> {

        void onGetUserSuccess(Tweet data);
    }

    interface Presenter extends BasePresenter {

        void startGetUser();

        String getImageProfile();

    }
}
