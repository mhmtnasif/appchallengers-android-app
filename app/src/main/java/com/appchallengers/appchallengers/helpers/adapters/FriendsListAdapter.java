package com.appchallengers.appchallengers.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.webservice.response.FriendsList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jir on 21.2.2018.
 */

public class FriendsListAdapter extends BaseAdapter {

    private Context mContext;
    public static List<FriendsList> mFriendList;
    private ArrayList<FriendsList> mFriendArray;
    private boolean[] mItemChecked;

    public FriendsListAdapter(Context context, List<FriendsList> list) {
        mContext = context;
        mFriendList = list;
        mFriendArray = new ArrayList<>();
        mFriendArray.addAll(mFriendList);
        mItemChecked = new boolean[list.size()];
    }

    @Override
    public int getCount() {
        return mFriendList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFriendList.get(i);
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
            rowView = inflater.inflate(R.layout.list_item_friends, null);
            viewHolder = new ViewHolder();
            viewHolder.mFriendProfilePicture = (ImageView) rowView.findViewById(R.id.friend_list_profile_picture);
            viewHolder.mFriendsFullName = (TextView) rowView.findViewById(R.id.friend_list_full_name);
            viewHolder.mFriendsCheckBox = (CheckBox) rowView.findViewById(R.id.friend_list_checkbox);
            viewHolder.mFriendsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int getPosition = (Integer) compoundButton.getTag();
                    mFriendList.get(getPosition).setSelected(compoundButton.isChecked());
                }
            });
            rowView.setTag(viewHolder);
            rowView.setTag(R.id.friend_list_profile_picture, viewHolder.mFriendProfilePicture);
            rowView.setTag(R.id.friend_list_full_name, viewHolder.mFriendsFullName);
            rowView.setTag(R.id.friend_list_checkbox, viewHolder.mFriendsCheckBox);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mFriendsCheckBox.setTag(position);
        if(mFriendList.get(position).getProfile_picture()!=null){
            Picasso.with(mContext).load(mFriendList.get(position).getProfile_picture()).into(viewHolder.mFriendProfilePicture);
        }
        viewHolder.mFriendsFullName.setText(mFriendList.get(position).getFullName());
        viewHolder.mFriendsCheckBox.setChecked(mFriendList.get(position).isSelected());
        return rowView;
    }

    public void selectAll() {
        mFriendList.clear();
        for (FriendsList friendsList:mFriendArray){
            friendsList.setSelected(true);
            mFriendList.add(friendsList);
        }
        notifyDataSetChanged();
    }

    public void unSelectAll() {
        mFriendList.clear();
        for (FriendsList friendsList:mFriendArray){
            friendsList.setSelected(false);
            mFriendList.add(friendsList);
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView mFriendProfilePicture;
        TextView mFriendsFullName;
        CheckBox mFriendsCheckBox;
    }

    public void getAll() {
        mFriendList.clear();
        mFriendList.addAll(mFriendArray);
        notifyDataSetChanged();
    }

    public boolean getSelectedItems() {
        mFriendList.clear();
        for (FriendsList friendsList : mFriendArray) {
            if (friendsList.isSelected()) {
                mFriendList.add(friendsList);
            }
        }
        if (mFriendList.isEmpty()) {
            mFriendList.addAll(mFriendArray);
            notifyDataSetChanged();
            return false;
        }else{
            notifyDataSetChanged();
            return true;
        }
    }
    public List<FriendsList> getSelectedItemsList() {
        List<FriendsList> temp=new LinkedList<>();
        for (FriendsList friendsList : mFriendArray) {
            if (friendsList.isSelected()) {
                temp.add(friendsList);
            }
        }
        return temp;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mFriendList.clear();
        if (charText.length() == 0) {
            mFriendList.addAll(mFriendArray);

        } else {
            for (FriendsList friendsList : mFriendArray) {
                if (charText.length() != 0 && friendsList.getFullName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mFriendList.add(friendsList);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void filterTheSelected(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mFriendList.clear();
        if (charText.length() == 0) {
            getSelectedItems();

        } else {
            for (FriendsList friendsList : mFriendArray) {
                if (charText.length() != 0 && friendsList.getFullName().toLowerCase(Locale.getDefault()).contains(charText) &&friendsList.isSelected()) {
                    mFriendList.add(friendsList);
                }
            }
        }
        notifyDataSetChanged();
    }
}
