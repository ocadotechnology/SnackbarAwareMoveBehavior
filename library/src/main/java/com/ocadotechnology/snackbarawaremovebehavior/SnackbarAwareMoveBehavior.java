/*
 * Copyright (C) 2016 Ocado Innovation Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 *
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.ocadotechnology.snackbarawaremovebehavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class SnackbarAwareMoveBehavior extends CoordinatorLayout.Behavior<View> {

    private static final float FRACTION_OF_HEIGHT_AT_WHICH_TO_ANIMATE = 0.667f;

    ObjectAnimator translationAnimator;
    private float verticalTranslation;
    private View[] viewsToAnimate;

    public SnackbarAwareMoveBehavior() {
        super();
    }

    public SnackbarAwareMoveBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected ViewProvider getAnimatingViewsProvider() {
        return DefaultViewProvider.INSTANCE;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child,
                                          View dependency) {
        return updateTranslationForSnackbar(parent, child);
    }

    private boolean updateTranslationForSnackbar(CoordinatorLayout parent, View view) {
        if (view.getVisibility() != View.VISIBLE) {
            return false;
        }

        float targetVerticalTranslation = getVerticalTranslationForSnackbar(parent, view);
        if (isAtTargetVerticalTranslation(targetVerticalTranslation)) {
            return false;
        }

        translateView(view, targetVerticalTranslation);
        return true;
    }

    private void translateView(View view, float targetVerticalTranslation) {
        cancelAnimatorIfRunning();

        if (viewsToAnimate == null) {
            viewsToAnimate = getAnimatingViewsProvider().getViews(view);
        }

        for (View viewToAnimate : viewsToAnimate) {
            animateView(viewToAnimate, targetVerticalTranslation);
        }
    }

    private void animateView(View view, float targetVerticalTranslation) {
        float currentVerticalTranslation = view.getTranslationY();
        if (shouldAnimateTranslation(view, targetVerticalTranslation,
                currentVerticalTranslation)) {
            updateTranslationWithAnimation(view, targetVerticalTranslation,
                    currentVerticalTranslation);
        } else {
            view.setTranslationY(targetVerticalTranslation);
        }
        verticalTranslation = targetVerticalTranslation;
    }

    private void cancelAnimatorIfRunning() {
        if (translationAnimator != null && translationAnimator.isRunning()) {
            translationAnimator.cancel();
        }
    }

    private void updateTranslationWithAnimation(View view, float targetVerticalTranslation,
                                                float currentVerticalTranslation) {
        if (translationAnimator == null) {
            createAnimator(view, targetVerticalTranslation, currentVerticalTranslation);
        }
        translationAnimator.start();
    }

    private void createAnimator(View view, float targetVerticalTranslation,
                                float currentVerticalTranslation) {
        translationAnimator = ObjectAnimator.ofFloat(view, "translationY",
                currentVerticalTranslation, targetVerticalTranslation);
        translationAnimator.setInterpolator(new FastOutSlowInInterpolator());
    }

    private boolean shouldAnimateTranslation(View view,
                                             float targetVerticalTranslation,
                                             float currentVerticalTranslation) {
        float thresholdAtWhichToAnimate = view.getHeight() * FRACTION_OF_HEIGHT_AT_WHICH_TO_ANIMATE;
        float translationDistance = currentVerticalTranslation - targetVerticalTranslation;
        return Math.abs(translationDistance) > thresholdAtWhichToAnimate;
    }

    private boolean isAtTargetVerticalTranslation(float targetVerticalTranslation) {
        return verticalTranslation == targetVerticalTranslation;
    }

    private float getVerticalTranslationForSnackbar(CoordinatorLayout parent,
                                                    View view) {
        float minOffset = 0;
        List<View> dependencies = parent.getDependencies(view);
        for (View dependency : dependencies) {
            if (dependency instanceof Snackbar.SnackbarLayout
                    && parent.doViewsOverlap(view, dependency)) {
                minOffset = Math.min(minOffset,
                        dependency.getTranslationY() - dependency.getHeight());
            }
        }

        return minOffset;
    }

    public interface ViewProvider {
        View[] getViews(View view);
    }

    private static final class DefaultViewProvider implements ViewProvider {
        private static final DefaultViewProvider INSTANCE = new DefaultViewProvider();

        @Override
        public View[] getViews(View viewGroup) {
            return new View[] {viewGroup};
        }
    }

}

