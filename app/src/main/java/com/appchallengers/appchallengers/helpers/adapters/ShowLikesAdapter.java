package com.appchallengers.appchallengers.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jir on 21.2.2018.
 */

public class ShowLikesAdapter extends BaseAdapter {

    private Context mContext;
    public static List<UserBaseDataModel> userBaseDataModelList;
    private ArrayList<UserBaseDataModel> userBaseDataModelArrayList;

    public ShowLikesAdapter(Context context, List<UserBaseDataModel> list) {
        mContext = context;
        userBaseDataModelList = list;
        userBaseDataModelArrayList = new ArrayList<>();
        userBaseDataModelArrayList.addAll(userBaseDataModelList);
    }

    @Override
    public int getCount() {
        return userBaseDataModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return userBaseDataModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        final ViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            rowView = inflater.inflate(R.layout.list_item_show_likes, null);
            viewHolder = new ViewHolder();
            viewHolder.mUserProfilePicture = (ImageView) rowView.findViewById(R.id.show_likes_fragment_user_picture);
            viewHolder.mUserFullName = (TextView) rowView.findViewById(R.id.show_likes_fragment_user_name);
            rowView.setTag(viewHolder);
            rowView.setTag(R.id.show_likes_fragment_user_picture, viewHolder.mUserProfilePicture);
            rowView.setTag(R.id.show_likes_fragment_user_name, viewHolder.mUserFullName);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(userBaseDataModelList.get(position).getProfile_picture()!=null){
            Picasso.with(mContext).load(userBaseDataModelList.get(position).getProfile_picture()).into(viewHolder.mUserProfilePicture);
        }
        viewHolder.mUserFullName.setText(userBaseDataModelList.get(position).getFullName());
        return rowView;
    }




    private static class ViewHolder {
        ImageView mUserProfilePicture;
        TextView mUserFullName;
    }





    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        userBaseDataModelList.clear();
        if (charText.length() == 0) {
            userBaseDataModelList.addAll(userBaseDataModelArrayList);

        } else {
            for (UserBaseDataModel userBaseDataModel : userBaseDataModelArrayList) {
                if (charText.length() != 0 && userBaseDataModel.getFullName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    userBaseDataModelList.add(userBaseDataModel);
                }
            }
        }
        notifyDataSetChanged();
    }

}
