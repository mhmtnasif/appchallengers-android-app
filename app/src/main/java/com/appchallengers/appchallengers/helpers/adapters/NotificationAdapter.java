package com.appchallengers.appchallengers.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.webservice.response.NotificationResponseModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {

    private Context mContext;
    private List<NotificationResponseModel> mNotificationList;

    public NotificationAdapter(Context context, List<NotificationResponseModel> list) {
        mContext = context;
        mNotificationList = list;
    }

    @Override
    public int getCount() {
        return mNotificationList.size();
    }

    @Override
    public Object getItem(int i) {
        return mNotificationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        final ViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            rowView = inflater.inflate(R.layout.list_item_natification, null);
            viewHolder = new ViewHolder();
            viewHolder.notification_text = (TextView) rowView.findViewById(R.id.natification_activity_notification_text);
            viewHolder.user_profile_picture = (ImageView) rowView.findViewById(R.id.natification_activity_user_profile_picture);
            rowView.setTag(viewHolder);
            rowView.setTag(R.id.natification_activity_notification_text, viewHolder.notification_text);
            rowView.setTag(R.id.natification_activity_user_profile_picture, viewHolder.user_profile_picture);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
//todo change strings
        String name = mNotificationList.get(i).getFromUserFullName();
        String natification_content = "";
        if (mNotificationList.get(i).getType() == 0) {
            natification_content = " " + "sana " + mNotificationList.get(i).getChallengeHeadLine() + " için meydan okudu";
        } else if (mNotificationList.get(i).getType() == 1) {
            natification_content = " " + "sana arkadaş isteği gönderdi";
        } else if (mNotificationList.get(i).getType() == 2) {
            natification_content = " " + "senin arkadaş isteğini kabul etti";
        } else if (mNotificationList.get(i).getType() == 3) {
            natification_content = " " + "senin " + mNotificationList.get(i).getChallengeHeadLine() + " için meydan okumanı beğendi";
        }
        viewHolder.notification_text.setText(name + natification_content);
        if(mNotificationList.get(i).getFromUserProfilePicture()!=null){
            Picasso.with(mContext).load(mNotificationList.get(i).getFromUserProfilePicture()).into(viewHolder.user_profile_picture);
        }
        return rowView;
    }

    private static class ViewHolder {
        TextView notification_text;
        ImageView user_profile_picture;
    }
}
