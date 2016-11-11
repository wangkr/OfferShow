package offershow.online.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import offershow.online.R;
import offershow.online.model.MessageInfo;

/**
 * Created by Kairong on 2016/11/10.
 * mail:wangkrhust@gmail.com
 */

public class MessageItemAdapter extends BaseAdapter {
    private LinkedList<MessageInfo> items;
    private LayoutInflater layoutInflater;

    public MessageItemAdapter(LinkedList<MessageInfo> items, Context context) {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public MessageInfo getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_message_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.time_tv.setText(items.get(i).getTime());
        viewHolder.content_tv.setText(items.get(i).getContent());

        return view;
    }

    public class ViewHolder {
        private TextView time_tv;
        private TextView content_tv;
        ViewHolder(View view){
            time_tv = (TextView)view.findViewById(R.id.message_item_time);
            content_tv = (TextView)view.findViewById(R.id.message_item_content);
        }
    }
}
