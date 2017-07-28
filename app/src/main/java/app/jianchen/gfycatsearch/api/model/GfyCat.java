package app.jianchen.gfycatsearch.api.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jianchen on 7/27/17.
 */

@org.parceler.Parcel
public class GfyCat {

    public String gfyId;
    public String gifUrl;
    public String max1mbGif;
    public String gif100px;
    public String title;
    public int views;

    public GfyCat() {
    }

    public GfyCat(String gfyId, String gifUrl, String max1mbGif, String gif100px, String title, int views) {
        this.gfyId = gfyId;
        this.gifUrl = gifUrl;
        this.max1mbGif = max1mbGif;
        this.gif100px = gif100px;
        this.title = title;
        this.views = views;
    }

    public String getGfyId() {
        return gfyId;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public String getMax1mbGif() {
        return max1mbGif;
    }

    public String getTitle() {
        return title;
    }

    public int getViews() {
        return views;
    }

    public String getGif100px() {
        return gif100px;
    }
}
