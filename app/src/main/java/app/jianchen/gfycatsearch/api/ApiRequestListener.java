package app.jianchen.gfycatsearch.api;

/**
 * Created by jianchen on 7/27/17.
 */

public interface ApiRequestListener {
    void onResponse(String result);
    void onFailure();
}
