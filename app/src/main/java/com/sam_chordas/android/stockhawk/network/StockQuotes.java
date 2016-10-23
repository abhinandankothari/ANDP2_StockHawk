package com.sam_chordas.android.stockhawk.network;

import com.google.gson.annotations.SerializedName;

public class StockQuotes {

    @SerializedName("Date")
    public String date;
    @SerializedName("Symbol")
    public String symbol;
    @SerializedName("Close")
    public String closeValue;
}