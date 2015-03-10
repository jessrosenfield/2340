package com.gatech.objectsanddesign.shoppingwithfriends;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RequestArrayAdaptor extends ArrayAdapter<Request> {

    public RequestArrayAdaptor(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text = (TextView) view.findViewById(R.id.list_item_request);
        Request r = getItem(position);
        Log.d("PROCESSING", r.toString());
        if (r.isMatched()) {
            Log.d("FOUND MATCH", r.toString());
            text.setBackgroundResource(R.color.request_matched);
        }
        return view;
    }
}
