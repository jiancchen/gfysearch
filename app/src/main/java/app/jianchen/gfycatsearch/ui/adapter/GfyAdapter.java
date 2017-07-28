package app.jianchen.gfycatsearch.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.jianchen.gfycatsearch.R;
import app.jianchen.gfycatsearch.api.model.GfyCat;

/**
 * Created by jianchen on 7/27/17.
 */

/**
 * Adapter for gifs, no placeholders currently for loading images
 */
public class GfyAdapter extends BaseAdapter {

    private Context context;

    public GfyAdapter(Context context) {
        this.context = context;
    }

    private List<GfyCat> cats;

    public void setCats(List<GfyCat> cats) {
        this.cats = cats;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cats == null ? 0 : cats.size();
    }

    @Override
    public GfyCat getItem(int position) {
        return cats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gfycat_item, null);
            viewholder = new ViewHolder();
            viewholder.imageOne = (ImageView) convertView.findViewById(R.id.iv_gfy);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(getItem(position).getGifUrl()).into(viewholder.imageOne);

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageOne;
    }
}
