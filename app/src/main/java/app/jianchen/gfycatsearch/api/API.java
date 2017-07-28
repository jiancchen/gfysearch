package app.jianchen.gfycatsearch.api;

import java.io.IOException;

import app.jianchen.gfycatsearch.auth.AuthToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jianchen on 7/27/17.
 */

/**
 * Class to access the gfycat API, all calls are made async
 */
public class API {

    private static final String SCHEME = "https";
    private static final String HOST = "api.gfycat.com";

    //TODO: Configure using build variants/flavors
    private static final String ENV_PROD = "v1";
    private static final String ENV_TEST = "v1test";

    private static OkHttpClient client = new OkHttpClient();

    //async request made ehre
    private static final void getResultFromAPI(Request request, final ApiRequestListener listener) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onFailure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (listener != null) {
                    listener.onResponse(response.body().string());
                }
            }
        });
    }

    /**
     * Request Auth token from gfycat
     *
     * Returns JSON Object:
     * { "token_type": "bearer", "scope": "", "expires_in": 3600, "access_token": "your_token"}
     *
     * @param clientId - assigned client ID
     * @param clientSecret - assigned client Secret
     * @param listener - ApiRequestListener callback for request results
     */
    public static void getToken(String clientId, String clientSecret, final ApiRequestListener listener) {

        HttpUrl getTokenUrl = new HttpUrl.Builder()
                .scheme(SCHEME)
                .host(HOST)
                .addPathSegment(ENV_PROD).addPathSegment("oauth").addPathSegment("token")
                .addQueryParameter("grant_type", "client_credentials")
                .addQueryParameter("client_id", clientId)
                .addQueryParameter("client_secret", clientSecret)
                .build();

        Request tokenRequest = new Request.Builder()
                .url(getTokenUrl).build();

        getResultFromAPI(tokenRequest, listener);
    }

    /**
     * Search returning GfyCat Objects
     *
     * @param keyword - keyword to search
     * @param count - number of objects to return. 0 returns default count of 100 results
     * @param token - auth token
     * @param listener - request listener
     */
    public static void getSearch(String keyword, int count, AuthToken token, final ApiRequestListener listener) {

        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalStateException("Cannot search with no keyword");
        }

        HttpUrl getSearchUrl = new HttpUrl.Builder()
                .scheme(SCHEME)
                .host(HOST)
                .addPathSegment(ENV_PROD).addPathSegment("gfycats").addPathSegment("search")
                .addQueryParameter("search_text", keyword)
                .addQueryParameter("count", String.valueOf(count == 0 ? 100 : count))
                .build();

        Request searchRequest = new Request.Builder()
                .url(getSearchUrl)
                .addHeader("Authorization", "Bearer " + token.getToken())
                .build();

        getResultFromAPI(searchRequest, listener);

    }
}
