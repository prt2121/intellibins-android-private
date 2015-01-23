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

import com.pt2121.envi.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by prt2121 on 11/25/14.
 */
public class OnboardingFragment extends Fragment {

    public static final String ARG_POSITION = "position";

    public static OnboardingFragment newInstance(int position) {
        OnboardingFragment f = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_onboarding, container, false);
        final int position = getArguments().getInt(ARG_POSITION);
        ((TextView) rootView.findViewById(R.id.onboardingTextView)).setText(
                getOnboardingText(position));
        rootView.findViewById(R.id.content).setBackgroundResource(getBackgroundId(position));
        return rootView;
    }

    private String getOnboardingText(int position) {
        switch (position) {
            case 0:
                return "Find the nearest recycling bins within walking distance.";
            case 1:
                return "Learn about which stores take stuff back.";
            case 2:
                return "Start making a difference one can at a time.";
            default:
                return "";
        }
    }

    private int getBackgroundId(int position) {
        switch (position) {
            case 0:
                return R.drawable.onboarding_1;
            case 1:
                return R.drawable.onboarding_2;
            case 2:
                return R.drawable.onboarding_3;
            default:
                return R.drawable.onboarding_1;
        }
    }

}
