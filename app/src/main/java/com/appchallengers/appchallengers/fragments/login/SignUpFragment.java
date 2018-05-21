package com.appchallengers.appchallengers.fragments.login;


import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.WebViewActivity;
import com.appchallengers.appchallengers.helpers.adapters.CountryListAdapter;
import com.appchallengers.appchallengers.helpers.database.CountriesTable;
import com.appchallengers.appchallengers.helpers.database.Database;
import com.appchallengers.appchallengers.helpers.setpages.SetLoginPages;
import com.appchallengers.appchallengers.helpers.util.CheckPermissions;
import com.appchallengers.appchallengers.helpers.util.Constants;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.helpers.util.InternetControl;
import com.appchallengers.appchallengers.helpers.util.SaveImageToDirectoryUtils;
import com.appchallengers.appchallengers.helpers.util.Utils;
import com.appchallengers.appchallengers.webservice.remote.UserAccount;
import com.appchallengers.appchallengers.webservice.remote.UserAccountApiClient;
import com.appchallengers.appchallengers.webservice.request.SignUpRequestModel;
import com.appchallengers.appchallengers.webservice.response.CountryList;
import com.appchallengers.appchallengers.webservice.response.UserPreferencesData;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
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


public class SignUpFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private ImageView mSignUpProfileImage;
    private ImageView mSignUpBackArrow;
    private EditText mSignUpFullName;
    private EditText mSignUpEmail;
    private EditText mSignUpPassword;
    private EditText mSignUpCountry;
    private ProgressBar mProgressbar;
    private CircularProgressButton mSignUpButton;
    private LinearLayout mLinearLayout;
    private TextView mCustomAlertDialogCamera;
    private TextView mCustomAlertDialogGalery;
    private TextView mTermsAndCookie;
    private final int GALLERY = 159;
    private final int CAMERA = 158;
    private String mImageUrl = null;
    private Animation mShakeAnimation;
    private SharedPreferences mSharedPreferences;
    private CountryListAdapter mCountryListAdapter;
    private List<CountryList> mCountryList;
    private EditText mSearchEdittext;
    private ListView mCountryListView;
    private CompositeDisposable mCompositeDisposable;
    private Observable<Response<UserPreferencesData>> mResponseObservableWithoutImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initalView(mRootView);
        return mRootView;
    }

    @SuppressLint("ResourceType")
    private void initalView(View view) {
        mCompositeDisposable=new CompositeDisposable();
        mProgressbar = (ProgressBar) view.findViewById(R.id.sign_up_fragment_progressbar);
        mSignUpProfileImage = (ImageView) view.findViewById(R.id.sign_up_fragment_profile_image);
        mSignUpBackArrow = (ImageView) view.findViewById(R.id.sign_up_fragment_back_arrow_imageview);
        mSignUpFullName = (EditText) view.findViewById(R.id.sign_up_fragment_user_fullname_edittext);
        mSignUpEmail = (EditText) view.findViewById(R.id.sign_up_fragment_email_edittext);
        mSignUpPassword = (EditText) view.findViewById(R.id.sign_up_fragment_password_edittext);
        mSignUpCountry = (EditText) view.findViewById(R.id.sign_up_fragment_country_edittext);
        mSignUpButton = (CircularProgressButton) view.findViewById(R.id.sign_up_fragment_sign_up_button);
        mTermsAndCookie = (TextView) view.findViewById(R.id.sign_up_fragment_profile_terms_cookie);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.sign_up_fragment_profile_image_ll);
        mShakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        mSharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mSignUpBackArrow.setOnClickListener(this);
        mSignUpProfileImage.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
        mTermsAndCookie.setOnClickListener(this);
        mSignUpCountry.setOnClickListener(this);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_fragment_back_arrow_imageview: {
                SetLoginPages.getInstance().constructor(getActivity(), 1);
                break;
            }
            case R.id.sign_up_fragment_profile_image: {
                if (checkPermission())
                    showPictureDialog();
                break;
            }
            case R.id.sign_up_fragment_profile_terms_cookie: {
                Intent intent=new  Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("web",Constants.TERMS_AND_COOKİE);
                getActivity().startActivity(intent);
                break;
            }
            case R.id.sign_up_fragment_sign_up_button: {
                if (checkValidation()) {
                    if (mImageUrl == null) {
                      InternetControl.getInstance().showSnack(mSignUpButton);
                        createAccountWithoutImage();

                    } else {
                        InternetControl.getInstance().showSnack(mSignUpButton);
                        ceateAccountWithImage();
                    }
                }
                break;
            }
            case R.id.sign_up_fragment_country_edittext: {
                countryDialog();
                break;
            }
        }
    }

    private void ButtonActionActive() {
        mSignUpButton.startAnimation();
        mSignUpButton.setInitialCornerRadius(75);
    }

    private void ButtonActionPasif() {
        mSignUpButton.revertAnimation();
    }

    private boolean checkPermission() {
        return CheckPermissions.getInstance().hasPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE});
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
                mSignUpCountry.setText(mCountryList.get(i).getCountryName());
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
        mSearchEdittext = (EditText) dialogPlus.getHolderView().findViewById(R.id.country_select_fragment_edittext);
        mSearchEdittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    dialogPlus.dismiss();
                    return true;
                }
                return false;
            }
        });
        mSearchEdittext.addTextChangedListener(new TextWatcher() {
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
                    mProgressbar.setVisibility(View.VISIBLE);
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
                            mImageUrl = SaveImageToDirectoryUtils.getOutputMediaFileUri(getActivity().getBaseContext(), bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mSignUpProfileImage.setImageBitmap(bitmap);
                        mProgressbar.setVisibility(View.GONE);
                        Log.e("galery", mImageUrl);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        Log.e("image compressor", throwable.getMessage());
                        mProgressbar.setVisibility(View.GONE);
                    }
                });
    }

    private boolean checkValidation() {
        String email = mSignUpEmail.getText().toString();
        String fullName = mSignUpFullName.getText().toString();
        String password = mSignUpPassword.getText().toString();
        String country = mSignUpCountry.getText().toString();
        return Utils.checkValidation(new String[]{email, fullName, password, country}, mLinearLayout, mShakeAnimation, getActivity(), mRootView);
    }

    private void ceateAccountWithImage() {
        ButtonActionActive();
        UserAccount userClient = UserAccountApiClient.getUserAccount();
        File file = new File(mImageUrl);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody fullName = RequestBody.create(MultipartBody.FORM, mSignUpFullName.getText().toString());
        RequestBody email = RequestBody.create(MultipartBody.FORM, mSignUpEmail.getText().toString());
        RequestBody password = RequestBody.create(MultipartBody.FORM, mSignUpPassword.getText().toString());
        RequestBody country = RequestBody.create(MultipartBody.FORM, mSignUpCountry.getText().toString());
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Call<UserPreferencesData> createAccountResponseModelCall = userClient.createAccountWithImage(fullName, email, password, country, body);
        createAccountResponseModelCall.enqueue(new Callback<UserPreferencesData>() {
            @Override
            public void onResponse(Call<UserPreferencesData> call, Response<UserPreferencesData> response) {

                if (response.isSuccessful()) {
                    Utils.sharedPreferences = mSharedPreferences;
                    Utils.setSharedPreferences("token", response.body().getToken());
                    Utils.setSharedPreferences("fullName", response.body().getFullName());
                    Utils.setSharedPreferences("imageUrl", response.body().getImageUrl());
                    Utils.setSharedPreferences("email", response.body().getEmail());
                    Utils.setSharedPreferences("country", response.body().getCountry());
                    Utils.setSharedPreferences("active", response.body().getActive() + "");
                    ButtonActionPasif();
                    SetLoginPages.getInstance().constructor(getActivity(), 4);
                } else{
                    if (response.code()==400){
                        if (response.errorBody()!=null){
                            try {
                                ErrorHandler.getInstance(getContext()).showEror(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                    }
                    ButtonActionPasif();
                }
            }

            @Override
            public void onFailure(Call<UserPreferencesData> call, Throwable t) {
                if (t instanceof IOException){
                    if (t instanceof java.net.ConnectException){
                        ErrorHandler.getInstance(getContext()).showInfo(300);
                    }
                }else{
                    ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                }
                ButtonActionPasif();

            }
        });
    }

    private void createAccountWithoutImage() {
        UserAccount userAccount = UserAccountApiClient.getUserAccount();
        SignUpRequestModel signUpRequestModel = new SignUpRequestModel(
                mSignUpFullName.getText().toString(),
                mSignUpEmail.getText().toString(),
                mSignUpPassword.getText().toString(),
                mSignUpCountry.getText().toString()
        );
        mResponseObservableWithoutImage = userAccount.createAccountWithoutImage(signUpRequestModel);
        mResponseObservableWithoutImage.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserPreferencesData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        ButtonActionActive();
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<UserPreferencesData> value) {
                        if (value.isSuccessful()) {
                            Utils.sharedPreferences = mSharedPreferences;
                            Utils.setSharedPreferences("token", value.body().getToken());
                            Utils.setSharedPreferences("fullName", value.body().getFullName());
                            Utils.setSharedPreferences("imageUrl", value.body().getImageUrl());
                            Utils.setSharedPreferences("email", value.body().getEmail());
                            Utils.setSharedPreferences("country", value.body().getCountry());
                            Utils.setSharedPreferences("active", value.body().getActive() + "");
                            ButtonActionPasif();
                            SetLoginPages.getInstance().constructor(getActivity(), 4);
                        } else{
                            if (value.code()==400){
                                if (value.errorBody()!=null){
                                    try {
                                        ErrorHandler.getInstance(getContext()).showEror(value.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else{
                                ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                            }
                            ButtonActionPasif();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException){
                            if (e instanceof java.net.ConnectException){
                                ErrorHandler.getInstance(getContext()).showInfo(300);
                            }
                        }else{
                            ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                        }
                        ButtonActionPasif();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
        } else {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 500);
        }
    }


    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

}
