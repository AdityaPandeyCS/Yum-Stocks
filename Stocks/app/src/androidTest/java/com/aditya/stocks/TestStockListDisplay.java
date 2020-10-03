package com.aditya.stocks;

import com.aditya.stocks.views.StockListDisplay;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TestStockListDisplay {
    @Rule
    public ActivityScenarioRule<StockListDisplay> activityRule =
            new ActivityScenarioRule<>(StockListDisplay.class);

    /**
     * Wait 5 seconds for the data to be fetched.
     *
     * @throws InterruptedException
     */
    @Before
    public void setup() throws InterruptedException {
        Thread.sleep(5 * 1000);
    }

    /**
     * Test that the data is fetched and
     * displayed correctly via websocket.
     */
    @Test
    public void testDataIsLoaded() {
        onView(withText("AAPL")).check(matches(isDisplayed()));
        onView(withText("YUM")).check(matches(isDisplayed()));
        onView(withText("F")).check(matches(isDisplayed()));

        // scroll down to reveal the rest of the list
        onView(withId(R.id.stockList)).perform(swipeUp());

        onView(withText("TSLA")).check(matches(isDisplayed()));
        onView(withText("AMZN")).check(matches(isDisplayed()));
        onView(withText("AMGN")).check(matches(isDisplayed()));
    }

    /**
     * Test that the data is correctly
     * filtered when inputting a prefix filter.
     */
    @Test
    public void testPrefixFilter() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.filterPrefix)).perform(typeText("Y"), closeSoftKeyboard());

        onView(withText("YUM")).check(matches(isDisplayed()));

        onView(withText("AAPL")).check(doesNotExist());
        onView(withText("F")).check(doesNotExist());
        onView(withText("TSLA")).check(doesNotExist());
        onView(withText("AMZN")).check(doesNotExist());
        onView(withText("AMGN")).check(doesNotExist());
    }

    /**
     * Test that the data is correctly
     * filtered when inputting a type filter.
     */
    @Test
    public void testTypeFilter() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.filterType)).perform(typeText("tech"), closeSoftKeyboard());

        onView(withText("AAPL")).check(matches(isDisplayed()));
        onView(withText("YUM")).check(matches(isDisplayed()));
        onView(withText("TSLA")).check(matches(isDisplayed()));
        onView(withText("AMZN")).check(matches(isDisplayed()));

        onView(withText("F")).check(doesNotExist());
        onView(withText("AMGN")).check(doesNotExist());
    }

    /**
     * Test that clicking on a stock
     * ticker leads to the correct page.
     */
    @Test
    public void testOnClick() throws InterruptedException {
        onView(withText("YUM")).perform(click());
        Thread.sleep(3000);
        onView(withText("Yum! Brands, Inc.")).check(matches(isDisplayed()));
        onView(withText("Food\nTech")).check(matches(isDisplayed()));
        onView(withText("1441 Gardiner Lane Louisville, KY 40213 United States")).check(matches(isDisplayed()));

    }
}
