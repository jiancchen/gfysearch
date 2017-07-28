package app.jianchen.gfycatsearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import app.jianchen.gfycatsearch.api.API;
import app.jianchen.gfycatsearch.api.ApiRequestListener;
import app.jianchen.gfycatsearch.api.model.GfyCat;
import app.jianchen.gfycatsearch.auth.AuthToken;
import app.jianchen.gfycatsearch.ui.GfyViewerActivity;
import app.jianchen.gfycatsearch.ui.adapter.GfyAdapter;

/**
 * Class for searching
 * for now just grabs a new token every time the activity is opened
 */
public class GfySearchActivity extends AppCompatActivity {

    private AuthToken authToken;
    private EditText searchBox;
    private GridView gfyList;
    private GfyAdapter gfyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBox = (EditText) findViewById(R.id.et_search);

        searchBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    search(searchBox.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(searchBox.getText().toString().trim());
            }
        });

        gfyAdapter = new GfyAdapter(GfySearchActivity.this);

        gfyList = (GridView) findViewById(R.id.gv_gfylist);
        gfyList.setEmptyView(findViewById(R.id.iv_empty));

        gfyList.setAdapter(gfyAdapter);

        gfyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gfyIntent = new Intent(GfySearchActivity.this, GfyViewerActivity.class);
                gfyIntent.putExtra(GfyViewerActivity.EXTRA_GFY, Parcels.wrap(gfyAdapter.getItem(position)));
                startActivity(gfyIntent);
            }
        });

        /**
         * TODO: Store Client secret and Client Id is JNI or obfuscate instead of hardcode
         */
        API.getToken("client_id", "client_secret", new ApiRequestListener() {
            @Override
            public void onResponse(String result) {
                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<AuthToken> jsonAdapter = moshi.adapter(AuthToken.class);
                try {
                    authToken = jsonAdapter.fromJson(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                //GfyCat is currently down please try again later
            }
        });

    }

    private void search(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return;
        }

        //close keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        API.getSearch(keyword, 0, authToken, new ApiRequestListener() {
            @Override
            public void onResponse(String result) {
                Moshi moshi = new Moshi.Builder().build();

                Type listOfCardsType = Types.newParameterizedType(List.class, GfyCat.class);
                JsonAdapter<List<GfyCat>> jsonAdapter = moshi.adapter(listOfCardsType);
                try {
                    final List<GfyCat> gifs = jsonAdapter.fromJson(new JSONObject(result).getJSONArray("gfycats").toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gfyAdapter.setCats(gifs);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                //TODO: Failure message or retry a couple times and then failure message
            }
        });

        Toast.makeText(GfySearchActivity.this, "Searching: " + searchBox.getText().toString().trim(), Toast.LENGTH_SHORT).show();
    }
}
