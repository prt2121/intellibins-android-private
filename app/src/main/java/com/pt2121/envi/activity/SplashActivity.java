/*
 * Copyright (c) 2015 Prat Tanapaisankit
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *   * Neither the name of The Intern nor the names of its contributors may
 * be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE LISTED COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.pt2121.envi.activity;


import com.pt2121.envi.ISharedPreferencesHelper;
import com.pt2121.envi.R;
import com.pt2121.envi.RecycleApp;
import com.pt2121.envi.RecycleMachine;
import com.pt2121.envi.SharedPreferencesHelperFactory;
import com.pt2121.envi.Utils;
import com.pt2121.envi.userlocation.IUserLocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import javax.inject.Inject;

public class SplashActivity extends Activity {

    private ISharedPreferencesHelper mSharedPreferencesHelper;

    @Inject
    IUserLocation mUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int splashScreenTimeout = 3000;
        mSharedPreferencesHelper = SharedPreferencesHelperFactory.get();
        new Handler().postDelayed(() -> {
            boolean firstRun = mSharedPreferencesHelper.isFirstRun(SplashActivity.this);
            Class<?> clazz = firstRun
                    ? OnboardingActivity.class : MapActivity.class;
            if (firstRun) {
                mSharedPreferencesHelper.setFirstRun(SplashActivity.this, false);
            }
            Intent intent = new Intent(SplashActivity.this, clazz);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }, splashScreenTimeout);

        RecycleMachine machine = RecycleApp.getRecycleMachine(this);
        mUserLocation = machine.locateUser();
        mUserLocation.start();
        mUserLocation.observe()
                .take(1)
                .subscribe(location ->
                        Utils.saveUserLocationToPreference(SplashActivity.this, location));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserLocation.stop();
    }
}
