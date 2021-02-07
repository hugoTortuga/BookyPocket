package android.cnam.bookypocket.Activities;

import android.app.Activity;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.StringUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Adapter pour ma liste d'utilisateur,
 * affiche un icone d'utilisateur et son nom prénom
 */
public class CustomReaderAdapter extends ArrayAdapter<Reader> {

    ArrayList<Reader> dataSet;
    Activity mContext;

    public CustomReaderAdapter(@NonNull Activity context, ArrayList<Reader> dataSet) {
        super(context, R.layout.reader_list_item, dataSet);
        this.dataSet = dataSet;
        this.mContext = context;
    }

    private static class ViewHolder {
        TextView txtNom;
        ImageView image;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reader reader = getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.reader_list_item, parent, false);
            viewHolder.txtNom = convertView.findViewById(R.id.text_reader_item_list);
            viewHolder.image = convertView.findViewById(R.id.img_reader_item_list);
            convertView.setTag(viewHolder);
        } else {
            convertView.getTag();
        }

        if (reader.getAvatar() != null) {
            byte[] chartData = reader.getAvatar().getImage();
            Bitmap bm = BitmapFactory.decodeByteArray(chartData, 0, chartData.length);
            DisplayMetrics dm = new DisplayMetrics();
            mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);


            if (viewHolder != null)
                if (viewHolder.image != null) {
                    viewHolder.image.setMinimumHeight(dm.heightPixels);
                    viewHolder.image.setMinimumWidth(dm.widthPixels);
                    viewHolder.image.setImageBitmap(bm);
                }
        }
        else{
            if(viewHolder != null)
                if (viewHolder.image != null)
                    viewHolder.image.setImageResource(R.drawable.user);
        }
                if (viewHolder != null)
                    if (viewHolder.txtNom != null)
                        if (reader != null)
                            if (!StringUtil.IsNullOrEmpty(reader.getFirstName()))
                            {
                                String toShow = reader.getFirstName();
                                if(!StringUtil.IsNullOrEmpty(reader.getLastName()))
                                    toShow += " " + reader.getLastName();
                                viewHolder.txtNom.setText(toShow);
                            }


        return convertView;
    }
}
