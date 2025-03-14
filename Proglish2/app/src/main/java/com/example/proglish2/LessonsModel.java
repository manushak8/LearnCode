package com.example.proglish2;

import android.os.Parcel;
import android.os.Parcelable;

public class LessonsModel implements Parcelable {
    private String lessonID;
    private String lessonName;
    private String lessonDescription;

    LessonsModel(){

    }

    public LessonsModel(String lessonID, String lessonName, String lessonDescription) {
        this.lessonID = lessonID;
        this.lessonName = lessonName;
        this.lessonDescription = lessonDescription;
    }

    protected LessonsModel(Parcel in) {
        lessonID = in.readString();
        lessonName = in.readString();
        lessonDescription = in.readString();
    }

    public String getLessonID() {
        return lessonID;
    }
    public String getLessonName() {
        return lessonName;
    }

    public String getLessonDescription() {
        return lessonDescription;
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
        parcel.writeString(lessonDescription);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
