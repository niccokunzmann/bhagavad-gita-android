package eu.quelltext.gita.model;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class Chapter {
    private final int index;
    private final String title;
    private final Context context;

    public static List<Chapter> all(Context context) {
        List chapters = new ArrayList();
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

}