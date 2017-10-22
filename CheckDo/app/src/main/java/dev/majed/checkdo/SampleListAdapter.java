package dev.majed.checkdo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


    public class SampleListAdapter  extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] name;
        private final Integer[] imageId;
        public SampleListAdapter(Activity context,
                          String[] name, Integer[] imageId) {
            super(context, R.layout.sample_list_item, name);
            this.context = context;
            this.name = name;
            this.imageId = imageId;

        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.sample_list_item, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
            txtTitle.setText(name[position]);

            imageView.setImageResource(imageId[position]);
            return rowView;
        }
        }

