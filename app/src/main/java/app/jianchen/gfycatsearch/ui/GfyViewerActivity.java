package app.jianchen.gfycatsearch.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import app.jianchen.gfycatsearch.R;
import app.jianchen.gfycatsearch.api.model.GfyCat;

/**
 * Created by jianchen on 7/27/17.
 */

public class GfyViewerActivity extends Activity {

    private ImageView gfyImage;
    private TextView viewCount;
    public static final String EXTRA_GFY = "gfy";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gfycat_viewer_activity);

        GfyCat gfy = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_GFY));

        gfyImage = (ImageView) findViewById(R.id.iv_largeimage);
        viewCount = (TextView) findViewById(R.id.tv_viewcount);

        viewCount.setText(String.format(getString(R.string.view_format), gfy.getViews()));

        Glide.with(this).load(gfy.getGifUrl()).into(gfyImage);
    }

}
