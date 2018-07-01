package sg.howard.twitterclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.twitter.sdk.android.core.models.Tweet;
import com.varunest.sparkbutton.SparkButton;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import sg.howard.twitterclient.R;
import sg.howard.twitterclient.util.TimelineConverter;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweet;
    private Context mContext;
    private TweetItemListener mTweetListener;

    public TweetAdapter(Context context, List<Tweet> News, TweetItemListener itemListener) {
        mTweet = News;
        mContext = context;
        mTweetListener = itemListener;
    }

    public TweetAdapter(Context ctx) {

        mTweet = new ArrayList<>();
        this.mContext = ctx;
    }

    @Override
    @NonNull
    public TweetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View postView;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        if (viewType == 0)
            postView = inflater.inflate(R.layout.item_tweet_media, parent, false);
        else
            postView = inflater.inflate(R.layout.item_tweet, parent, false);


        return new ViewHolder(postView, this.mTweetListener);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final String link_thumbnail;
        String name;
        String name_profile;
        String time;
        Spannable tweet_detail;
        String retweet_count;
        String like_count;

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
        circularProgressDrawable.setStrokeWidth(4f);
        circularProgressDrawable.setCenterRadius(35f);
        circularProgressDrawable.setColorSchemeColors(android.R.color.holo_orange_light);
        circularProgressDrawable.start();

        Tweet tweet = mTweet.get(position);

        if (tweet.user.profileImageUrl != null)
            link_thumbnail = setupLink(tweet.user.profileImageUrl);
        else
            link_thumbnail = "";
        Log.d("link",link_thumbnail);

        if (tweet.user.name.length() > 20)
            name = tweet.user.name.substring(0, 20) + "... ";
        else
            name = tweet.user.name + " ";

        if (tweet.user.screenName.length() > 20)
            name_profile = " @" + tweet.user.screenName.substring(0, 20) + "... ";
        else
            name_profile = " @" + tweet.user.screenName + " ";

        time = "• " + TimelineConverter.dateToAge(tweet.createdAt, DateTime.now());

        //For tweet detail
        tweet_detail = setColor(tweet);

        //For verified icon
        if (tweet.user.verified)
            holder.image_verified.setVisibility(View.VISIBLE);

        //For retweet icon
        if (tweet.retweeted)
            holder.image_retweet.setImageResource(R.drawable.ic_retweet_active);

        //For like icon
        if (tweet.favorited)
            holder.spark_button.setChecked(true);

        retweet_count = " " + String.valueOf(tweet.retweetCount);
        like_count = " " + String.valueOf(tweet.favoriteCount);

        holder.name.setText(name);
        holder.name_profile.setText(name_profile);
        holder.time.setText(time);
        holder.tweet.setText(tweet_detail);
        holder.retweet.setText(retweet_count);
        holder.like.setText(like_count);

        Glide.with(mContext).load(link_thumbnail)
                .apply(new RequestOptions()
                        .placeholder(circularProgressDrawable)
                        .fitCenter())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.image_profile);

        if (tweet.entities.media.size() > 0) {
            Glide.with(mContext).load(tweet.entities.media.get(0).mediaUrl)
                    .apply(new RequestOptions()
                            .placeholder(circularProgressDrawable)
                            .fitCenter())
                    .into(holder.image_tweet);
        }

    }

    @Override
    public int getItemCount() {
        return mTweet.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (mTweet.get(position).entities.media.size() > 0)
            return 0;
        else
            return 1;

    }

    public void setData(List<Tweet> data, int type) {

        if (type == 0) {
            mTweet.clear();
            mTweet.addAll(data);
            notifyDataSetChanged();
        } else {
            mTweet.addAll(data);
            notifyItemInserted(data.size() - 1);
        }

    }

    public void setListener(TweetItemListener listener) {
        this.mTweetListener = listener;
    }

    private Tweet getItem(int adapterPosition) {
        return mTweet.get(adapterPosition);
    }

    public interface TweetItemListener {

        void onImage(View view, Tweet tweet);

        void onTweet(View view, Tweet tweet);

        void onRetweet(View view, Tweet tweet);

        void onLike(View view, Tweet tweet);

        void onShare(View view, Tweet tweet);

        void onTweetClick(Tweet tweet);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TweetItemListener mTweetListener;

        private ImageView image_profile;

        private TextView name;
        private ImageView image_verified;
        private TextView name_profile;
        private TextView time;

        private TextView tweet;
        @Nullable
        private ImageView image_tweet;

        private TextView retweet;
        private TextView like;

        private ImageView image_tweet_reply;
        private ImageView image_retweet;
        private SparkButton spark_button;
        private ImageView image_share;


        private ViewHolder(View itemView, TweetItemListener NewItemListener) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            name = itemView.findViewById(R.id.name);
            image_verified = itemView.findViewById(R.id.image_verified);
            name_profile = itemView.findViewById(R.id.name_profile);
            time = itemView.findViewById(R.id.time);
            tweet = itemView.findViewById(R.id.tweet);
            image_tweet = itemView.findViewById(R.id.image_thumbnail);
            retweet = itemView.findViewById(R.id.count_retweet);
            like = itemView.findViewById(R.id.count_like);

            image_tweet_reply = itemView.findViewById(R.id.image_tweet);
            image_retweet = itemView.findViewById(R.id.image_retweet);
            spark_button = itemView.findViewById(R.id.spark_button);
            image_share = itemView.findViewById(R.id.image_share);


            this.mTweetListener = NewItemListener;

            itemView.setOnClickListener(this);

            if (image_tweet != null)
                image_tweet.setOnClickListener(this);

            image_tweet_reply.setOnClickListener(this);
            image_retweet.setOnClickListener(this);
            spark_button.setOnClickListener(this);
            image_share.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.image_thumbnail:
                    this.mTweetListener.onImage(view, getItem(getAdapterPosition()));
                    break;
                case R.id.image_tweet:
                    this.mTweetListener.onTweet(view, getItem(getAdapterPosition()));
                    break;
                case R.id.image_retweet:
                    this.mTweetListener.onRetweet(view, getItem(getAdapterPosition()));
                    break;
                case R.id.spark_button:
                    this.mTweetListener.onLike(view, getItem(getAdapterPosition()));
                    break;
                case R.id.image_share:
                    this.mTweetListener.onShare(view, getItem(getAdapterPosition()));
                    break;
                default:
                    this.mTweetListener.onTweetClick(getItem(getAdapterPosition()));
                    break;

            }
            notifyDataSetChanged();
        }
    }

    private Spannable setColor(Tweet tweet) {

        Spannable wordtoSpan = new SpannableString(tweet.text);

        for (int i=0; i<tweet.entities.hashtags.size(); i++) {
            wordtoSpan.setSpan(
                    new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimaryTwitter)),
                    tweet.entities.hashtags.get(i).indices.get(0),
                    tweet.entities.hashtags.get(i).indices.get(1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        for (int i=0; i<tweet.entities.userMentions.size(); i++) {
            wordtoSpan.setSpan(
                    new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimaryTwitter)),
                    tweet.entities.userMentions.get(i).indices.get(0),
                    tweet.entities.userMentions.get(i).indices.get(1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return wordtoSpan;
    }

    private String setupLink(String url) {
        int pos;
        Log.d("Original link",url);
        if (url.contains("_normal.jpeg")) {
            pos = url.indexOf("_normal.jpeg");
            return url.substring(0,pos)+".jpeg";
        }
        else if (url.contains("_normal.png")) {
            pos = url.indexOf("_normal.png");
            return url.substring(0,pos)+".png";
        }
        else if (url.contains("_normal.jpg")) {
            pos = url.indexOf("_normal.jpg");
            return url.substring(0,pos)+".jpg";
        }
        return url;

    }

}


