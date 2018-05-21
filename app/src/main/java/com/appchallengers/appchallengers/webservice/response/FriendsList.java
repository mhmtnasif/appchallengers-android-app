package com.appchallengers.appchallengers.webservice.response;

/**
 * Created by MHMTNASIF on 1.03.2018.
 */
import android.os.Parcelable;
import android.os.Parcel;
public class FriendsList implements Parcelable {
    private long id;
    private String fullName;
    private String profile_picture;
    private boolean selected;

    protected FriendsList(Parcel in) {
        id = in.readLong();
        fullName = in.readString();
        profile_picture = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<FriendsList> CREATOR = new Creator<FriendsList>() {
        @Override
        public FriendsList createFromParcel(Parcel in) {
            return new FriendsList(in);
        }

        @Override
        public FriendsList[] newArray(int size) {
            return new FriendsList[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(fullName);
        parcel.writeString(profile_picture);
        parcel.writeByte((byte) (selected ? 1 : 0));
    }
}
