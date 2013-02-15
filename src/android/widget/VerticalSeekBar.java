/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
public class VerticalSeekBar extends SeekBar {

	public VerticalSeekBar(Context context) {
		super(context);
	}

	public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VerticalSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	@Override
	protected void onDraw(Canvas c) {
		c.rotate(-90);
		c.translate(-getHeight(), 0);

		super.onDraw(c);
	}

	private OnSeekBarChangeListener onChangeListener;
	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener onChangeListener){
		this.onChangeListener = onChangeListener;
	}

	private int lastProgress = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			return false;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onChangeListener.onStartTrackingTouch(this);
			setPressed(true);
			setSelected(true);
			break;
		case MotionEvent.ACTION_MOVE:
			super.onTouchEvent(event);
			int progress = getMax() - (int) (getMax() * event.getY() / getHeight());
			
			// Ensure progress stays within boundaries
			if(progress < 0) {progress = 0;}
			if(progress > getMax()) {progress = getMax();}
			setProgress(progress);	// Draw progress
			if(progress != lastProgress) {
				// Only enact listener if the progress has actually changed
				lastProgress = progress;
				onChangeListener.onProgressChanged(this, progress, true);
			}
			
			onSizeChanged(getWidth(), getHeight() , 0, 0);
			setPressed(true);
			setSelected(true);
			break;
		case MotionEvent.ACTION_UP:
			onChangeListener.onStopTrackingTouch(this);
			setPressed(false);
			setSelected(false);
			break;
		case MotionEvent.ACTION_CANCEL:
			super.onTouchEvent(event);
			setPressed(false);
			setSelected(false);
			break;
		}
		return true;
	}

	public synchronized void setProgressAndThumb(int progress) {
		setProgress(progress);
		onSizeChanged(getWidth(), getHeight() , 0, 0);
		if(progress != lastProgress) {
			// Only enact listener if the progress has actually changed
			lastProgress = progress;
			onChangeListener.onProgressChanged(this, progress, true);
		}
	}

	public synchronized void setMaximum(int maximum) {
		setMax(maximum);
	}

	public synchronized int getMaximum() {
		return getMax();
	}
}
