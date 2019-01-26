package eu.quelltext.gita.model;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import eu.quelltext.gita.activities.ChapterActivity;

public class Chapter {
    private final int index;
    private final String title;
    private final Context context;

    public static List<Chapter> all(Context context) {
        List<Chapter> chapters = new ArrayList<>();
        for (int i = 1; true; i++) {
            try {
                Chapter chapter = new Chapter(context, i);
                chapters.add(chapter);
            } catch (Resources.NotFoundException e) {
                break;
            }
        }
        return chapters;
    }

    public Chapter (Context context, int index) {
        this.index = index;
        this.context = context;
        this.title = getStringResourceByName("chapter_" + index + "_title");
    }


    public String getTitle() {
        return title;
    }

    public int getIndex() {
        return index;
    }

    private String getStringResourceByName(String aString) {
        // from https://stackoverflow.com/a/11595723
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    public void openAsActivity(Context context) {
        Intent myIntent = new Intent(context, ChapterActivity.class);
        myIntent.putExtra(ChapterActivity.CHAPTER_INDEX, this.getIndex()); //Optional parameters
        context.startActivity(myIntent);
    }

    public List<Verse> allVerses() {
        ArrayList<Verse> verses = new ArrayList<>();
        for (int start = 1; true; start++) {
            boolean aVerseWasAdded = false;
            int stop;
            for (stop = start; stop < start + Verse.MAX_NUMBER_OF_UNITED_VERSES; stop++) {
                Verse verse;
                try {
                    verse = new Verse(start, stop);
                } catch (Resources.NotFoundException e) {
                    continue;
                }
                verses.add(verse);
                aVerseWasAdded = true;
                break;
            }
            start = stop;
            if (!aVerseWasAdded) {
                break;
            }
        }
        return verses;
    }

    public class Verse {

        static final int MAX_NUMBER_OF_UNITED_VERSES = 10;
        private final int start;
        private final int stop;
        private final String text;

        private Verse(int start, int stop) {
            this.start = start;
            this.stop = stop;
            this.text = getMeaning();
        }

        boolean hasMultipleVerses() {
            return start != stop;
        }

        public String getIndexString() {
            return hasMultipleVerses() ? Integer.toString(start) + "-" + Integer.toString(stop) : Integer.toString(start);
        }

        public String getText() {
            return text;
        }

        private String getMeaning() {
            String verseId = hasMultipleVerses() ? Integer.toString(start) + "_" + Integer.toString(stop) : Integer.toString(start);
            return getStringResourceByName(
                    "chapter_" + Integer.toString(Chapter.this.getIndex()) +
                            "_verse_" + verseId +
                            "_meaning");
        }
    }
}