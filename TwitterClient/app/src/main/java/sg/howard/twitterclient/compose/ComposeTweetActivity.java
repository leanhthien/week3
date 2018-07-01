package sg.howard.twitterclient.compose;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import sg.howard.twitterclient.R;
import sg.howard.twitterclient.fragment.ModalDialogFrament;
import sg.howard.twitterclient.timeline.TimelineActivity;

public class ComposeTweetActivity extends AppCompatActivity implements ComposeContract.View{

    String DEFAULT_TEXT = "140 character left.";
    ImageView image_alter;
    Button btnSend;
    TextView leftChar;
    EditText edtCompose;
    ImageView imageView;
    ProgressBar loader;
    ComposeContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        presenter = new ComposeTweetPresenter(this, TwitterCore.getInstance().getSessionManager().getActiveSession());

        setupView();

        openPopup();

    }

    @Override
    public void setPresenter(ComposeContract.Presenter presenter) {
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

    @Override
    public void sendTweetSuccess(Result<Tweet> result) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, TimelineActivity.class));
    }

    public void setupView() {
        image_alter = findViewById(R.id.image_alter);

        Glide.with(ComposeTweetActivity.this)
                .load(presenter.getImageProfile())
                .apply(RequestOptions.circleCropTransform())
                .into(image_alter);
    }

    public void openPopup() {

        final boolean wrapInScrollView = false;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(ComposeTweetActivity.this)
                .customView(R.layout.activity_compose_modal, wrapInScrollView);

        MaterialDialog dialog = builder.build();
        View viewPopup = dialog.getCustomView();

        imageView = viewPopup.findViewById(R.id.image_account);
        edtCompose = viewPopup.findViewById(R.id.edtCompose);
        leftChar = viewPopup.findViewById(R.id.left_char);
        btnSend = viewPopup.findViewById(R.id.btnSend);
        loader = viewPopup.findViewById(R.id.loader);

        Glide.with(ComposeTweetActivity.this)
                .load(presenter.getImageProfile())
                .apply(RequestOptions.circleCropTransform())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        loader.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loader.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);

        leftChar.setText(DEFAULT_TEXT);

        edtCompose.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                String countChar = String.valueOf( 140 - s.length()) + "/140 characters left";
                leftChar.setText(countChar);

            }
        });

        btnSend.setOnClickListener(view -> presenter.sendTweet(edtCompose.getText().toString()));

        dialog.show();

    }
}
