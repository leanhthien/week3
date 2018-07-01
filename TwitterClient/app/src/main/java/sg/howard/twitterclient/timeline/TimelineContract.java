package sg.howard.twitterclient.timeline;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import sg.howard.twitterclient.base.BasePresenter;
import sg.howard.twitterclient.base.BaseView;

public interface TimelineContract {
    interface View extends BaseView<Presenter>{

        void onGetStatusesSuccess(List<Tweet> data);
    }

    interface Presenter extends BasePresenter{

        void startGetTimeline(int page);

        String getImageProfile();

    }
}
