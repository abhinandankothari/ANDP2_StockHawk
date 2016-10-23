package com.sam_chordas.android.stockhawk.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GraphQueryNetworkService {
    @GET
    Call<StockResponse> stockQuoteResults(
            @Url String url);
}
