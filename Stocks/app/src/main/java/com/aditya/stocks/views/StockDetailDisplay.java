package com.aditya.stocks.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aditya.stocks.R;
import com.aditya.stocks.models.StockDetail;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StockDetailDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail_display);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(id);

        populateData(this, id);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Fetches and populates the data based on the current ID
    private void populateData(StockDetailDisplay stockDetailDisplay, String id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://interviews.yum.dev/api/stocks/" + id;

        TextView idView = findViewById(R.id.detail_id);
        TextView nameView = findViewById(R.id.detail_name);
        TextView priceView = findViewById(R.id.detail_price);
        TextView companyTypesView = findViewById(R.id.detail_companyTypes);
        TextView allTimeHighView = findViewById(R.id.detail_allTimeHigh);
        TextView addressView = findViewById(R.id.detail_address);
        ImageView imageUrlView = findViewById(R.id.detail_imageUrl);
        TextView websiteView = findViewById(R.id.detail_website);

        Gson gson = new Gson();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    StockDetail detail = gson.fromJson(response, StockDetail.class);
                    idView.setText(detail.getId());
                    nameView.setText(detail.getName());
                    priceView.setText(detail.getPrice());
                    companyTypesView.setText(String.join("\n", detail.getCompanyType()));
                    allTimeHighView.setText(detail.getAllTimeHigh());
                    SpannableString mapLink = new SpannableString(detail.getAddress());
                    mapLink.setSpan(new UnderlineSpan(), 0, mapLink.length(), 0);
                    addressView.setText(mapLink);
                    Glide.with(stockDetailDisplay).load(detail.getImageUrl()).into(imageUrlView);
                    SpannableString siteLink = new SpannableString(detail.getWebsite());
                    siteLink.setSpan(new UnderlineSpan(), 0, siteLink.length(), 0);
                    websiteView.setText(siteLink);
                },
                error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }

    // website link click handler
    public void goToWebsite(View view) {
        TextView v = (TextView) view;
        String url = v.getText().toString();
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    // maps link click handler
    public void openInMaps(View view) {
        TextView v = (TextView) view;
        String address = v.getText().toString();
        address = address.replace(" ", "+");
        String url = "https://www.google.com/maps/search/?api=1&query=" + address;
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
