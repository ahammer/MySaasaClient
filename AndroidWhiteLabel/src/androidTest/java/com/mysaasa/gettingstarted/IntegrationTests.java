package com.mysaasa.gettingstarted;

import android.support.test.espresso.Espresso;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Toast;

import com.jayway.awaitility.Awaitility;
import com.mysaasa.Envelope;
import com.mysaasa.MySaasaApplication;
import com.mysaasa.ui.ActivityMain;
import com.mysaasa.ui.views.ContactView;
import com.mysaasa.api.MySaasaClient;
import com.mysaasa.api.model.BlogPost;
import com.mysaasa.whitelabel.R;

import org.greenrobot.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;

/**
 * Created by Adam on 3/7/2016.
 */


/**
 * These tests verify the Getting Started app
 *
 * They are meant to be running on the default http://gettingstarted.test:8080
 * The tests will verify that the app is working in conjuction with the server
 *
 * They should run successfully given that you don't make big changes to the getting started website
 * and it's data.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntegrationTests {
    private ActivityMain mActivity;
    private MySaasaApplication application;
    private MySaasaClient client;
    private TestState testState;

    public class TestState {
        boolean createdUser = false;
        final String TEST_UNIQUE = "_"+(int) ((new Date()).getTime()/1000);
        final String TEST_USERNAME = "TESTUSER"+TEST_UNIQUE;
        final String TEST_PASSWORD = "testuser"+TEST_UNIQUE;
        final String TEST_POST_TITLE = "TestPost"+TEST_UNIQUE;
        final String TEST_POST_SUBTITLE = "Sub Title"+TEST_UNIQUE;
        final String TEST_POST_SUMMARY = "Summary"+TEST_UNIQUE;
        final String TEST_POST_BODY = "Body"+TEST_UNIQUE;
        final String TEST_COMMENT_BODY = "Comment"+TEST_UNIQUE;
        final String TEST_REPLY_BODY = "Reply"+TEST_UNIQUE;
    }

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(ActivityMain.class);

    public IntegrationTests() {}

    @Before
    public void setUp() throws Exception {
        mActivity = (ActivityMain) mActivityRule.getActivity();
        application = (MySaasaApplication) mActivity.getApplication();
        client = application.getMySaasaClient();
        testState = new TestState();

    }

    @After
    public void tearDown() throws Exception {
        ContactView.GLOBAL_CONTACT_USER_OVERRIDE = null;
    }

    /**
     * This smoke tests creating a blog post and a comment
     * @throws Exception
     */
    @Test
    public void testSmokeBlogAndComment() throws Exception {
        openSideNav();
        onView(withText("News")).perform(click());
        onView(withId(R.id.action_post)).perform(click());
        authenticateIfNecessary();
        writePostAndSubmit();
        clickOnNewBlogPost();
        onView(withId(R.id.comments_fab)).perform(click());
        onView(withId(R.id.action_comment)).perform(click());
        onView(withId(R.id.comment)).perform(typeText(testState.TEST_COMMENT_BODY));
        onView(withId(R.id.post)).perform(click());
        onData(MySaasaMatchers.withComment(client.getCommentManager(), testState.TEST_COMMENT_BODY))
                .inAdapterView(withId(R.id.blog_comments))
                .onChildView(withId(R.id.reply)).perform(click());

        onView(withId(R.id.comment)).perform(typeText(testState.TEST_REPLY_BODY));
        onView(withId(R.id.post)).perform(click());
        Thread.sleep(1000); //Next step only shows up after a push and update, so small delay for the push
        onData(MySaasaMatchers.withComment(client.getCommentManager(), testState.TEST_REPLY_BODY))
                .inAdapterView(withId(R.id.blog_comments))
                .check(matches(isDisplayed()));
    }

    /**
     * This test creates an account, logs out, logs in again
     * @throws Exception
     */
    @Test
    public void testAccountBox() throws Exception {

        openSideNav();
        if (client.getAuthenticationManager().getAuthenticatedUser() != null) {
            onView(withId(R.id.logout)).perform(click());
        }
        onView(withId(R.id.signin)).perform(click());
        authenticateIfNecessary();
        onView(withId(R.id.logout)).check(matches(isDisplayed()));
        onView(withId(R.id.logout)).perform(click());
        onView(withId(R.id.signin)).check(matches(isDisplayed()));
        onView(withId(R.id.signin)).perform(click());
        authenticateIfNecessary();
        onView(withId(R.id.logout)).check(matches(isDisplayed()));
   }

    /**
     * We log in, contact form, and then verify the push message is received.
     * @throws Exception
     */
    @Test
    public void contact() throws Exception {
        performContact(true);
    }

    private void performContact(boolean override) throws Exception {
        announce("Testing contact form");
        if (override) {
            announce("Over-riding target user");
            ContactView.GLOBAL_CONTACT_USER_OVERRIDE = testState.TEST_USERNAME;
        }

        openSideNav();
        if (client.getAuthenticationManager().getAuthenticatedUser() != null) {
            announce("Logging out");
            onView(withId(R.id.logout)).perform(click());
        }

        onView(withId(R.id.signin)).perform(click());
        authenticateIfNecessary();

        announce("Trying to contact");
        onView(withText("Contact")).perform(click());
        onView(withId(R.id.name)).perform(typeText(testState.TEST_USERNAME));
        onView(withId(R.id.email)).perform(typeText("adamhammer2@gmail.com"));
        onView(withId(R.id.phone)).perform(typeText("5551234455"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.body)).perform(typeText(testState.TEST_POST_BODY));
        PushNotifiedNewMessageWaiter waiter = new PushNotifiedNewMessageWaiter();
        MySaasaApplication.getService().bus.register(waiter);
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.send)).perform(click());
        if (override) {
            //We wait for message to come back in to ourself
            //This is GCM 3rd party push, so if that fails or queus this test may fail
            Awaitility.await().atMost(10, TimeUnit.SECONDS).until(waiter::getResult);
            assertTrue(waiter.getResult());
        }
    }

    private void announce(String s) {
        mActivity.runOnUiThread(new Runnable() {
            public Toast toast;
            @Override
            public void run() {
                if (toast != null) {
                    toast.setText(s);
                } else {
                    toast = Toast.makeText(mActivity, s, Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        });
    }

    public static class PushNotifiedNewMessageWaiter {
        private boolean result;

        @Subscribe
        public void event(Envelope event) {
            result = true;
        }
        public boolean getResult() {
            return result;
        }
    }

    private void clickOnNewBlogPost() {
        for (BlogPost bp:mActivity.getPosts()) {
            if (bp.title.equals(testState.TEST_POST_TITLE)) {
                onData(allOf(is(instanceOf(BlogPost.class)), is(bp))).inAdapterView(withId(R.id.content_frame)).perform(click());
            }
        }
    }

    private void writePostAndSubmit() throws InterruptedException {
        onView(withId(R.id.title)).perform(typeText(testState.TEST_POST_TITLE));
        onView(withId(R.id.subtitle)).perform(typeText(testState.TEST_POST_SUBTITLE));
        onView(withId(R.id.summary)).perform(typeText(testState.TEST_POST_SUMMARY));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.body)).perform(typeText(testState.TEST_POST_BODY));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.post)).perform(click());
    }

    private void openSideNav() {
        announce("Opening side nav");
        onView(withContentDescription("Navigate up")).perform(click());
    }

    private void authenticateIfNecessary() throws Exception {
        if (client.getAuthenticationManager().getAuthenticatedUser() == null) {
            if (!testState.createdUser) {
                announce("Creating User");
                onView(withId(R.id.username)).perform(click()).perform(typeText(testState.TEST_USERNAME));
                onView(withId(R.id.password)).perform(click()).perform(typeText(testState.TEST_PASSWORD));
                onView(withId(R.id.password_repeat)).perform(click()).perform(typeText(testState.TEST_PASSWORD));
                onView(withId(R.id.button_create_account)).perform(click());
                assertTrue(client.getAuthenticationManager().getAuthenticatedUser() != null);
                testState.createdUser = true;
            } else {
                announce("Signing in");
                onView(withId(R.id.username)).perform(click()).perform(typeText(testState.TEST_USERNAME));
                onView(withId(R.id.password)).perform(click()).perform(typeText(testState.TEST_PASSWORD));
                onView(withId(R.id.button_login)).perform(click());
                assertTrue(client.getAuthenticationManager().getAuthenticatedUser() != null);
            }
        }
    }

}
