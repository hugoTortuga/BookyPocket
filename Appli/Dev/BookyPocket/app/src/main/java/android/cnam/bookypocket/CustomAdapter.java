package android.cnam.bookypocket;

import android.app.Activity;
import android.cnam.bookypocket.Model.Book;
import android.content.Context;
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

public class CustomAdapter extends ArrayAdapter<Book> {

    ArrayList<Book> dataSet;
    Activity mContext;

    public CustomAdapter(@NonNull Activity context, ArrayList<Book> dataSet ) {
        super(context, R.layout.book_item_list, dataSet);

        this.dataSet=dataSet;
        this.mContext=context;

    }

    private static class ViewHolder
    {
        TextView txtNom;
        ImageView image;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book book= getItem(position);
        ViewHolder viewHolder =new ViewHolder();
        if(convertView==null)
        {

            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.book_item_list,parent,false);
            viewHolder.txtNom= convertView.findViewById(R.id.text_book_item_list);
            viewHolder.image = convertView.findViewById(R.id.img_book_item_list);
            convertView.setTag(viewHolder);
        }
        else
        {
            convertView.getTag();
        }

        if(book.getPhoto() != null) {
            byte[] chartData = book.getPhoto().getImage();
            Bitmap bm = BitmapFactory.decodeByteArray(chartData, 0, chartData.length);
            DisplayMetrics dm = new DisplayMetrics();
            mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);

            if(viewHolder.image != null){
                viewHolder.image.setMinimumHeight(dm.heightPixels);
                viewHolder.image.setMinimumWidth(dm.widthPixels);
                viewHolder.image.setImageBitmap(bm);
            }

        }


        if(book != null)
            if(book.getTitle() != null)
                if(viewHolder != null)
                    if(viewHolder.txtNom != null)
                        viewHolder.txtNom.setText(book.getTitle());

        return convertView ;
    }
}
