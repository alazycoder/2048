package com.bjtu.zero.a2048.ui;

import android.content.Context;
import android.widget.Button;

import java.io.Serializable;

/**
 * 开挂按钮。最后没有使用。
 *
 * @author Infinity
 */

public class AutoButton extends Button implements Serializable {

    private static final String text = "Auto";
    private final int ops;

    public AutoButton(Context context, int times) {
        this(context, times, null);
    }

    public AutoButton(Context context, int times, OnClickListener listener) {
        super(context);
        ops = times;
        setOnClickListener(listener);
    }
}
