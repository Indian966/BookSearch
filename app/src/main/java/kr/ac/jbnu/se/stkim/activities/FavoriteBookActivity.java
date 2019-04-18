package kr.ac.jbnu.se.stkim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import kr.ac.jbnu.se.stkim.R;
import kr.ac.jbnu.se.stkim.adapters.BookAdapter;
import kr.ac.jbnu.se.stkim.models.Book;

public class FavoriteBookActivity extends AppCompatActivity {

    public static final String BOOK_DETAIL_KEY = "book";
    private ListView lvBooks;
    private BookAdapter bookAdapter;
    ArrayList<Book> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        lvBooks = (ListView) findViewById(R.id.lvBooks);
        ArrayList<Book> aBooks = new ArrayList<Book>();
        // initialize the adapter
        bookAdapter = new BookAdapter(this, aBooks);
        // attach the adapter to the ListView
        lvBooks.setAdapter(bookAdapter);
        setupBookSelectedListener();
        adaptFavoriteBook();

    }

    public void setupBookSelectedListener() {
        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch the detail view passing book as an extra
                Intent intent = new Intent(FavoriteBookActivity.this, BookDetailActivity.class);
                intent.putExtra(BOOK_DETAIL_KEY, bookAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    private void adaptFavoriteBook(){
        bookAdapter.clear();
        readFavoriteBookFile();
        if(books.isEmpty()){
            Toast.makeText(getApplicationContext(),"즐겨찾기된 책이 없습니다",0).show();
        }
        for(Book book : books){
            bookAdapter.add(book);
        }

    }

    private void readFavoriteBookFile(){
        String path = getFilesDir().getAbsolutePath();
        File file = new File(path);
        File[] filelist = file.listFiles();

        String[] filename = new String[filelist.length];
        String title = new String();
        String author = new String();
        String coverUrl = new String();
        String openLibery = new String();
        String[] tmp = new String[4];

        for(int i=0; i<filelist.length; i++){
            Book book = new Book();
            filename[i] = filelist[i].getName();
            try {
                FileInputStream fis = new FileInputStream(file+"/"+filename[i]);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));

                tmp = br.readLine().split("@@");
                title = tmp[0];
                author = tmp[1];
                coverUrl = tmp[2];
                openLibery = tmp[3];

                br.close();
                fis.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"불러오기 오류",0).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            book.setTitle(title);
            book.setAuthor(author);
            book.setCoverUrl(coverUrl);
            book.setOpenLibraryId(openLibery);
            books.add(book);
        }

    }






}




//
//    }
