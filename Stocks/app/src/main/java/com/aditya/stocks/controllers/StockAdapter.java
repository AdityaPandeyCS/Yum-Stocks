package com.aditya.stocks.controllers;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aditya.stocks.R;
import com.aditya.stocks.models.StockTicker;
import com.aditya.stocks.views.StockDetailDisplay;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Connects the data to the RecyclerView.
 */
public class StockAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private List<StockTicker> stockList = new ArrayList<>();
    private List<StockTicker> filteredStockList = new ArrayList<>();
    private String prefixFilter;
    private List<String> typeFilter;

    public StockAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        prefixFilter = "";
        typeFilter = new ArrayList<>();
    }

    // Create new views
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.stock_ticker, parent, false);
        return new StockTickerHolder(view);
    }

    // Replace the contents of a view
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int GREEN = 0xFF00FF00;
        final int RED = 0xFFFF0000;
        final int BLUE = 0xFF0000FF;
        StockTicker ticker = filteredStockList.get(position);
        StockTickerHolder tickerHolder = (StockTickerHolder) holder;
        tickerHolder.card.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), StockDetailDisplay.class);
            intent.putExtra("id", ticker.getId());
            v.getContext().startActivity(intent);
        });
        tickerHolder.id.setText(ticker.getId());
        tickerHolder.name.setText(ticker.getName());
        tickerHolder.price.setText(ticker.getPrice());
        double change = ticker.getChange();
        if (change > 0) {
            tickerHolder.change.setText(String.format("(+%.2f)", change));
            tickerHolder.change.setTextColor(GREEN);
        } else if (change < 0) {
            tickerHolder.change.setText(String.format("(%.2f)", change));
            tickerHolder.change.setTextColor(RED);
        } else {
            tickerHolder.change.setText("(+0.00)");
            tickerHolder.change.setTextColor(BLUE);
        }
        tickerHolder.companyTypes.setText(Arrays.toString(ticker.getCompanyType()));
    }

    @Override
    public int getItemCount() {
        return filteredStockList.size();
    }

    public void setPrefixFilter(String filter) {
        prefixFilter = filter.trim().toLowerCase();
    }

    public void setTypeFilter(String filter) {
        typeFilter = new ArrayList<>(Arrays.asList(filter.split(",")));
        for (int i = 0; i < typeFilter.size(); i++) {
            typeFilter.set(i, typeFilter.get(i).trim().toLowerCase());
        }
        typeFilter.removeIf(String::isEmpty);
    }

    // Applies the prefix and type filters
    public void filter() {
        filteredStockList = new ArrayList<>(stockList);

        filteredStockList.removeIf(ticker -> !ticker.
                getName().
                toLowerCase().
                startsWith(prefixFilter));

        if (typeFilter.size() > 0) {
            filteredStockList.removeIf(ticker -> {
                for (String type : ticker.getCompanyType()) {
                    for (String filterType : typeFilter) {
                        if (type.toLowerCase().equals(filterType)) {
                            return false;
                        }
                    }
                }
                return true;
            });
        }

        try {
            notifyDataSetChanged();
        } catch (NullPointerException e) {
            // thrown when running unit tests
        }
    }

    // Updates the list based on the data in jsonList
    // Done generically so that new companies can be
    // added in the back end without needing to update
    // the front end
    public void updateList(String jsonList) {
        Gson gson = new Gson();
        StockTicker[] tickers = gson.fromJson(jsonList, StockTicker[].class);
        for (StockTicker ticker : tickers) {
            int idx = stockList.indexOf(ticker);
            if (idx != -1) {
                // update the existing stock ticker
                StockTicker current = stockList.get(idx);
                current.update(ticker);
            } else {
                // add the new ticker
                stockList.add(ticker);
            }
        }
        filter();
    }

    public List<StockTicker> getItems() {
        return filteredStockList;
    }

    // Provide a reference to the views for each data item
    public static class StockTickerHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView id;
        TextView name;
        TextView price;
        TextView change;
        TextView companyTypes;

        public StockTickerHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.detail_container);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            change = itemView.findViewById(R.id.change);
            companyTypes = itemView.findViewById(R.id.companyTypes);
        }
    }

}
