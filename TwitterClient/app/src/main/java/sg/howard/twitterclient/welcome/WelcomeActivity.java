package sg.howard.twitterclient.welcome;

import android.os.Bundle;

import com.twitter.sdk.android.core.TwitterCore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import sg.howard.twitterclient.R;

public class WelcomeActivity extends AppCompatActivity implements WelcomeContract.View {

    WelcomeContract.Presenter presenter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        presenter = new WelcomePresenter(this, TwitterCore.getInstance().getSessionManager().getActiveSession());

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startGetUser();
    }

    @Override
    public void onGetUserSuccess() {

    }


    @Override
    public void setPresenter(WelcomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading(boolean isShow) {

    }

    @Override
    public void showError(String message) {

    }
}
