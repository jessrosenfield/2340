package com.gatech.objectsanddesign.shoppingwithfriends;

import android.content.Context;
import android.graphics.Color;
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
        if (r.isMatched()) {
            text.setBackgroundResource(R.color.request_matched);
        } else {
            text.setBackgroundResource(Color.TRANSPARENT);
        }
        return view;
    }
}
