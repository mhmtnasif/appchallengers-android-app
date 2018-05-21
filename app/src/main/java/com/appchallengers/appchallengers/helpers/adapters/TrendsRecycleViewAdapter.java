package com.appchallengers.appchallengers.helpers.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appchallengers.appchallengers.ChallengeDetailActivity;
import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.webservice.response.TrendsDataModel;

import java.util.List;

public class TrendsRecycleViewAdapter extends RecyclerView.Adapter<TrendsRecycleViewAdapter.TrendsCardHolder> {

    private List<TrendsDataModel> mTrendsDataModel;
    private Activity mActivity;

    public TrendsRecycleViewAdapter(List<TrendsDataModel> mTrendsDataModel, Activity mActivity) {
        this.mTrendsDataModel = mTrendsDataModel;
        this.mActivity = mActivity;
    }

    @Override

    public TrendsCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trends_cards_item, parent, false);
        TrendsCardHolder item = new TrendsCardHolder(v);
        return item;
    }

    @Override
    public void onBindViewHolder(TrendsCardHolder holder, int position) {
        holder.trendHeadLine.setText(mTrendsDataModel.get(position).getHeadLine());
        holder.trendCounter.setText(mTrendsDataModel.get(position).getCounter() +" challenge");
    }

    @Override
    public int getItemCount() {
        return mTrendsDataModel.size();
    }

    public class TrendsCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView trendHeadLine;
        TextView trendCounter;

        public TrendsCardHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.trends_cardview);
            trendHeadLine = (TextView) itemView.findViewById(R.id.headLine);
            trendCounter = (TextView) itemView.findViewById(R.id.headline_counter);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TrendsDataModel card = mTrendsDataModel.get(position);
            switch (view.getId()) {
                case R.id.trends_cardview: {
                    Intent intent = new Intent(mActivity, ChallengeDetailActivity.class);
                    intent.putExtra("challenge_detail_id", card.getChallengeId());
                    mActivity.startActivity(intent);
                }
            }
        }
    }
}
