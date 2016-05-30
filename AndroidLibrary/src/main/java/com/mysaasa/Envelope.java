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
 */
public class Envelope<T> {
    final T contents;
    boolean opened;

    public Envelope(T contents) {
        this.contents = contents;
    }

    public T open() {
        opened = true;
        return contents;
    }



    public boolean isOpened() {
        return opened;
    }

    @Override
    public String toString() {
        return "Envelope{" +
                "contents=" + contents +
                ", opened=" + opened +
                '}';
    }
}
