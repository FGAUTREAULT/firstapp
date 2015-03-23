package com.perso.fab.myfirstapp.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.perso.fab.myfirstapp.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pcdev on 16/03/2015.
 */
public class MySimpleArrayAdapter extends ArrayAdapter<File> {
    private final Context context;
    private final ArrayList<File> values;

    public MySimpleArrayAdapter(Context context, ArrayList<File> values) {
        super(context, R.layout.row_layout, values);
        this.context = context;
        this.values = values;
    }

    static class ViewHolder {
        public TextView textView;
        public TextView pathView;
        public ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //When user scroll the getview can reuse item already inflated
        //@time consumption reduced
        //More => ViewHolder pattern
        View rowView = convertView;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.row_layout, parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) rowView.findViewById(R.id.firstLine);
            viewHolder.pathView = (TextView) rowView.findViewById(R.id.secondLine);
            viewHolder.imageView = (ImageView) rowView.findViewById(R.id.icon);
            rowView.setTag(viewHolder);
        }

        //Fill data
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        viewHolder.textView.setText(values.get(position).getName());
        viewHolder.pathView.setText(values.get(position).getAbsolutePath());
        viewHolder.imageView.setImageResource(R.drawable.ic_action_search);

        return rowView;
    }

}
