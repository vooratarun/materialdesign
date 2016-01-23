package info.androidhive.materialdesign.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.R;

/**
 * Created by YASHWANTH on 25/Oct/2015.
 */
public class ExampleAdapter extends CursorAdapter {

    private List<String> items = new ArrayList<String>();

    private TextView text;
    private Cursor cursor;
    private Context context;
    View view;

    public ExampleAdapter(Context context, Cursor cursor, List<String> items) {

        super(context, cursor, false);
        this.cursor = cursor;
        this.context = context;
        this.items = items;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        text.setText(items.get(cursor.getPosition()));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.item, parent, false);

        text = (TextView) view.findViewById(R.id.item);

        return view;

    }
}
