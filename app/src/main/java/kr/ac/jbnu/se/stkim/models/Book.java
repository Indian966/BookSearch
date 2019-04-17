package kr.ac.jbnu.se.stkim.models;

import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private String openLibraryId;
    private String author;
    private String title;
    private String coverURL;

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    // Get medium sized book cover from covers API
    public String getCoverUrl() {
        return coverURL;
    }

    // Returns a Book given the expected JSON
    public static Book fromJson(JSONObject jsonObject) {
        Book book = new Book();
        try {
            // Deserialize json into object fields
            // Check if a cover edition is available
            if (jsonObject.has("isbn")) {
                book.openLibraryId = jsonObject.getString("isbn");
            }

            // 한글 Tag처리
            String orginalTitle = jsonObject.has("title") ? jsonObject.getString("title") : "";
            String unEscapedXMLTitle = StringEscapeUtils.unescapeXml(orginalTitle);
            String cleanXMLTitle = unEscapedXMLTitle.replaceAll("<[^>]+>", "");
            Log.d("tag", "cleanXML :" + cleanXMLTitle);

            book.title = cleanXMLTitle;
            book.author = jsonObject.getString("authors");
            book.coverURL = jsonObject.getString("thumbnail");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return book;
    }

    // Decodes array of book json results into business model objects
    public static ArrayList<Book> fromJson(JSONArray jsonArray) {
        ArrayList<Book> books = new ArrayList<Book>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Book book = Book.fromJson(bookJson);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }
}
