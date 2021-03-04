package xpx.map.entity;

import android.view.View;
import android.widget.ImageView;

import com.amap.api.services.core.PoiItem;

public class MapAddress {
    public boolean select = false;
    public PoiItem poiItem;
    public ImageView view;
    public MapAddress(PoiItem poiItem) {
        this.poiItem = poiItem;
    }
}
