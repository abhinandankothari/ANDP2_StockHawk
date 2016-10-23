package com.sam_chordas.android.stockhawk.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockResults {
    @SerializedName("quote")
    public List<StockQuotes> stockQuotes;

}