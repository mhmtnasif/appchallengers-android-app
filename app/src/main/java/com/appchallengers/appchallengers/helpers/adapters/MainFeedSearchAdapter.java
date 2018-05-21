package com.appchallengers.appchallengers.helpers.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jir on 27.4.2018.
 */

public class MainFeedSearchAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<UserBaseDataModel> mResponsUserBase;


    public MainFeedSearchAdapter(Context context, List<UserBaseDataModel> mResponsUserBase) {
        mContext = context;
        this.mResponsUserBase=mResponsUserBase;
    }

    @Override
    public int getCount() {
        return mResponsUserBase.size();
    }

    @Override
    public UserBaseDataModel getItem(int index) {
        return mResponsUserBase.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_show_serch, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.show_search_activity_user_name)).setText(getItem(position).getFullName());
        Picasso.with(mContext).load(getItem(position).getProfile_picture()).into(((CircleImageView) convertView.findViewById(R.id.show_search_activity_user_picture)));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<UserBaseDataModel> response ;
                    response =mResponsUserBase;
                    filterResults.values = response;
                    filterResults.count = response.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                  //  mResponsUserBase =  results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

}
