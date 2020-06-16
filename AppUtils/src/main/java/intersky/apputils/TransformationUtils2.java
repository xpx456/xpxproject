package intersky.apputils;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;

public class TransformationUtils2 extends ImageViewTarget<Bitmap> {
    private ImageView target;
    public TransformationUtils2(ImageView target) {
        super(target);
        this.target = target;
    }
    @Override
    protected void setResource(Bitmap resource) {
        view.setImageBitmap(resource);
        //获取原图的宽高
        int width = resource.getWidth();
        int height = resource.getHeight();
        //获取imageView的宽
        int imageViewWidth = target.getWidth();
        //计算图片等比例放大后的高
        int imageViewHeight = imageViewWidth/width*height;
        if(imageViewHeight < target.getHeight())
        {
            imageViewHeight = target.getHeight();
            imageViewWidth = imageViewHeight/height*width;
        }
        ViewGroup.LayoutParams params = target.getLayoutParams();
        params.width = imageViewWidth;
        params.height = imageViewHeight;
        target.setLayoutParams(params);
    }
}
