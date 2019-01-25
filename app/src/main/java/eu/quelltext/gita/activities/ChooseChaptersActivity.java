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
import eu.quelltext.gita.model.Chapter;

import eu.quelltext.gita.R;

public class ChooseChaptersActivity extends Activity {

    private ListView chaptersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_chapters);
        // ListView inspired by http://www.vogella.com/tutorials/AndroidListView/article.html
        chaptersList = (ListView) findViewById(R.id.chapters_list);
        final ArrayAdapter<Chapter> adapter = new ArrayAdapter<Chapter>(this, -1, Chapter.all(this)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Chapter chapter = getItem(position);
                LayoutInflater inflater = (LayoutInflater) ChooseChaptersActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View chapterView = inflater.inflate(R.layout.chapter_list_element, parent, false);
                TextView indexText = chapterView.findViewById(R.id.text_index);
                TextView titleText = chapterView.findViewById(R.id.text_title);
                indexText.setText(Integer.toString(chapter.getIndex()));
                titleText.setText(chapter.getTitle());
                System.out.println("getView");
                return chapterView;
            }
        };
        chaptersList.setAdapter(adapter);
        chaptersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Chapter chapter = (Chapter)chaptersList.getItemAtPosition(i);
                System.out.println("Clicked: " + chapter.getTitle());
            }
        });
    }
}
