package kr.ac.jbnu.se.stkim.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BookClient {
    private static final String API_BASE_URL = "https://dapi.kakao.com/";
    private static final String API_KEY="KakaoAK 2703c3633aeeeb696e02603f4e2a8a68";

    private AsyncHttpClient client;

    public BookClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getBooks(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("/v3/search/book?target=title&query=");
            client.addHeader("Authorization",API_KEY);
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
