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

    // Constructor
    public Exercise(String text, String answer, String weight) {
        mText = text;
        mAnswer = answer;
        mWeight = weight;
    }

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
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty.");
        }
        mText = text;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("Answer cannot be null or empty.");
        }
        mAnswer = answer;
    }

    public String getWeight() {
        return mWeight;
    }

    public void setWeight(String weight) {
    try {
        float weightValue = Float.parseFloat(weight);
        if (weightValue < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }
        this.mWeight = weight;
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid weight format.");
    }
}

    public long getSubjectId() {
        return mSubjectId;
    }

    public void setSubjectId(long subjectId) {
        mSubjectId = subjectId;
    }
}
