package com.aditya.stocks.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.aditya.stocks.R;
import com.aditya.stocks.controllers.StockAdapter;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class StockListDisplay extends AppCompatActivity {
    private StockAdapter stockAdapter;
    private WebSocket webSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initiateSocketConnection();
    }

    @Override
    protected void onDestroy() {
        webSocket.close(1000, "Not in use");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            toggleSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // show or hide the filter inputs
    private void toggleSearch() {
        EditText filterPrefix = findViewById(R.id.filterPrefix);
        EditText filterType = findViewById(R.id.filterType);
        if (filterPrefix.getVisibility() == View.VISIBLE) {
            filterPrefix.setVisibility(View.GONE);
            filterType.setVisibility(View.GONE);
        } else {
            filterPrefix.setVisibility(View.VISIBLE);
            filterType.setVisibility(View.VISIBLE);
        }
    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        final String SERVER_PATH = "wss://interviews.yum.dev/ws/stocks";
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
    }

    private void initializeView() {
        RecyclerView recyclerView = findViewById(R.id.stockList);
        stockAdapter = new StockAdapter(getLayoutInflater());
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        EditText prefixFilter = findViewById(R.id.filterPrefix);
        prefixFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stockAdapter.setPrefixFilter(s.toString());
                stockAdapter.filter();
            }
        });

        EditText typeFilter = findViewById(R.id.filterType);
        typeFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stockAdapter.setTypeFilter(s.toString());
                stockAdapter.filter();
            }
        });
    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(StockListDisplay.this::initializeView);
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull final String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> stockAdapter.updateList(text));
        }
    }
}
