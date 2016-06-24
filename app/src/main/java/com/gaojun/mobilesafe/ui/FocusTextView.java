package com.gaojun.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/6/25.
 */
public class FocusTextView extends TextView {
    public FocusTextView(Context context) {
        super(context);
    }

    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    @ViewDebug.ExportedProperty(category = "focus")
    public boolean isFocused(){
        return true;
    }
}
