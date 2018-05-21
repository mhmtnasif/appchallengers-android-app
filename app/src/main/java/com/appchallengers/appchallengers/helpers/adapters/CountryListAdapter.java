package com.appchallengers.appchallengers.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
    import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.webservice.response.CountryList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jir on 21.2.2018.
 */

public class    CountryListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CountryList> mCountriesListModelList;
    private ArrayList<CountryList> mCountriesListModelArray;

    public CountryListAdapter(Context context, List<CountryList> list) {
        mContext=context;
        mCountriesListModelList = list;
        mCountriesListModelArray = new ArrayList<>();
        mCountriesListModelArray.addAll(mCountriesListModelList);
    }

    @Override
    public int getCount() {
        return mCountriesListModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return mCountriesListModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        final ViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            rowView = inflater.inflate(R.layout.list_item_countries, null);
            viewHolder = new ViewHolder();
            viewHolder.mCountriesCharTextView = (TextView) rowView.findViewById(R.id.country_select_fragment_char_edittext);
            viewHolder.mCountriesNameTextView = (TextView) rowView.findViewById(R.id.country_select_fragment_name_edittext);
            rowView.setTag(viewHolder);
            rowView.setTag(R.id.country_select_fragment_char_edittext, viewHolder.mCountriesCharTextView);
            rowView.setTag(R.id.country_select_fragment_name_edittext, viewHolder.mCountriesNameTextView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mCountriesCharTextView.setText(mCountriesListModelList.get(position).getCountryName().charAt(0)+"");
        viewHolder.mCountriesNameTextView.setText(mCountriesListModelList.get(position).getCountryName());
        return rowView;
    }


    private static class ViewHolder {
        TextView mCountriesCharTextView;
        TextView mCountriesNameTextView;
    }
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        mCountriesListModelList.clear();
        if (charText.length() == 0) {
            mCountriesListModelList.addAll(mCountriesListModelArray);

        } else {
            for (CountryList countryList : mCountriesListModelArray) {
                if (charText.length() != 0 && countryList.getCountryName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mCountriesListModelList.add(countryList);
                }
            }
        }
        notifyDataSetChanged();
    }
}