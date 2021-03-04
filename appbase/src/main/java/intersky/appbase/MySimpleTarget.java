package intersky.appbase;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class MySimpleTarget extends SimpleTarget<Drawable> {

    public ImageView mImageView;
    public MySimpleTarget(ImageView mImageView){
        this.mImageView = mImageView;
    }

    @Override
    public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
        this.mImageView.setImageDrawable(resource);
    }
}
