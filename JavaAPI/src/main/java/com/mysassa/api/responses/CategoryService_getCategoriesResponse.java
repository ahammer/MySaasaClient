package com.mysassa.api.responses;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysassa.api.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 2014-10-16.
 */
public class CategoryService_getCategoriesResponse extends SimpleResponse {
    public final List<Category> results = new ArrayList();

    public CategoryService_getCategoriesResponse(JsonObject rootObject) {
        super(rootObject);
        try {
            JsonArray array = rootObject.getAsJsonArray("data");
            for (int i = 0; i < array.size(); i++)
                results.add(new Category(array.get(i).getAsJsonObject()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
