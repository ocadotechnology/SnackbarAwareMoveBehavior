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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.andrewlord1990.snackbarbuilder.SnackbarBuilder;
import com.ocadotechnology.snackbarawaremovebehavior.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SampleActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        new SnackbarBuilder(this)
                .message(R.string.snackbar_text)
                .actionText(R.string.snackbar_action_text)
                .build()
                .show();
    }

}
