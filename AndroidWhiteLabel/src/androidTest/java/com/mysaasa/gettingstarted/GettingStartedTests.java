package com.mysaasa.gettingstarted;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.test.ActivityInstrumentationTestCase2;

import com.mysaasa.MySaasaAndroidApplication;
import com.mysaasa.ui.ActivityMain;
import com.mysassa.api.MySaasaClient;
import com.mysassa.whitelabel.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

/**
 * Created by Adam on 3/7/2016.
 */


/**
 * These tests verify the Getting Started app, assuming that you can login with admin/test123
 *
 * I run a local server and point to it on DNS for the purpose of the tests.
 */
public class GettingStartedTests extends ActivityInstrumentationTestCase2<ActivityMain> {
    private ActivityMain mActivity;
    private MySaasaAndroidApplication application;
    private MySaasaClient client;

    public GettingStartedTests() {
        super(ActivityMain.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        application = (MySaasaAndroidApplication) mActivity.getApplication();
        client = application.getMySaasaClient();
    }

    @Test
    public void testPostToBlogComments() throws Exception {
        onData(anything()).inAdapterView(withId(R.id.content_frame)).atPosition(0).perform(click());
        onView(withId(R.id.action_comment)).perform(click());
        if (client.getLoginManager().getAuthenticatedUser() == null) {
            onView(withId(R.id.username)).perform(click()).perform(typeText("admin"));
            onView(withId(R.id.password)).perform(click()).perform(typeText("test123"));
            onView(withId(R.id.button_login)).perform(click());
            assertTrue(client.getLoginManager().getAuthenticatedUser() != null);
        }
        onView(withId(R.id.comment)).perform(click()).perform(typeText("This is a test comment"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.post)).perform(click());

    }


}
