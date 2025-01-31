package com.example.proglish2;

import android.os.Parcel;
import android.os.Parcelable;

public class LessonsModel implements Parcelable {
    private String lessonNumber;

    public LessonsModel(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    protected LessonsModel(Parcel in) {
        lessonNumber = in.readString();
    }

    public String getLessonNumber() {
        return lessonNumber;
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
        parcel.writeString(lessonNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
