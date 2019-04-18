package ms.ac.jbnu.se.mschoi.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ms.ac.jbnu.se.mschoi.R;
import ms.ac.jbnu.se.mschoi.models.Book;
import ms.ac.jbnu.se.mschoi.net.AsyncHttpTask;
import ms.ac.jbnu.se.mschoi.net.BookClient;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvDescription;
    private TextView tvPageCount;
    private BookClient client;
    private Button purchaseButton;
    private Button bookMarkButton;
    private Button favoriteBookButton;
    private Boolean isBookMark = false;
    private File file;
    private Book favoriteBook;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // Fetch views
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvPublisher = (TextView) findViewById(R.id.tvPublisher);
        tvDescription = (TextView) findViewById(R.id.description_text);
        tvPageCount = (TextView) findViewById(R.id.tvPageCount);
        purchaseButton = (Button) findViewById(R.id.purchaseButton);
        bookMarkButton = (Button) findViewById(R.id.bookMarkButton);

        // Use the book to populate the data into our views
        Book book = (Book) getIntent().getSerializableExtra(BookListActivity.BOOK_DETAIL_KEY);
        loadBook(book);
        checkBookMark(book);

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.aladin.co.kr/m/msearch.aspx?SearchWord=" + tvTitle.getText().toString() + "&SearchTarget=All"));
                startActivity(intent);
            }
        });

        bookMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBookMark)
                    removeBookMark();
                else
                    addBookMark();
            }
        });

    }

    private void checkBookMark(Book book) {
        favoriteBook = book;
        filename = book.getTitle();
        String path = getFilesDir().getAbsolutePath() + "/" + filename + ".txt";
        file = new File(path);
        FileInputStream fins;
        try {
            fins = new FileInputStream(path);
            isBookMark = true;
            fins.close();
        } catch (IOException e) {
            e.printStackTrace();
            isBookMark = false;
        }
    }

    private void addBookMark() {
        String title = favoriteBook.getTitle();
        String athor = favoriteBook.getAuthor();
        String coverUrl = favoriteBook.getCoverUrl();
        String openLibraryId = favoriteBook.getOpenLibraryId();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(title + "@@" + athor + "@@" + coverUrl + "@@" + openLibraryId);
            Toast.makeText(getApplicationContext(), "즐겨찾기 추가되었습니다", Toast.LENGTH_SHORT).show();
            bw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "즐겨찾기 오류", Toast.LENGTH_SHORT).show();
        }
        isBookMark = true;
    }

    private void removeBookMark() {
        file.delete();
        Toast.makeText(getApplicationContext(), "즐겨찾기 삭제되었습니다", Toast.LENGTH_SHORT).show();
        isBookMark = false;
    }


    // Populate data for the book
    private void loadBook(Book book) {
        //change activity title
        this.setTitle(book.getTitle());
        // Populate data
        Picasso.with(this).load(Uri.parse(book.getCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        // fetch extra book data from books API
        client = new BookClient();
        requestLibraryBook(this, book.getTitle());
//        client.getExtraBookDetails(book.getOpenLibraryId(), new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                try {
//                    if (response.has("publishers")) {
//                        // display comma separated list of publishers
//                        final JSONArray publisher = response.getJSONArray("publishers");
//                        final int numPublishers = publisher.length();
//                        final String[] publishers = new String[numPublishers];
//                        for (int i = 0; i < numPublishers; ++i) {
//                            publishers[i] = publisher.getString(i);
//                        }
//                        tvPublisher.setText(TextUtils.join(", ", publishers));
//                    }
//                    if (response.has("number_of_pages")) {
//                        tvPageCount.setText(Integer.toString(response.getInt("number_of_pages")) + " pages");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share) {
            setShareIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent() {
        ImageView ivImage = (ImageView) findViewById(R.id.ivBookCover);
        final TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(ivImage);
        // Construct a ShareIntent with link to image
        Intent shareIntent = new Intent();
        // Construct a ShareIntent with link to image
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, (String) tvTitle.getText());
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        // Launch share menu
        startActivity(Intent.createChooser(shareIntent, "Share Image"));

    }

    // Returns the URI path to the Bitmap displayed in cover imageview
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    public void requestLibraryBook(Context cx, String title) {

//


        new AsyncHttpTask(cx, "https://dl.jbnu.ac.kr/eds/brief/integrationResult?x=0&y=0&st=KWRD&si=TOTAL&lmtst=OR&lmt0=TOTAL&q=" + title, mHandler, null,
                null, null, 1, 0);
    }


    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout


            if (msg.what == 1) {

                Log.d("ASDFASDF", msg.obj.toString());
try {
    Document doc = Jsoup.parseBodyFragment(msg.obj.toString());
    Element body = doc.body();
    Element lib = doc.getElementsByClass("briefDeFont").first();


    tvDescription.setText("전북대 도서관\n" + lib.text());
} catch (Exception e){
    tvDescription.setText("전북대 도서관\n" + "전북대 도서관에 없는 책");
}
            }


        }
    };


}
