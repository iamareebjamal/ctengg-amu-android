package amu.areeb.zhcet;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.content.Context;
import android.util.AttributeSet;

public class FABBehaviour extends FloatingActionButton.Behavior{

	//private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    //private boolean mIsAnimatingOut = false;
	
	public FABBehaviour(Context context, AttributeSet attrs) {
        super();
    }
	
	@Override
	public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child, final View directTargetChild, final View target, final int nestedScrollAxes) {
		   // Ensure we react to vertical scrolling
		   return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL||super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
	}
	
	@Override
	public void onNestedScroll(CoordinatorLayout coordinatorLayout , FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
		if (dyConsumed > 0 && child.getVisibility () == View.VISIBLE) {
			child.hide();
			//animateOut(child);
		} else if(dyConsumed < 0 && child.getVisibility()!= View.VISIBLE) {
			child.show();
			//animateIn(child);
		}
	}
	
	// Same animation that FloatingActionButton.Behavior uses to hide the FAB when the AppBarLayout exits
    /*private void animateOut(final FloatingActionButton button) {
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(button).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(INTERPOLATOR).withLayer()
				.setListener(new ViewPropertyAnimatorListener() {
					public void onAnimationStart(View view) {
						FABBehaviour.this.mIsAnimatingOut = true;
					}

					public void onAnimationCancel(View view) {
						FABBehaviour.this.mIsAnimatingOut = false;
					}

					public void onAnimationEnd(View view) {
						FABBehaviour.this.mIsAnimatingOut = false;
						view.setVisibility(View.GONE);
					}
				}).start();
        } else {
            Animation anim = AnimationUtils.loadAnimation(button.getContext(), R.anim.fab_out);
            anim.setInterpolator(INTERPOLATOR);
            anim.setDuration(200L);
            anim.setAnimationListener(new Animation.AnimationListener() {
					public void onAnimationStart(Animation animation) {
						FABBehaviour.this.mIsAnimatingOut = true;
					}

					public void onAnimationEnd(Animation animation) {
						FABBehaviour.this.mIsAnimatingOut = false;
						button.setVisibility(View.GONE);
					}

					@Override
					public void onAnimationRepeat(final Animation animation) {
					}
				});
            button.startAnimation(anim);
        }
    }

    // Same animation that FloatingActionButton.Behavior uses to show the FAB when the AppBarLayout enters
    private void animateIn(FloatingActionButton button) {
        button.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(button).scaleX(1.0F).scaleY(1.0F).alpha(1.0F)
				.setInterpolator(INTERPOLATOR).withLayer().setListener(null)
				.start();
        } else {
            Animation anim = AnimationUtils.loadAnimation(button.getContext(), R.anim.fab_in);
            anim.setDuration(200L);
            anim.setInterpolator(INTERPOLATOR);
            button.startAnimation(anim);
        }
    }*/
}
