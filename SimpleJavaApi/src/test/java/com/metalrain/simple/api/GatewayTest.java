package com.metalrain.simple.api;

import com.google.common.eventbus.Subscribe;
import com.jayway.awaitility.Awaitility;
import com.metalrain.simple.api.messages.BlogPostsModified;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by adam on 2014-09-29.
 */
public class GatewayTest  {

    @Test
    public void testTest() throws Exception {
        assertTrue(true);
    }
}
