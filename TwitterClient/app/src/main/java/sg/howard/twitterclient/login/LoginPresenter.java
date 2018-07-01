package sg.howard.twitterclient.login;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterSession;

import sg.howard.twitterclient.util.SharedPreferenceHelper;

public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void saveResult(Result<TwitterSession> result) {
        SharedPreferenceHelper.getInstance().saveUser(result);
        view.saveUserSuccess();
    }

    @Override
    public void start() {

    }
}
