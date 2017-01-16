package nhoxs25.yenvo.newspaperapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nhoxs25.yenvo.newspaperapp.R;
import nhoxs25.yenvo.newspaperapp.model.RssItem;

/**
 * Created by yenvo on 16/01/2017.
 */

public class NewsAdapter extends ArrayAdapter<RssItem> {
    private Context context;
    private List<RssItem> items;

    public NewsAdapter(Context context, int ivIcon, List<RssItem> items) {
        super(context, ivIcon, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup paprent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_title_bai_bao, paprent, false);

        ImageView iv = (ImageView) rowView.findViewById(R.id.imgIcon);
        TextView tv = (TextView) rowView.findViewById(R.id.title);
        TextView txtPubdate = (TextView)rowView.findViewById(R.id.pubDate);

        tv.setText(items.get(position).getTitle().trim());
        txtPubdate.setText(items.get(position).getDate());

        if(items.get(position).getImg().length()<2){
            iv.setImageResource(R.drawable.icon_bai_bao);
        }else {
            Picasso.with(context).load(items.get(position).getImg())
                    .placeholder(R.drawable.icon_bai_bao).error(R.drawable.icon_bai_bao).into(iv);
        }
        return rowView;
    }
}
