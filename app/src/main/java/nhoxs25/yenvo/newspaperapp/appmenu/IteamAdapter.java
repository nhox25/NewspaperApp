package nhoxs25.yenvo.newspaperapp.appmenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nhoxs25.yenvo.newspaperapp.R;
import nhoxs25.yenvo.newspaperapp.model.RssItem;

/**
 * Created by yenvo on 13/01/2017.
 */

public class IteamAdapter extends RecyclerView.Adapter<IteamAdapter.MyHolder> {
    Context context;
    List<RssItem> rssItems;
    LayoutInflater inflater;
    OnItemClick iOnItemClick;
    private int paper;

    public IteamAdapter(Context context, int paper, List<RssItem> rssItems) {
        this.context = context;
        this.paper = paper;
        this.rssItems = rssItems;
    }
    public IteamAdapter(Context context, List<RssItem> rssItems) {
        this.context = context;
        this.rssItems = rssItems;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_title_bai_bao,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.tv.setText(String.valueOf(rssItems.get(position).getTitel()));
        holder.txtPubdate.setText(String.valueOf(rssItems.get(position).getDate()));
        if(rssItems.get(position).getDescription().length()<2){
            holder.iv.setImageResource(R.drawable.icon_bai_bao);
        }else {
            Picasso.with(context).load(rssItems.get(position).getDescription())
                    .placeholder(R.drawable.icon_bai_bao).error(R.drawable.icon_bai_bao).into(holder.iv);
//            Picasso.with(context).load(items.get(position).getImg()).resize(100,100).into(iv);
        }

    }

    @Override
    public int getItemCount() {
        return rssItems.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv,txtPubdate;
        public MyHolder(View itemView) {
            super(itemView);
             iv = (ImageView) itemView.findViewById(R.id.imgIcon);
             tv = (TextView) itemView.findViewById(R.id.title);
             txtPubdate = (TextView)itemView.findViewById(R.id.pubDate);
        }
    }
    public interface OnItemClick{
        void onItemClick(int position);
    }

}
