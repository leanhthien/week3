package sg.howard.twitterclient.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import sg.howard.twitterclient.R;
import sg.howard.twitterclient.timeline.TimelineActivity;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {


    TwitterLoginButton loginButton;
    MaterialButton alterButton;
    protected ProgressBar loader;
    LoginContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loader = findViewById(R.id.loader);
        loginButton = findViewById(R.id.login_button);
        alterButton = findViewById(R.id.alter_button);

        presenter = new LoginPresenter(this);
        setUpView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null){
            saveUserSuccess();
        }
    }

    private void setUpView() {

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                presenter.saveResult(result);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("TwitterClient", "Login failed");
                clearTwitter();
                endAuthorizeInProgress();
            }
        });

    }

    private void clearTwitter() {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }

    @Override
    public void saveUserSuccess() {

        setContentView(R.layout.activity_welcome);

        //Display the logo during 5 seconds,
        new CountDownTimer(3000,3000){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTick(long millisUntilFinished){

            }

            @Override
            public void onFinish(){
                startActivity(new Intent(LoginActivity.this, TimelineActivity.class));
                finish();

            }
        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            final TwitterAuthClient twitterAuthClient = new TwitterAuthClient();
            if(twitterAuthClient.getRequestCode()==requestCode) {
                Boolean twitterLoginWasCanceled = (resultCode == RESULT_CANCELED);
                if(!twitterLoginWasCanceled)
                    loginButton.onActivityResult(requestCode, resultCode, data);
                else
                    endAuthorizeInProgress();
            }
        } catch (TwitterAuthException exception) {
            clearTwitter();
        }
    }
    private void endAuthorizeInProgress() {
        try {
            final TwitterAuthClient twitterAuthClient = new TwitterAuthClient();
            Field authStateField = twitterAuthClient.getClass().getDeclaredField("authState");
            authStateField.setAccessible(true);
            Object authState = authStateField.get(twitterAuthClient);
            Method endAuthorize = authState.getClass().getDeclaredMethod("endAuthorize");
            endAuthorize.invoke(authState);
        } catch (NoSuchFieldException | SecurityException | InvocationTargetException |
                NoSuchMethodException | IllegalAccessException e) {
            Log.e("TwitterClient","Couldn't end authorize in progress.", e);
        }
    }
    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading(boolean isShow) {
        loader.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
