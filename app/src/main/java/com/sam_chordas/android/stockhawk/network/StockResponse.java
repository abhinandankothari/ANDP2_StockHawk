package com.sam_chordas.android.stockhawk.network;

import com.google.gson.annotations.SerializedName;

public class StockResponse {
    @SerializedName("query")
    public StockQuery stockQuery;
}