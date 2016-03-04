package com.mysassa;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.mysassa.api.model.Category;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 1/3/2015.
 */
public class ApplicationSectionsManager {
    public ApplicationSectionsManager(Context ctx) {
        XmlResourceParser xml = ctx.getResources().getXml(R.xml.blog_category_defs);

        try {
            int eventType = xml.getEventType();
            String lastItem = null;
            CategoryDef currentItem = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (xml.getName().equals("item")) {
                            if (currentItem != null) items.add(currentItem);
                            currentItem = new CategoryDef();
                        } else {
                            lastItem = xml.getName();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (currentItem != null) {
                            if (lastItem.equals("title")) {
                                currentItem.title = xml.getText();
                            } else if (lastItem.equals("category")) {
                                currentItem.category = xml.getText();
                            } else if (lastItem.equals("sortColumn")) {
                                currentItem.sortColumn = xml.getText();
                            } else if (lastItem.equals("sortOrder")) {
                                currentItem.sortOrder = xml.getText();
                            } else if (lastItem.equals("pagesize")) {
                                currentItem.pagesize = Integer.parseInt(xml.getText());
                            } else if (lastItem.equals("commentsAllowed")) {
                                currentItem.commentsAllowed = Boolean.valueOf(xml.getText());
                            } else if (lastItem.equals("postsAllowed")) {
                                currentItem.postsAllowed = Boolean.valueOf(xml.getText());
                            } else if (lastItem.equals("fragment")) {
                                currentItem.fragment = xml.getText();
                            }
                        }
                        break;
                }
                eventType = xml.next();
            }
            if (currentItem != null && currentItem.title!=null) items.add(currentItem);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDef(CategoryDef def) {

    }

    public static class CategoryDef {
        public String title;
        public String category;
        public String sortColumn;
        public String sortOrder;
        public String fragment = null;
        public int pagesize;
        public boolean commentsAllowed;
        public boolean postsAllowed;

        public CategoryDef(Category selectedCategory) {
            title = selectedCategory.name;
            category = selectedCategory.name;
            sortOrder = "DESC";
            sortColumn = "dateCreated";
            pagesize = 20;
            commentsAllowed = true;
            postsAllowed = true;
            fragment = null;
        }

        public CategoryDef() {}

        @Override
        public String toString() {
            return "CategoryDef{" +
                    "title='" + title + '\'' +
                    ", category='" + category + '\'' +
                    ", sortColumn='" + sortColumn + '\'' +
                    ", sortOrder='" + sortOrder + '\'' +
                    ", pagesize=" + pagesize +
                    '}';
        }
        public Category toCategory() {
            return new Category(title);
        }
    }

    private final List<CategoryDef> items = new ArrayList<CategoryDef>();

    public CategoryDef getCategoryDef(Category selectedCategory) {
        for (CategoryDef cd:items) {
            if (cd.toCategory().equals(selectedCategory)) {
                return cd;
            }
        }
        return new CategoryDef(selectedCategory);
    }



    public List<CategoryDef> getItems() {
        return items;
    }
}
