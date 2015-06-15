package org.andengine.input.touch.detector;

import android.content.Context;
import android.view.MotionEvent;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:53:40 - 04.04.2012
 */
public class SurfaceGestureDetectorAdapter extends SurfaceGestureDetector {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SurfaceGestureDetectorAdapter(final Context pContext) {
		super(pContext);
	}

	public SurfaceGestureDetectorAdapter(final Context pContext, final float pSwipeMinDistance) {
		super(pContext, pSwipeMinDistance);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================



	@Override
	protected boolean onSwipe(final MotionEvent pMotionEventStart, final float pVelocityX, final float pVelocityY ) {
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}