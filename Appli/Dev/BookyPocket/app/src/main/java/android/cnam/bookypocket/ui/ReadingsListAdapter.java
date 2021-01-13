package android.cnam.bookypocket.ui;

import android.cnam.bookypocket.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ReadingsListAdapter extends BaseAdapter {

    private ArrayList<String> titleList = new ArrayList<>(
            Arrays.asList("Buenos Aires", "Córdoba", "La Plata"));

    //to format the lines
    private LayoutInflater inflater;

    public ReadingsListAdapter(Context context, ArrayList<String> titleList){
        this.titleList = titleList;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * returns the number of lines of the list
     * @return
     */
    @Override
    public int getCount() {
        return titleList.size();
    }

    /**
     * returns item of the current line
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return titleList.get(position);
    }

    /**
     * returns an index for the current line
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder{
        ImageButton plusButton;
        TextView readingsTitle;
        TextView readingsAuthor;
    }

    /**
     * returns the formatted line with events management
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //declaration of an older
        ViewHolder holder;

        //verify if line exists
        if(convertView == null){
            holder = new ViewHolder();

            //the line is formatted (inflater) linked to the readings list layout
            convertView = inflater.inflate(R.layout.layout_list_readings, null);

            //all properties of the holder is associated with a graphic property
            holder.readingsTitle = (TextView) convertView.findViewById(R.id.readings_title);
            holder.readingsAuthor = (TextView) convertView.findViewById(R.id.readings_author);
            holder.plusButton = (ImageButton) convertView.findViewById(R.id.plus_button);

            //affect holder to the view
            convertView.setTag(holder);
        } else{
            //get the holder into the existing line
            holder = (ViewHolder) convertView.getTag();

        }

        //fill the holder values -> lines, get data from dataBase
        //holder.readingsTitle.setText(titleList.get(position).getBookTitle()));
        //holder.readingsAuthor.setText((titleList.get(position).getAuthorByTitle()));

        //we need to get the current line's button
        holder.plusButton.setTag(position);

        return convertView;
    }


}
