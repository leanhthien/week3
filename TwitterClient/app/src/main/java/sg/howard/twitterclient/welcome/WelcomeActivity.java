package sg.howard.twitterclient.welcome;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.john.waveview.WaveView;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import sg.howard.twitterclient.R;
import sg.howard.twitterclient.login.LoginActivity;
import sg.howard.twitterclient.timeline.TimelineActivity;

public class WelcomeActivity extends AppCompatActivity implements WelcomeContract.View {

    WelcomeContract.Presenter presenter;
    private ImageView image_account;
    private TextView name_account;
    ProgressBar loader;
    private WaveView waveView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        image_account = findViewById(R.id.image_account);
        name_account = findViewById(R.id.name_account);
        loader = findViewById(R.id.loader);

        presenter = new WelcomePresenter(this, TwitterCore.getInstance().getSessionManager().getActiveSession());

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startGetUser();
    }

    @Override
    public void onGetUserSuccess(Tweet data) {

        String tagName = "Welcome ";
        waveView = findViewById(R.id.wave_view);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(4f);
        circularProgressDrawable.setCenterRadius(35f);
        circularProgressDrawable.setColorSchemeColors(android.R.color.holo_orange_light);
        circularProgressDrawable.start();

        if (data.user.profileImageUrl != null) {
            Glide.with(this).load(presenter.getImageProfile())
                    .apply(new RequestOptions()
                            .placeholder(circularProgressDrawable)
                            .fitCenter())
                    .apply(RequestOptions.circleCropTransform())
                    .into(image_account);
        }

        tagName = tagName + data.user.name + " to TwitterClient!";
        name_account.setText(tagName);

        new CountDownTimer(5000,100){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTick(long millisUntilFinished){

                waveView.setProgress((int)((5000 - millisUntilFinished)/1000)*10);

            }

            @Override
            public void onFinish(){

                 Intent i = new Intent(WelcomeActivity.this, TimelineActivity.class);

                 View sharedView = image_account;
                 String transitionName = getString(R.string.image_target);

                 ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this, sharedView, transitionName);
                    startActivity(i, transitionActivityOptions.toBundle());

                startActivity(new Intent(WelcomeActivity.this, TimelineActivity.class));

            }
        }.start();


        //startActivity(new Intent(WelcomeActivity.this, TimelineActivity.class));
    }


    @Override
    public void setPresenter(WelcomeContract.Presenter presenter) {
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
