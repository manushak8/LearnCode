package com.example.itEnglish;

import android.os.Parcel;
import android.os.Parcelable;

public class LessonsModel implements Parcelable {
    private String lessonID;
    private String lessonName;


    public LessonsModel(String lessonID, String lessonName) {
        this.lessonID = lessonID;
        this.lessonName = lessonName;

    }

    protected LessonsModel(Parcel in) {
        lessonID = in.readString();
        lessonName = in.readString();
    }

    public String getLessonName() {
        return lessonName;
    }


    public static final Creator<LessonsModel> CREATOR = new Creator<LessonsModel>() {
        @Override
        public LessonsModel createFromParcel(Parcel in) {
            return new LessonsModel(in);
        }

        @Override
        public LessonsModel[] newArray(int size) {
            return new LessonsModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(lessonID);
        parcel.writeString(lessonName);


    }

    @Override
    public int describeContents() {
        return 0;
    }
}
