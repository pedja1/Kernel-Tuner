package android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by pedja on 23.4.14..
 */
public class AutoScrollingButton extends Button
{
    public AutoScrollingButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public AutoScrollingButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AutoScrollingButton(Context context)
    {
        super(context);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)
    {
        if (focused)
        {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean focused)
    {
        if (focused)
        {
            super.onWindowFocusChanged(focused);
        }
    }

    @Override
    public boolean isFocused()
    {
        return true;
    }
}