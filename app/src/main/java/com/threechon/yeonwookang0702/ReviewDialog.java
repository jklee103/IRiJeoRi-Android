package com.threechon.yeonwookang0702;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class ReviewDialog extends Dialog {
    EditText editText;
    RatingBar ratingBar;
    Button button;
    String content;
    Float rating;

    public ReviewDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.review_dialog);

        editText = findViewById(R.id.review_write_content);
        ratingBar = findViewById(R.id.review_write_ratingbar);

        button = findViewById(R.id.review_write_complete);

        //String content = savedInstanceState.getString("content");
        //Float rating = savedInstanceState.getFloat("rating");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setContent(editText.getText().toString());
                setRating(ratingBar.getRating());

                dismiss();
            }
        });
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
