package sg.howard.twitterclient.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import sg.howard.twitterclient.R;


public class ModalDialogFrament extends DialogFragment {

    private Context context;
    private int type;

    public static ModalDialogFrament newInstance(String data) {
        ModalDialogFrament dialog = new ModalDialogFrament();
        Bundle args = new Bundle();
        args.putString("link", data);
        dialog.setArguments(args);
        return dialog;
    }

    public void setContext(Context context, int type) {
        this.context = context;
        this.type = type;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (type == 0)
            return inflater.inflate(R.layout.activity_popup, container);
        else
            return inflater.inflate(R.layout.activity_compose_modal, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        if (type == 0) {
            String link = getArguments().getString("link", "");
            final ImageView imageView = view.findViewById(R.id.image_media);
            final ProgressBar progressBar = view.findViewById(R.id.load_image);
            Glide.with(context)
                    .load(link)
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
        }
        else {

        }


    }

}
