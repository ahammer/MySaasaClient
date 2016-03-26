package com.mysaasa.gettingstarted;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.mysaasa.MySaasaApplication;
import com.mysaasa.ui.ActivityMain;
import com.mysassa.api.MySaasaClient;
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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
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

    static final String TEST_USERNAME = "TESTUSER"+(new Date()).getTime();
    static final String TEST_PASSWORD = "testuser";

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(ActivityMain.class);

    public IntegrationTests() {}

    @Before
    public void setUp() throws Exception {
        mActivity = (ActivityMain) mActivityRule.getActivity();
        application = (MySaasaApplication) mActivity.getApplication();
        client = application.getMySaasaClient();
    }

    @Test
    public void testNewsPost() throws Exception {
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withText("News")).perform(click());
        onView(withId(R.id.action_post)).perform(click());
        authenticateIfNecessary();
        Thread.sleep(50000);
    }



    @Test
    public void testBlogPost() throws Exception {
        clickOnTheFirstArticle();
        clickOnTheCommentButton();
        authenticateIfNecessary();
        onView(withId(R.id.comment)).perform(click()).perform(typeText("This is a test comment"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.post)).perform(click());
    }

    @Test
    public void jumpToComments() throws Exception {
        onData(anything()).inAdapterView(withId(R.id.content_frame)).atPosition(2).perform(click());

        Thread.sleep(50000);
    }

    private void authenticateIfNecessary() throws Exception {
        if (client.getAuthenticationManager().getAuthenticatedUser() == null) {
            onView(withId(R.id.username)).perform(click()).perform(typeText(TEST_USERNAME));
            onView(withId(R.id.password)).perform(click()).perform(typeText(TEST_PASSWORD));
            onView(withId(R.id.password_repeat)).perform(click()).perform(typeText(TEST_PASSWORD));
            onView(withId(R.id.button_create_account)).perform(click());
            assertTrue(client.getAuthenticationManager().getAuthenticatedUser() != null);
        }
    }

    private void clickOnTheCommentButton() {
        onView(withId(R.id.action_comment)).perform(click());
    }

    private void clickOnTheFirstArticle() {
        onData(anything()).inAdapterView(withId(R.id.content_frame)).atPosition(0).perform(click());
    }


}
