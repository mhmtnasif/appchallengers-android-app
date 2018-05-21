package com.appchallengers.appchallengers.fragments.show_setting_user_activity_fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.ShowProfilActivity;
import com.appchallengers.appchallengers.helpers.adapters.CountryListAdapter;
import com.appchallengers.appchallengers.helpers.database.CountriesTable;
import com.appchallengers.appchallengers.helpers.database.Database;
import com.appchallengers.appchallengers.helpers.util.CheckPermissions;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.helpers.util.InternetControl;
import com.appchallengers.appchallengers.helpers.util.SaveImageToDirectoryUtils;
import com.appchallengers.appchallengers.helpers.util.Utils;
import com.appchallengers.appchallengers.webservice.remote.UserAccount;
import com.appchallengers.appchallengers.webservice.remote.UserAccountApiClient;
import com.appchallengers.appchallengers.webservice.response.CountryList;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;


public class SettingProfileFragment extends Fragment implements View.OnClickListener {
    private CompositeDisposable mCompositeDisposable;
    private View mRootView;
    private ListView mCountryListView;
    private List<CountryList> mCountryList;
    private String mProfileImageUrl;
    private CircleImageView mProfileImage;
    private boolean mPictureStatus;
    private boolean mPictureDeleteStatus;
    private CircularProgressButton mUpDateButton;
    private CountryListAdapter mCountryListAdapter;
    private EditText mFullName;
    private EditText mProfilSettingCountryEdit;
    private EditText mSettinUpCountry;
    private TextView mEmail;
    private TextView mProfilImageChangeLink;
    private TextView mCustomAlertDialogCamera;
    private TextView mCustomAlertDialogLoad;
    private TextView mCustomAlertDialogdelete;
    private TextView mCustomAlertDialogGalery;
    private Animation mShakeAnimation;
    private LinearLayout mLinearLayout;
    private final int GALLERY = 159;
    private final int CAMERA = 158;
    private SharedPreferences mSharedPreferences;
    private Observable<Response<Void>> mProfileSetting;
    private Observable<Response<Void>> mProfileSettingdelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_setting_profile, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View mRootView) {
        mCompositeDisposable = new CompositeDisposable();
        mUpDateButton = (CircularProgressButton) mRootView.findViewById(R.id.setting_fragment_up_date_button);
        mProfileImage=(CircleImageView) mRootView.findViewById(R.id.fragment_setting_profile_image);
        mProfilImageChangeLink=(TextView) mRootView.findViewById(R.id.fragment_setting_profile_change_link);
        mFullName=(EditText)mRootView.findViewById(R.id.fragment_setting_profile_name_edittext);
        mEmail=(TextView) mRootView.findViewById(R.id.fragment_setting_profile_email_edittext);
        mSharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mShakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        mSettinUpCountry = (EditText) mRootView.findViewById(R.id.fragment_setting_profile_country_edittext);
        mLinearLayout = (LinearLayout)  mRootView.findViewById(R.id.fragment_profil_setting_ll);
        mSettinUpCountry.setOnClickListener(this);
        mUpDateButton.setOnClickListener(this);
        mEmail.setOnClickListener(this);
        mProfileImage.setOnClickListener(this);
        mProfilImageChangeLink.setOnClickListener(this);
        mPictureStatus=false;
        getProfilSettingInfo();
    }
    private void getProfilSettingInfo(){
        Utils.sharedPreferences = mSharedPreferences;
        mFullName.setText(Utils.getPref("fullName"));
        mSettinUpCountry.setText(Utils.getPref("country"));
        mEmail.setText(Utils.getPref("email"));
        mProfileImageUrl=Utils.getPref("imageUrl");
        if (mProfileImageUrl!=null){
            Picasso.with(getContext()).load(mProfileImageUrl).into(mProfileImage);
            mPictureDeleteStatus=true;
        }
        else  mPictureDeleteStatus=false;
    }

    private void upDateAccountWithImage() {
        ButtonActionActive();
        UserAccount userClient = UserAccountApiClient.getUserAccountClient(getContext());
        File file = new File(mProfileImageUrl);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody fullName = RequestBody.create(MultipartBody.FORM, mFullName.getText().toString());
        RequestBody country = RequestBody.create(MultipartBody.FORM, mSettinUpCountry.getText().toString());
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Call<UserBaseDataModel> createAccountResponseModelCall = userClient.upDateAccountWithImage(fullName, country, body);
        createAccountResponseModelCall.enqueue(new Callback<UserBaseDataModel>() {
            @Override
            public void onResponse(Call<UserBaseDataModel> call, Response<UserBaseDataModel> response) {

                if (response.isSuccessful()) {
                    Utils.sharedPreferences = mSharedPreferences;
                    Utils.setSharedPreferences("fullName", mFullName.getText().toString());
                    Utils.setSharedPreferences("imageUrl", response.body().getProfile_picture());
                    Utils.setSharedPreferences("country", mSettinUpCountry.getText().toString());
                    ButtonActionPasif();
                    startActivity(new Intent(getContext(), ShowProfilActivity.class));
                    getActivity().finish();
                } else {
                    if (response.code() == 400) {
                        if (response.errorBody() != null) {
                            try {
                                ErrorHandler.getInstance(getContext()).showEror(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                    }
                    ButtonActionPasif();
                }
            }

            @Override
            public void onFailure(Call<UserBaseDataModel> call, Throwable t) {
                if (t instanceof IOException) {
                    if (t instanceof java.net.ConnectException) {
                        ErrorHandler.getInstance(getContext()).showInfo(300);
                    }
                } else {
                    ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                }
                ButtonActionPasif();

            }
        });
    }

    private void upDateAccountWithoutImage() {
        UserAccount userAccount = UserAccountApiClient.getUserAccountClient(getContext());
        String name = mFullName.getText().toString();
        String country = mSettinUpCountry.getText().toString();
        mProfileSetting = userAccount.upDateAccountWithoutImage(name, country);
        mProfileSetting.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        ButtonActionActive();
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<Void> value) {
                        if (value.isSuccessful()) {
                            Utils.sharedPreferences = mSharedPreferences;
                            Utils.setSharedPreferences("fullName", mFullName.getText().toString());
                            Utils.setSharedPreferences("country", mSettinUpCountry.getText().toString());
                            ButtonActionPasif();
                            startActivity(new Intent(getContext(), ShowProfilActivity.class));
                            getActivity().finish();
                        } else {
                            if (value.code() == 400) {
                                if (value.errorBody() != null) {
                                    try {
                                        ErrorHandler.getInstance(getContext()).showEror(value.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                            }
                            ButtonActionPasif();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            if (e instanceof java.net.ConnectException) {
                                ErrorHandler.getInstance(getContext()).showInfo(300);
                            }
                        } else {
                            ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                        }
                        ButtonActionPasif();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void deleteImageProfile() {
        UserAccount userAccount = UserAccountApiClient.getUserAccountClient(getContext());
        mProfileSettingdelete = userAccount.deleteAccountImage();
        mProfileSettingdelete.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<Void> getUserInfoResponseModelResponse) {
                        if (getUserInfoResponseModelResponse.isSuccessful()) {
                            Utils.sharedPreferences = mSharedPreferences;
                            Utils.setSharedPreferences("imageUrl", null);
                        } else {
                            ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            if (e instanceof java.net.ConnectException) {
                                ErrorHandler.getInstance(getContext()).showInfo(300);
                            }
                        } else {
                            ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private boolean checkPermission() {
        return CheckPermissions.getInstance().hasPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE});
    }
    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(contentURI, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageCompressor(cursor.getString(columnIndex));
                    cursor.close();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            String path = null;
            try {
                path = SaveImageToDirectoryUtils.getOutputMediaFileUri(getActivity().getBaseContext(), thumbnail);
                imageCompressor(path);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (path != null)
                    new File(path);
            }
        }
    }

    private void imageCompressor(String path) {
        new Compressor(getContext())
                .compressToFileAsFlowable(new File(path))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                        try {
                            mProfileImageUrl = SaveImageToDirectoryUtils.getOutputMediaFileUri(getActivity().getBaseContext(), bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mPictureStatus=true;
                        mProfileImage.setImageBitmap(bitmap);
                        Log.e("galery", mProfileImageUrl);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        Log.e("image compressor", throwable.getMessage());
                    }
                });
    }

    private void showSettingPhotoDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_alert_dialog_select_profile_setting_photo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCustomAlertDialogLoad = (TextView) dialog.findViewById(R.id.custom_alert_dialog_setting_photo_load);
        mCustomAlertDialogdelete = (TextView) dialog.findViewById(R.id.custom_alert_dialog_setting_photo_delete);
        mCustomAlertDialogLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
                dialog.dismiss();
            }
        });
        if (!mPictureDeleteStatus){
            mCustomAlertDialogdelete.setVisibility(View.GONE);
        }
        mCustomAlertDialogdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPictureDeleteStatus=true;
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showPictureDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_alert_dialog_select_image_provider);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCustomAlertDialogCamera = (TextView) dialog.findViewById(R.id.custom_alert_dialog_image_provider_camera);
        mCustomAlertDialogGalery = (TextView) dialog.findViewById(R.id.custom_alert_dialog_image_provider_galery);
        mCustomAlertDialogGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY);
                dialog.dismiss();
            }
        });
        mCustomAlertDialogCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); // Resim çekme isteği ve activity başlatılıp id'si tanımlandı
                startActivityForResult(kamera, CAMERA);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void ButtonActionActive() {
        mUpDateButton.startAnimation();
        mUpDateButton.setInitialCornerRadius(75);
    }

    private void ButtonActionPasif() {
        mUpDateButton.revertAnimation();
    }
    private void countryDialog() {
        Database database = new Database();
        CountriesTable countriesTable = new CountriesTable(database.open(getContext()));
        mCountryList = countriesTable.getList();
        database.close();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        mCountryListAdapter = new CountryListAdapter(getContext(), mCountryList);
        final DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                .setContentHolder(new ViewHolder(R.layout.fragment_contry_select))
                .setCancelable(true)
                .setInAnimation(R.anim.down_to_up_animation)
                .setContentHeight(height - 50)
                .create();
        mCountryListView = dialogPlus.getHolderView().findViewById(R.id.country_select_fragment_listview);
        mCountryListAdapter = new CountryListAdapter(getContext(), mCountryList);
        mCountryListView.setAdapter(mCountryListAdapter);
        mCountryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSettinUpCountry.setText(mCountryList.get(i).getCountryName());
                dialogPlus.dismiss();
                View view1 = getActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
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
        mProfilSettingCountryEdit = (EditText) dialogPlus.getHolderView().findViewById(R.id.country_select_fragment_edittext);
        mProfilSettingCountryEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    dialogPlus.dismiss();
                    return true;
                }
                return false;
            }
        });
        mProfilSettingCountryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCountryListAdapter.filter(charSequence.toString());
                mCountryListView.invalidate();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialogPlus.show();
    }
    private boolean checkValidation() {
        String fullName = mFullName.getText().toString();
        String email=mEmail.getText().toString();
        return Utils.checkValidation(new String[]{email,fullName}, mLinearLayout, mShakeAnimation, getActivity(), mRootView);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_setting_profile_image: {
                if (checkPermission())
                    showSettingPhotoDialog();
                break;
            }
            case R.id.fragment_setting_profile_change_link: {
                if (checkPermission())
                    showSettingPhotoDialog();
                break;
            }

            case R.id.setting_fragment_up_date_button: {
                if (checkValidation()){
                    if (mPictureStatus&&!mPictureDeleteStatus) {
                        InternetControl.getInstance().showSnack(mUpDateButton);
                        upDateAccountWithImage();
                    }
                    else if(mPictureDeleteStatus) {
                        InternetControl.getInstance().showSnack(mUpDateButton);
                        deleteImageProfile();
                        upDateAccountWithoutImage();
                    }
                    else upDateAccountWithoutImage();
                }
                break;
            }
            case R.id.fragment_setting_profile_country_edittext:{
                 countryDialog();
            }
        }
    }
}
