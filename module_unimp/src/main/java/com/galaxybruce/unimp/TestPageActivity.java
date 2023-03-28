package com.galaxybruce.unimp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TestPageActivity extends Activity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        FrameLayout rootView = new FrameLayout(this);
        TextView textView = new TextView(this);
        textView.setText("textPage");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        rootView.addView(textView , new FrameLayout.LayoutParams(300, 300));
        setContentView(rootView);
    }
}
