# SnackbarAwareMoveBehavior

[![Build Status](https://travis-ci.org/ocadotechnology/SnackbarAwareMoveBehavior.svg?branch=master)](https://travis-ci.org/ocadotechnology/SnackbarAwareMoveBehavior)
[![Coverage Status](https://coveralls.io/repos/ocadotechnology/SnackbarAwareMoveBehavior/badge.svg?branch=master&service=github)](https://coveralls.io/github/ocadotechnology/SnackbarAwareMoveBehavior?branch=master)
[![License](https://img.shields.io/badge/license-Apache%202.0-green.svg) ](https://github.com/ocadotechnology/SnackbarAwareMoveBehavior/blob/master/LICENSE)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)
<!--[![Download](https://api.bintray.com/packages/ocadotechnology/maven/snackbar-aware-move-behavior/images/download.svg) ](https://bintray.com/ocadotechnology/maven/snackbar-aware-move-behavior/_latestVersion)-->

Within the Android Design Support library there is a `Snackbar` class, which shows a nice message at the bottom of your app screen. However, you may have another view occupating that area of the screen. This library allows you to apply a behaviour to any view within a `CoordinatorLayout` with the `Snackbar`, so that the view slides upwards out of the way and then returns after the `Snackbar` is dismissed.


## Download

The library is currently in development, however, once it has been released it will be available via Gradle on Bintray (JCenter).

<!--Download via Gradle:-->
<!--```groovy-->
<!--compile 'com.ocadotechnology:snackbarawaremovebehavior:0.1.0'-->
<!--```-->

<!--The library is available on Bintray (JCenter).-->

<!--```groovy-->
<!--repositories {-->
<!--  jcenter()-->
<!--}-->
<!--```-->

## Usage

Simply attach the behavior to any view within a `CoordinatorLayout` either in the layout XML or programmatically.

#### XML

```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom"
    android:padding="@dimen/bottom_bar_margin"
    android:background="@color/bottomBarColor"
    app:layout_behavior="com.ocadotechnology.snackbarawaremovebehavior.SnackbarAwareMoveBehavior">
```

Don't forget the `app` namespace will need to be defined in the first layout within the file.

```xml
<android.support.design.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    ...
```

#### Java

```java
CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
layoutParams.setBehavior(new SnackbarAwareMoveBehavior());
view.requestLayout();
```

## Suggestions

If there is any features that have been missed that you are interested in then please open an issue.

## License

    Copyright 2016 Ocado Innovation Limited

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
