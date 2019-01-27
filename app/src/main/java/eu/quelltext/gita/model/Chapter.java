package eu.quelltext.gita.model;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import eu.quelltext.gita.activities.ChapterActivity;

public class Chapter {
    private final int index;
    private final Context context;

    private static final int[] VERSE_COUNT = {47, 72, 43, 42, 29, 47, 30, 28, 34, 42, 55, 20, 35, 27, 20, 24, 28, 78};

    public static List<Chapter> all(Context context) {
        List<Chapter> chapters = new ArrayList<>();
        for (int i = 1; i <= 18; i++) {
            Chapter chapter = new Chapter(context, i);
            chapters.add(chapter);
        }
        return chapters;
    }

    public Chapter (Context context, int index) {
        this.index = index;
        this.context = context;
    }

    public String getTitle() {
        return getStringResourceByName("chapter_" + index + "_title");
    }

    public int getIndex() {
        return index;
    }

    public int getVerseCount() {
        return VERSE_COUNT[getIndex() - 1];
    }

    /* Return the string or null if it does not exist.
     */
    private String getStringResourceByName(String aString) {
        // from https://stackoverflow.com/a/11595723
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        if (resId == 0) {
            return null;
        }
        return context.getString(resId);
    }

    public void openAsActivity(Context context) {
        Intent myIntent = new Intent(context, ChapterActivity.class);
        myIntent.putExtra(ChapterActivity.CHAPTER_INDEX, this.getIndex()); //Optional parameters
        context.startActivity(myIntent);
    }

    public List<Verse> allVerses() {
        ArrayList<Verse> verses = new ArrayList<>();
        int numberOfVerses = getVerseCount();
        Verse lastVerse = null;
        for (int index = 1; index <= numberOfVerses; index++) {
            Verse verse = new Verse(index);
            if (verse.exists()) {
                if (lastVerse != null) {
                    lastVerse.setStop(index - 1);
                }
                verses.add(verse);
                lastVerse = verse;
            }
        }
        lastVerse.setStop(numberOfVerses);
        return verses;
    }

    public class Verse {

        private final int start;
        private int stop;

        private Verse(int start) {
            this.start = start;
            this.stop = start;
        }

        boolean hasMultipleVerses() {
            return start != stop;
        }

        public String getIndexString() {
            return hasMultipleVerses() ? Integer.toString(start) + "-" + Integer.toString(stop) : Integer.toString(start);
        }

        public String getMeaning() {
            String verseId = Integer.toString(start);
            return getStringResourceByName(
                    "chapter_" + Integer.toString(Chapter.this.getIndex()) +
                            "_verse_" + verseId +
                            "_meaning");
        }

        public boolean exists() {
            String meaning = getMeaning();
            return meaning != null && !meaning.equals("");
        }

        public void setStop(int stop) {
            this.stop = stop;
        }
    }
}