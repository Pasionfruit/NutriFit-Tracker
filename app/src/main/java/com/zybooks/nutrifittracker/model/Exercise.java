package com.zybooks.nutrifittracker.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Workout.class, parentColumns = "id",
        childColumns = "subject_id", onDelete = CASCADE))
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "text")
    private String mText;

    @ColumnInfo(name = "answer")
    private String mAnswer;

    @ColumnInfo(name = "weight")
    private String mWeight;

    @ColumnInfo(name = "subject_id")
    private long mSubjectId;


    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public String getWeight() {
        return mWeight;
    }

    public void setWeight(String weight) {
        mWeight = weight;
    }

    public long getSubjectId() {
        return mSubjectId;
    }

    public void setSubjectId(long subjectId) {
        mSubjectId = subjectId;
    }
}