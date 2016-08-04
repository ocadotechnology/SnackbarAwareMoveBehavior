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
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
public class SnackbarAwareMoveBehaviorTest {

    @Mock
    CoordinatorLayout coordinator;

    @Mock
    Snackbar.SnackbarLayout snackbarLayout;

    @Mock
    AttributeSet attributeSet;

    @Mock
    ObjectAnimator animator;

    private SnackbarAwareMoveBehavior behavior;

    @Before
    @SuppressLint("PrivateResource")
    public void before() {
        MockitoAnnotations.initMocks(this);

        SnackbarAwareMoveBehavior.BEHAVIOR_ENABLED = true;
        RuntimeEnvironment.application.setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        behavior = new SnackbarAwareMoveBehavior();
    }

    @Test
    public void whenCreatedFromContextAndAttributeSet_thenSetupCorrectly() {
        SnackbarAwareMoveBehavior behavior = new SnackbarAwareMoveBehavior(
                RuntimeEnvironment.application, attributeSet);

        assertThat(behavior.translationAnimator).isNull();
    }

    @Test
    public void givenBehaviorDisabled_whenLayoutDependsOn_thenFalse() {
        SnackbarAwareMoveBehavior.BEHAVIOR_ENABLED = false;
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(RuntimeEnvironment.application);
        View child = new View(RuntimeEnvironment.application);
        View dependency = new View(RuntimeEnvironment.application);

        boolean actual = behavior.layoutDependsOn(coordinatorLayout, child, dependency);

        Assertions.assertThat(actual).isFalse();
    }

    @Test
    public void givenBehaviorEnabledAndSnackbarLayout_whenLayoutDependsOn_thenTrue() {
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(RuntimeEnvironment.application);
        View child = new View(RuntimeEnvironment.application);
        Snackbar.SnackbarLayout dependency = new Snackbar.SnackbarLayout(RuntimeEnvironment.application);

        boolean actual = behavior.layoutDependsOn(coordinatorLayout, child, dependency);

        Assertions.assertThat(actual).isTrue();
    }

    @Test
    public void givenBehaviourDisabled_whenOnDependentViewChanged_thenFalse() {
        SnackbarAwareMoveBehavior.BEHAVIOR_ENABLED = false;
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(RuntimeEnvironment.application);
        View child = new View(RuntimeEnvironment.application);
        View dependency = new View(RuntimeEnvironment.application);

        boolean actual = behavior.onDependentViewChanged(coordinatorLayout, child, dependency);

        Assertions.assertThat(actual).isFalse();
    }

    @Test
    public void givenChildViewNotVisible_whenOnDependentViewChanged_thenFalse() {
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(RuntimeEnvironment.application);
        View child = new View(RuntimeEnvironment.application);
        child.setVisibility(View.GONE);
        View dependency = new View(RuntimeEnvironment.application);

        boolean actual = behavior.onDependentViewChanged(coordinatorLayout, child, dependency);

        Assertions.assertThat(actual).isFalse();
    }

    @Test
    public void givenViewDoesNotOverlapSnackbar_whenOnDependentViewChanged_thenFalse() {
        View child = new View(RuntimeEnvironment.application);
        List<View> dependencies = new ArrayList<>();
        dependencies.add(snackbarLayout);
        when(coordinator.getDependencies(child)).thenReturn(dependencies);
        when(coordinator.doViewsOverlap(child, snackbarLayout)).thenReturn(false);

        boolean actual = behavior.onDependentViewChanged(coordinator, child, snackbarLayout);

        Assertions.assertThat(actual).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Test
    public void givenTranslationDistanceLessThanTwoThirdsViewHeight_whenOnDependentViewChanged_thenTranslateView() {
        View child = new View(RuntimeEnvironment.application);
        child.setBottom(200);
        child.setTop(0);
        setupCoordinatorLayout(child);

        boolean actual = behavior.onDependentViewChanged(coordinator, child, snackbarLayout);

        Assertions.assertThat(actual).isTrue();
        assertThat(child).hasTranslationY(-110f);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Test
    public void givenTranslationDistanceMoreThanTwoThirdsViewHeight_whenOnDependentViewChanged_thenAnimationTranslationOfView() {
        View child = new View(RuntimeEnvironment.application);
        child.setBottom(150);
        child.setTop(0);
        setupCoordinatorLayout(child);

        Robolectric.getForegroundThreadScheduler().pause();

        boolean actual = behavior.onDependentViewChanged(coordinator, child, snackbarLayout);

        Assertions.assertThat(actual).isTrue();
        assertThat(behavior.translationAnimator).isStarted();

        Robolectric.getForegroundThreadScheduler().unPause();

        assertThat(child).hasTranslationY(-110f);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Test
    public void givenAlreadyTranslated_whenOnDependentViewChanged_thenFalse() {
        View child = new View(RuntimeEnvironment.application);
        child.setBottom(200);
        child.setTop(0);
        setupCoordinatorLayout(child);
        behavior.onDependentViewChanged(coordinator, child, snackbarLayout);

        boolean actual = behavior.onDependentViewChanged(coordinator, child, snackbarLayout);

        Assertions.assertThat(actual).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Test
    public void givenAnimatorAlreadyStarted_whenOnDependentViewChanged_thenAnimatorCancelled() {
        View child = new View(RuntimeEnvironment.application);
        child.setBottom(100);
        child.setTop(0);
        setupCoordinatorLayout(child);
        Robolectric.getForegroundThreadScheduler().pause();
        behavior.translationAnimator = animator;
        when(animator.isRunning()).thenReturn(true);

        behavior.onDependentViewChanged(coordinator, child, snackbarLayout);

        verify(animator).cancel();
        verify(animator).start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupCoordinatorLayout(View child) {
        List<View> dependencies = new ArrayList<>();
        dependencies.add(snackbarLayout);
        when(coordinator.getDependencies(child)).thenReturn(dependencies);
        when(coordinator.doViewsOverlap(child, snackbarLayout)).thenReturn(true);
        when(snackbarLayout.getTranslationY()).thenReturn(-20f);
        when(snackbarLayout.getHeight()).thenReturn(90);
    }
}

