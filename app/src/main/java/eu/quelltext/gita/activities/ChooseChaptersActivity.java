package eu.quelltext.gita.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import eu.quelltext.gita.model.Chapter;

import eu.quelltext.gita.R;

public class ChooseChaptersActivity extends Activity {

    private ListView chaptersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_chapters);
        // ListView inspired by http://www.vogella.com/tutorials/AndroidListView/article.html
        chaptersList = findViewById(R.id.chapters_list);
        final ArrayAdapter<Chapter> adapter = new ArrayAdapter<Chapter>(this, -1, Chapter.all(this)) {
            @SuppressLint("SetTextI18n")
            @Override
            public View getView(int position, View chapterView, ViewGroup parent) {
                Chapter chapter = getItem(position);
                if (chapterView == null) {
                    LayoutInflater inflater = (LayoutInflater) ChooseChaptersActivity.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert inflater != null;
                    chapterView = inflater.inflate(R.layout.chapter_list_element, parent, false);
                }
                TextView indexText = chapterView.findViewById(R.id.text_index);
                TextView titleText = chapterView.findViewById(R.id.text_title);
                assert chapter != null;
                indexText.setText(Integer.toString(chapter.getIndex()));
                titleText.setText(chapter.getTitle());
                return chapterView;
            }
        };
        chaptersList.setAdapter(adapter);
        chaptersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Chapter chapter = (Chapter)chaptersList.getItemAtPosition(i);
                System.out.println("Clicked Chapter: " + chapter.getTitle());
                chapter.openAsActivity(ChooseChaptersActivity.this);
            }
        });
    }
}
