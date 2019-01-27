package eu.quelltext.gita.activities;

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

import eu.quelltext.gita.R;
import eu.quelltext.gita.model.Chapter;

public class ChapterActivity extends Activity {

    public static final String CHAPTER_INDEX = "chapter_index";
    private ListView versesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        final Chapter chapter = new Chapter(this, getIntent().getIntExtra(CHAPTER_INDEX, 0));

        // ListView inspired by http://www.vogella.com/tutorials/AndroidListView/article.html
        versesList = findViewById(R.id.verses_list);
        final ArrayAdapter<Chapter.Verse> adapter = new ArrayAdapter<Chapter.Verse>(this, -1, chapter.allVerses()) {
            @Override
            public View getView(int position, View verseView, ViewGroup parent) {
                Chapter.Verse verse = getItem(position);
                if (verseView == null) {
                    LayoutInflater inflater = (LayoutInflater) ChapterActivity.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert inflater != null;
                    verseView = inflater.inflate(R.layout.verse_list_element, parent, false);
                }
                TextView indexText = verseView.findViewById(R.id.text_verse_number);
                TextView contentText = verseView.findViewById(R.id.text_verse);
                assert verse != null;
                indexText.setText(verse.getIndexString());
                contentText.setText(verse.getMeaning());
                return verseView;
            }
        };
        versesList.setAdapter(adapter);
        versesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Chapter.Verse chapter = (Chapter.Verse)versesList.getItemAtPosition(i);
                System.out.println("Clicked Verse: " + chapter.getIndexString());
            }
        });
    }
}
