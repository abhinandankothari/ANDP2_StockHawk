package com.sam_chordas.android.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.network.GraphQueryNetworkService;
import com.sam_chordas.android.stockhawk.network.StockResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LineGraphActivity extends AppCompatActivity {
    String stockSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        stockSymbol = getIntent().getStringExtra("symbol");
        getSupportActionBar().setTitle("From: " + FormattedDate(monthAgo()) + " to " + FormattedDate(new Date(System.currentTimeMillis())));
        LineChartView lineChartView = (LineChartView) findViewById(R.id.linechart);
        LineSet dataset = new LineSet();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GraphQueryNetworkService service = retrofit.create(GraphQueryNetworkService.class);
        Call<StockResponse> stockResponseCall =
                service.stockQuoteResults("v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20=%20\"" + stockSymbol + "\"%20and%20startDate%20=%20\"" + FormattedDate(monthAgo()) + "\"%20and%20endDate%20=%20\"" + FormattedDate(new Date(System.currentTimeMillis())) + "\"&format=json&env=http://datatables.org/alltables.env");
        try {
            StockResponse response = stockResponseCall.execute().body();
            for (int i = 0; i < response.stockQuery.stockResults.stockQuotes.size(); i++) {
                dataset.addPoint(new Point(response.stockQuery.stockResults.stockQuotes.get(i).date.substring(8, 10), Float.parseFloat(response.stockQuery.stockResults.stockQuotes.get(i).closeValue)));
            }
        } catch (IOException ex) {
            Log.e("ERROR", "Unable to get Data");
        }
        lineChartView.setStep(50);
        lineChartView.setLabelsColor(Color.MAGENTA);
        dataset.setFill(Color.BLUE);
        lineChartView.addData(dataset);
        lineChartView.show();

    }

    private Date monthAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    String FormattedDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

}
