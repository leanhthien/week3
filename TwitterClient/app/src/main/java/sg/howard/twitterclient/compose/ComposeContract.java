package sg.howard.twitterclient.compose;

import android.text.Editable;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.models.Tweet;

import sg.howard.twitterclient.base.BasePresenter;
import sg.howard.twitterclient.base.BaseView;

public interface ComposeContract {
    interface View extends BaseView<Presenter>{

        void sendTweetSuccess(Result<Tweet> result);
    }

    interface Presenter extends BasePresenter{

        void sendTweet(String text);
    }
}
