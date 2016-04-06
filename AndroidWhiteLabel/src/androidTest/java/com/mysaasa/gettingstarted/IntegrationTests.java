package com.mysaasa.gettingstarted;

import android.support.test.espresso.Espresso;

import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.common.eventbus.Subscribe;
import com.mysaasa.MySaasaApplication;
import com.mysaasa.PushNotifiedNewMessageEvent;
import com.mysaasa.ui.ActivityMain;
import com.mysaasa.ui.views.ContactView;
import com.mysassa.api.MySaasaClient;
import com.mysassa.api.model.BlogPost;
import com.mysassa.whitelabel.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
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
        ContactView.GLOBAL_CONTACT_USER_OVERRIDE = testState.TEST_USERNAME;
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
        onData(MySaasaMatchers.withComment(client.getCommentManager(), testState.TEST_COMMENT_BODY)).inAdapterView(withId(R.id.blog_comments)).onChildView(withId(R.id.reply)).perform(click());
        onView(withId(R.id.comment)).perform(typeText(testState.TEST_REPLY_BODY));
        onView(withId(R.id.post)).perform(click());
        onData(MySaasaMatchers.withComment(client.getCommentManager(), testState.TEST_REPLY_BODY)).inAdapterView(withId(R.id.blog_comments)).check(matches(isDisplayed()));
    }

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
     * Verifies contact form and push notifications!!
     * @throws Exception
     */
    @Test
    public void contact() throws Exception {
        openSideNav();
        if (client.getAuthenticationManager().getAuthenticatedUser() != null) {
            onView(withId(R.id.logout)).perform(click());
        }
        onView(withId(R.id.signin)).perform(click());
        authenticateIfNecessary();

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
        openSideNav();
        onView(withText("Messages")).perform(click());
        onView(withText("App Feedback")).perform(click());
        Thread.sleep(10000);

        assertTrue(waiter.getResult());

    }

    public static class PushNotifiedNewMessageWaiter {
        private boolean result;

        @Subscribe
        public void event(PushNotifiedNewMessageEvent event) {
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
        onView(withContentDescription("Navigate up")).perform(click());
    }




    private void authenticateIfNecessary() throws Exception {
        if (client.getAuthenticationManager().getAuthenticatedUser() == null) {
            if (!testState.createdUser) {
                onView(withId(R.id.username)).perform(click()).perform(typeText(testState.TEST_USERNAME));
                onView(withId(R.id.password)).perform(click()).perform(typeText(testState.TEST_PASSWORD));
                onView(withId(R.id.password_repeat)).perform(click()).perform(typeText(testState.TEST_PASSWORD));
                onView(withId(R.id.button_create_account)).perform(click());
                assertTrue(client.getAuthenticationManager().getAuthenticatedUser() != null);
                testState.createdUser = true;
            } else {
                onView(withId(R.id.username)).perform(click()).perform(typeText(testState.TEST_USERNAME));
                onView(withId(R.id.password)).perform(click()).perform(typeText(testState.TEST_PASSWORD));
                onView(withId(R.id.button_login)).perform(click());
                assertTrue(client.getAuthenticationManager().getAuthenticatedUser() != null);
            }
        }
    }

    private void clickOnTheCommentButton() {
        onView(withId(R.id.action_comment)).perform(click());
    }

    private void clickOnTheFirstArticle() {
        onData(anything()).inAdapterView(withId(R.id.content_frame)).atPosition(0).perform(click());
    }


}
