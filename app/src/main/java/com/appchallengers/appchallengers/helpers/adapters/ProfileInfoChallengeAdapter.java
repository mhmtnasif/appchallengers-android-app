package com.appchallengers.appchallengers.helpers.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appchallengers.appchallengers.ChallengeDetailActivity;
import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.ShowUserActivity;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.webservice.remote.UserVote;
import com.appchallengers.appchallengers.webservice.remote.UserVoteApiClient;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;
import com.appchallengers.appchallengers.webservice.response.UserChallengeFeedListModel;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.List;

import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.SimpleExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProfileInfoChallengeAdapter extends RecyclerView.Adapter<ProfileInfoChallengeAdapter.UserFeedAdapterHelper> {
    private Context mContext;
    private List<UserChallengeFeedListModel> mCardList;
    private Activity mActivity;
    private SpannableString mSpannableString;
    private long mChallangeId;

    @Override
    public UserFeedAdapterHelper onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flow_video_cardview, parent, false);

        return new UserFeedAdapterHelper(view);
    }

    public ProfileInfoChallengeAdapter(Context mContext, List<UserChallengeFeedListModel> mCardList, Activity activity) {
        this.mContext = mContext;
        this.mCardList = mCardList;
        mActivity = activity;

    }

    @Override
    public void onBindViewHolder(final UserFeedAdapterHelper holder, int position) {
        final UserChallengeFeedListModel cardlist = mCardList.get(position);
        holder.bind(Uri.parse(cardlist.getChallenge_url()));
        holder.fullname.setText(holder.spannableStringModel(cardlist.getFullname() + " ", " " + cardlist.getHeadline()));
        holder.fullname.setMovementMethod(LinkMovementMethod.getInstance());
        holder.like.setText(cardlist.getLikes() + " beğeni");
        if (cardlist.getVote() == 1) {
            holder.likebutton.setTextColor(Color.parseColor("#FD5739"));
            holder.likebutton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_favorite_orange_24dp, 0, 0, 0);
        }
        if (cardlist.getProfilepicture()!=null){
            Picasso.with(mContext).load(cardlist.getProfilepicture()).into(holder.imageview);
        }

    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }



    public class UserFeedAdapterHelper extends RecyclerView.ViewHolder implements ToroPlayer, View.OnClickListener {

        public TextView like;
        public TextView likebutton;
        public TextView fullname;
        public ImageView imageview;
        private SimpleExoPlayerView playerView;
        private SimpleExoPlayerViewHelper helper;
        private Uri mediaUri;
        private Observable<Response<Void>> voteObservable;
        private CompositeDisposable mCompositeDisposable;
        private List<UserBaseDataModel> userBaseDataModelList;
        private ShowLikesAdapter mShowLikesAdapter;
        private ListView mShowLikesListview;
        private RotateLoading mRotateLoading;
        private Observable<Response<List<UserBaseDataModel>>> mResponseObservable;

        public UserFeedAdapterHelper(View itemView) {
            super(itemView);
            playerView = (SimpleExoPlayerView) itemView.findViewById(R.id.flow_video_cardview_videoview);
            imageview = (ImageView) itemView.findViewById(R.id.flow_video_cardview_profil_picture);
            fullname = (TextView) itemView.findViewById(R.id.flow_video_cardview_fullname_and_headline);
            like = (TextView) itemView.findViewById(R.id.flow_video_cardview_like);
            likebutton = (TextView) itemView.findViewById(R.id.flow_video_cardview_like_button);
            mCompositeDisposable = new CompositeDisposable();
            likebutton.setOnClickListener(this);
            like.setOnClickListener(this);
        }

        @Override
        public View getPlayerView() {
            return playerView;
        }

        @Override
        public PlaybackInfo getCurrentPlaybackInfo() {
            return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
        }

        @Override
        public void initialize(Container container, PlaybackInfo playbackInfo) {
            if (helper == null) {
                helper = new SimpleExoPlayerViewHelper(container, this, mediaUri);
            }
            helper.initialize(playbackInfo);
        }


        @Override
        public void play() {
            if (helper != null) helper.play();
        }

        @Override
        public void pause() {
            if (helper != null) helper.pause();
        }

        @Override
        public boolean isPlaying() {
            return helper != null && helper.isPlaying();
        }

        @Override
        public void release() {
            if (helper != null) {
                helper.release();
                helper = null;
            }
        }

        @Override
        public boolean wantsToPlay() {
            return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
        }

        @Override
        public int getPlayerOrder() {
            return getAdapterPosition();
        }


        void bind(Uri media) {
            this.mediaUri = media;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            UserChallengeFeedListModel cardlist = mCardList.get(position);
            mChallangeId=cardlist.getChallenge_id();
            switch (view.getId()) {
                case R.id.flow_video_cardview_like_button: {
                    vote(position, cardlist);
                    break;
                }
                case R.id.flow_video_cardview_like: {
                    getVoteList(cardlist.getChallenge_detail_id());
                    break;
                }
                case R.id.flow_video_cardview_fullname_and_headline: {

                }
            }
        }
        private SpannableString spannableStringModel(final String name, final String headline) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_grey_24dp);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            mSpannableString = new SpannableString(name + headline);
            mSpannableString.setSpan(name, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpannableString.setSpan(span, name.length(), name.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpannableString.setSpan(new MyClickableSpan(headline), name.length() + 1, headline.length() + name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return mSpannableString;
        }
        class MyClickableSpan extends ClickableSpan{// extend ClickableSpan

            String clicked;
            public MyClickableSpan(String string) {
                super();
                clicked = string;
            }

            public void onClick(View tv) {
                int position = getAdapterPosition();
                UserChallengeFeedListModel cardlist = mCardList.get(position);
                mChallangeId=cardlist.getChallenge_id();
                Intent intent = new Intent(mActivity, ChallengeDetailActivity.class);
                intent.putExtra("challenge_detail_id", mChallangeId);
                mActivity.startActivity(intent);
                mActivity.finish();

            }

            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.parseColor("#24243D"));
                ds.setUnderlineText(false);
            }
        }

        public void vote(final int position, final UserChallengeFeedListModel cardlist) {
            UserVote userVote = UserVoteApiClient.getVoteClient(mContext);
            voteObservable = userVote.vote(cardlist.getChallenge_detail_id());
            voteObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<Void>>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            mCompositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<Void> voidResponse) {
                            if (voidResponse.isSuccessful()) {
                                if (cardlist.getVote() == 1) {
                                    cardlist.setLikes(cardlist.getLikes() - 1);
                                    cardlist.setVote(0);
                                    likebutton.setTextColor(Color.parseColor("#24243D"));
                                    likebutton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_favorite_border_gray_24dp, 0, 0, 0);
                                    notifyItemChanged(position, likebutton);
                                } else if (cardlist.getVote() == 0) {
                                    cardlist.setLikes(cardlist.getLikes() + 1);
                                    cardlist.setVote(1);
                                    likebutton.setTextColor(Color.parseColor("#FD5739"));
                                    likebutton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_favorite_orange_24dp, 0, 0, 0);
                                    notifyItemChanged(position, likebutton);
                                }
                            }
                            else {
                                if (voidResponse.code() == 400) {
                                    if (voidResponse.errorBody() != null) {
                                        try {
                                            ErrorHandler.getInstance(mActivity.getApplicationContext()).showEror(voidResponse.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    ErrorHandler.getInstance(mActivity.getApplicationContext()).showEror("{code:1000}");
                                }
                            }
                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (cardlist.getVote() == 1) {
                                cardlist.setLikes(cardlist.getLikes()-1);
                                cardlist.setVote(0);
                                likebutton.setTextColor(Color.parseColor("#24243D"));
                                likebutton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_favorite_border_gray_24dp, 0, 0, 0);
                                notifyItemChanged(position, likebutton);
                            } else if (cardlist.getVote() == 0) {
                                cardlist.setLikes(cardlist.getLikes()+1);
                                cardlist.setVote(1);
                                likebutton.setTextColor(Color.parseColor("#FD5739"));
                                likebutton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_favorite_orange_24dp, 0, 0, 0);
                                notifyItemChanged(position, likebutton);
                            }
                        }
                    });
        }
        public void getVoteList(long challenge_detail_id) {
            UserVote userVote = UserVoteApiClient.getVoteClientWithCache(mContext);
            mResponseObservable = userVote.getVoteList(challenge_detail_id);
            mResponseObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<List<UserBaseDataModel>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mCompositeDisposable.add(d);
                            showLikesDialog();
                            mRotateLoading.start();
                        }

                        @Override
                        public void onNext(Response<List<UserBaseDataModel>> value) {
                            userBaseDataModelList = value.body();
                            if (userBaseDataModelList.size() != 0 && userBaseDataModelList != null) {
                                mShowLikesAdapter = new ShowLikesAdapter(mContext, userBaseDataModelList);
                                mShowLikesListview.setAdapter(mShowLikesAdapter);
                            } else {
                                //todo there isnt anyone like this post
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            onComplete();
                            if (e instanceof IOException) {
                                if (e instanceof java.net.ConnectException) {
                                    ErrorHandler.getInstance(mContext).showInfo(300);
                                }
                            } else {
                                ErrorHandler.getInstance(mContext).showEror("{code:1000}");
                            }
                        }

                        @Override
                        public void onComplete() {
                            mRotateLoading.stop();
                            mRotateLoading.setVisibility(View.GONE);
                        }
                    });
        }

        private void showLikesDialog() {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            final DialogPlus dialogPlus = DialogPlus.newDialog(mContext)
                    .setContentHolder(new ViewHolder(R.layout.fragment_show_likes))
                    .setCancelable(true)
                    .setInAnimation(R.anim.down_to_up_animation)
                    .setContentHeight(height - 50)
                    .create();
            mRotateLoading = dialogPlus.getHolderView().findViewById(R.id.show_friends_fragment_rotateloading);
            mShowLikesListview = dialogPlus.getHolderView().findViewById(R.id.show_friends_fragment_listview);
            mShowLikesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    dialogPlus.dismiss();
                    Intent intent = new Intent(mActivity, ShowUserActivity.class);
                    intent.putExtra("user_id", userBaseDataModelList.get(i).getId());
                    mActivity.startActivity(intent);

                }
            });
            dialogPlus.getHolderView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (i == KeyEvent.KEYCODE_BACK) {
                        dialogPlus.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            dialogPlus.show();
        }

    }
}
