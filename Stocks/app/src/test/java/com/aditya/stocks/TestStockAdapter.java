package com.aditya.stocks;

import com.aditya.stocks.controllers.StockAdapter;
import com.aditya.stocks.models.StockTicker;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestStockAdapter {

    /**
     * Test that a JSON string is correctly
     * converted into a list of StockTickers.
     */
    @Test
    public void testUpdateList() {
        StockAdapter adapter = new StockAdapter(null);
        String text = "[" +
                    "{" +
                        "\"id\":\"YUM\"," +
                        "\"name\":\"Yum! Brands, Inc.\"," +
                        "\"price\":74.15932610445329," +
                        "\"companyType\":[\"Food\",\"Tech\"]" +
                    "}" +
                "]";
        adapter.updateList(text);

        assertEquals(1, adapter.getItemCount());
        List<StockTicker> tickers = adapter.getItems();
        StockTicker ticker = tickers.get(0);
        assertEquals("YUM", ticker.getId());
        assertEquals("Yum! Brands, Inc.", ticker.getName());
        assertEquals("$74.16", ticker.getPrice());
        assertArrayEquals(new String[]{"Food", "Tech"}, ticker.getCompanyType());
    }

    /**
     * Test that the list of tickers
     * gets filtered correctly.
     */
    @Test
    public void testFilter() {
        StockAdapter adapter = new StockAdapter(null);
        String text = "[" +
                    "{" +
                        "\"id\":\"YUM\"," +
                        "\"name\":\"Yum! Brands, Inc.\"," +
                        "\"price\":74.15932610445329," +
                        "\"companyType\":[\"Food\",\"Tech\"]" +
                    "}," +
                    "{" +
                        "\"id\":\"AAPL\"," +
                        "\"name\":\"Apple Inc.\"," +
                        "\"price\":301.95508880648987," +
                        "\"companyType\":[\"Tech\"]" +
                    "}" +
                "]";

        // filter by the prefix "a"
        adapter.updateList(text);
        adapter.setPrefixFilter("a");
        adapter.filter();

        assertEquals(1, adapter.getItemCount());
        List<StockTicker> tickers = adapter.getItems();
        StockTicker ticker = tickers.get(0);
        assertEquals("AAPL", ticker.getId());

        // filter by the type "food"
        adapter.setPrefixFilter("");
        adapter.setTypeFilter("food");
        adapter.filter();

        assertEquals(1, adapter.getItemCount());
        tickers = adapter.getItems();
        ticker = tickers.get(0);
        assertEquals("YUM", ticker.getId());
    }
}