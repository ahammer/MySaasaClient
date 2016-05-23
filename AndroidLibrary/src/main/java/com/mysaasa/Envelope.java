package com.mysaasa;

import com.google.gson.Gson;

/**
 * An envelope for a push message
 *
 * It can tell when it's been opened, so we can decide how we want to handle it.
 *
 * Typical Usage Pattern
 *
 * 1) Push In
 * 2) Wrapped in Envelope
 * 3) Sent on bus
 * 4) Check if read?
 * 5) If not, fallback behavior
 * @param <T> The type of the contents
 * @param <V> the type of the Json data
 */
public class Envelope<T, V> {
    static final private Gson gson = new Gson();
    final T contents;
    private final String json;
    private final Class jsonClass;
    boolean opened;

    public Envelope(T contents, String json, Class jsonClass) {
        this.contents = contents;
        this.json = json;
        this.jsonClass = jsonClass;
    }

    public T open() {
        opened = true;
        return contents;
    }


    public V getObject() {
        return (V) gson.fromJson(json, jsonClass);
    }

    public boolean isOpened() {
        return opened;
    }

    @Override
    public String toString() {
        return "Envelope{" +
                "contents=" + contents +
                ", json='" + json + '\'' +
                ", opened=" + opened +
                '}';
    }
}
