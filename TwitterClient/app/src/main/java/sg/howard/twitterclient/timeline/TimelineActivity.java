package sg.howard.twitterclient.timeline;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.varunest.sparkbutton.SparkButton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import sg.howard.twitterclient.R;
import sg.howard.twitterclient.adapter.EndlessRecyclerViewScrollListener;
import sg.howard.twitterclient.adapter.TweetAdapter;
import sg.howard.twitterclient.compose.ComposeTweetActivity;
import sg.howard.twitterclient.util.AnimationUtil;

public class TimelineActivity extends AppCompatActivity implements TimelineContract.View {

    private static String TAG = TimelineActivity.class.getSimpleName();
    ImageView image_account;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView rvTimeline;
    ProgressBar loader;
    FloatingActionButton fab;
    FrameLayout cover;
    TimelineContract.Presenter presenter;
    TweetAdapter tweetAdapter;
    int pageCount;
    boolean blurred;
    private EndlessRecyclerViewScrollListener scrollListener;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        image_account = findViewById(R.id.image_account);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        rvTimeline = findViewById(R.id.rvTimeline);
        loader = findViewById(R.id.loader);
        fab = findViewById(R.id.fab);
        cover = findViewById(R.id.cover);

        presenter = new TimelinePresenter(this, TwitterCore.getInstance().getSessionManager().getActiveSession());
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_blue_bright)
        );

        fab.setVisibility(View.VISIBLE);
        cover.setVisibility(View.INVISIBLE);

        pageCount = 10;
        blurred = false;
        setupView();

        fab.setOnClickListener(view -> {

            //Play some animation
            AnimationUtil action = new AnimationUtil(this.findViewById(android.R.id.content));

            synchronized(action) {
                action.moveLeft();
            }

            new CountDownTimer(2000,2000){
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onTick(long millisUntilFinished){

                }

                @Override
                public void onFinish(){

                    Intent i = new Intent(TimelineActivity.this, ComposeTweetActivity.class);

                    View sharedView = image_account;
                    String transitionName = getString(R.string.image_target);

                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(TimelineActivity.this, sharedView, transitionName);
                    startActivity(i, transitionActivityOptions.toBundle());

                }
            }.start();

        });
    }

    private void setupView() {

        tweetAdapter = new TweetAdapter(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        DividerItemDecoration mDividerItem = new DividerItemDecoration(rvTimeline.getContext(),
                DividerItemDecoration.VERTICAL);
        rvTimeline.addItemDecoration(mDividerItem);
        rvTimeline.setLayoutManager(layoutManager);

        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(tweetAdapter);

        scaleAdapter.setDuration(500);

        rvTimeline.setAdapter(scaleAdapter);


        Glide.with(this).load(presenter.getImageProfile())
                .apply(new RequestOptions()
                        .fitCenter())
                .apply(RequestOptions.circleCropTransform())
                .into(image_account);


        //Action for load more
        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi();
            }

        };
        rvTimeline.addOnScrollListener(scrollListener);

        //Action for swipe-to-refresh-layout feature
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d("Refresh", "Involve");
            pageCount = 10;
            presenter.startGetTimeline(pageCount);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startGetTimeline(pageCount);
    }

    @Override
    public void setPresenter(TimelineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onGetStatusesSuccess(List<Tweet> data) {

        mSwipeRefreshLayout.setRefreshing(false);
        tweetAdapter.setData(data, 0);
        tweetAdapter.setListener(new TweetAdapter.TweetItemListener() {

            @Override
            public void onImage(View view, Tweet tweet) {

                final boolean wrapInScrollView = false;

                MaterialDialog.Builder builder = new MaterialDialog.Builder(TimelineActivity.this)
                        .customView(R.layout.activity_popup, wrapInScrollView);

                MaterialDialog dialog = builder.build();
                View viewPopup = dialog.getCustomView();

                final ImageView imageView = viewPopup.findViewById(R.id.image_media);
                final ProgressBar progressBar = viewPopup.findViewById(R.id.load_image);

                Glide.with(TimelineActivity.this)
                        .load(tweet.entities.media.get(0).mediaUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(imageView);


                dialog.show();
               /*
                Blurry.with(view.getContext())
                        .radius(25)
                        .sampling(1)
                        .color(Color.argb(66, 20, 23, 26))
                        .onto(findViewById(R.id.timeline_view));
                blurred = true;

                if (dialog.isCancelled()) {
                    Blurry.delete(findViewById(R.id.timeline_view));
                    blurred = false;
                }*/

            }

            @Override
            public void onTweet(View view, Tweet tweet) {

            }

            @Override
            public void onRetweet(View view, Tweet tweet) {
                ImageView image_tweet = view.findViewById(R.id.image_retweet);

                if (tweet.retweeted) {

                    image_tweet.setImageResource(R.drawable.ic_retweet);
                }
                else {

                    image_tweet.setImageResource(R.drawable.ic_retweet_active);
                }

            }

            @Override
            public void onLike(View view, Tweet tweet) {

                SparkButton sparkButton = view.findViewById(R.id.spark_button);
                TextView like_count = view.findViewById(R.id.count_like);
                //int count = Integer.parseInt(like_count.getText().toString().trim());

                if (!sparkButton.isChecked()) {

                    sparkButton.playAnimation();
                    sparkButton.setChecked(true);
                    //like_count.setText(" " + String.valueOf(count + 1));
                } else {
                    sparkButton.setChecked(false);
                    //like_count.setText(" " + String.valueOf(count - 1));
                }

            }

            @Override
            public void onShare(View view, Tweet tweet) {

            }

            @Override
            public void onTweetClick(Tweet tweet) {

                Toast.makeText(TimelineActivity.this, tweet.idStr, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void showLoading(boolean isShow) {
        loader.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void loadNextDataFromApi() {

        Log.d("Load more", "Triggered");
        pageCount = pageCount + 10;
        presenter.startGetTimeline(pageCount);
    }




}
