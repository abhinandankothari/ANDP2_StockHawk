package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

public class StockWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final Intent intent;
    private Cursor cursor;

    public StockWidgetFactory(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null)
            cursor.close();
        cursor = context.getContentResolver().query(
                QuoteProvider.Quotes.CONTENT_URI,
                new String[]{
                        QuoteColumns._ID,
                        QuoteColumns.SYMBOL,
                        QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE,
                        QuoteColumns.CHANGE,
                        QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
    }

    @Override
    public void onDestroy() {
        if (cursor != null)
            cursor.close();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item_quote);
        if (cursor.moveToPosition(position)) {
            String symbol = cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL));
            remoteViews.setTextViewText(R.id.stock_symbol, symbol);
            remoteViews.setTextViewText(R.id.bid_price, cursor.getString(cursor.getColumnIndex(QuoteColumns.BIDPRICE)));
            remoteViews.setTextViewText(R.id.change, cursor.getString(cursor.getColumnIndex(QuoteColumns.CHANGE)));
            if (cursor.getColumnIndex(QuoteColumns.ISUP) == 1)
                remoteViews.setInt(R.id.stock, "setBackgroundResource", R.drawable.percent_change_pill_green);
            else
                remoteViews.setInt(R.id.stock, "setBackgroundResource", R.drawable.percent_change_pill_red);
            Intent intent = new Intent();
            intent.putExtra("symbol", symbol);
            remoteViews.setOnClickFillInIntent(R.id.stock, intent);
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}