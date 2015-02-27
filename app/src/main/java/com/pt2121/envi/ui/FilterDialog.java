/*
 * Copyright (c) 2015 Prat Tanapaisankit and Intellibins authors
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name of The Intern nor the names of its contributors may
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

package com.pt2121.envi.ui;

import com.pt2121.envi.R;
import com.pt2121.envi.model.LocType;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by pt2121 on 2/26/15.
 */
public class FilterDialog extends DialogFragment {

    private static final String STATE_FLAG = "filter_flag";

    private int mCurrentFlag = LocType.BIN;

    private Switch mBinSwitch;

    private Switch mDropOffSwitch;

    private Switch mWholeFoodsSwitch;

    private FilterCallbacks mCallbacks;

    static FilterDialog newInstance() {
        return new FilterDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.setTitle("Filters");
        TextView titleTextView = (TextView) dialog.findViewById(android.R.id.title);
        titleTextView.setGravity(Gravity.CENTER);
        TableLayout layout = (TableLayout) inflater.inflate(
                R.layout.fragment_filter, container, false);
        mBinSwitch = (Switch) layout.findViewById(R.id.binSwitch);
        mDropOffSwitch = (Switch) layout.findViewById(R.id.dropOffSwitch);
        mWholeFoodsSwitch = (Switch) layout.findViewById(R.id.wholeFoodSwitch);
        mBinSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> setStateFlag(isChecked, LocType.BIN));
        mDropOffSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> setStateFlag(isChecked, LocType.DROPOFF));
        mWholeFoodsSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> setStateFlag(isChecked, LocType.WHOLE_FOODS));
        setSwitchState(mCurrentFlag);
        return layout;
    }

    private void setSwitchState(int flag) {
        mBinSwitch.setChecked((flag & LocType.BIN) == LocType.BIN);
        mDropOffSwitch.setChecked((flag & LocType.DROPOFF) == LocType.DROPOFF);
        mWholeFoodsSwitch.setChecked((flag & LocType.WHOLE_FOODS) == LocType.WHOLE_FOODS);
        if (mCallbacks != null) {
            mCallbacks.onFlagChanged(mCurrentFlag);
        }
    }

    private void setStateFlag(boolean isChecked, int type) {
        if (isChecked) {
            mCurrentFlag |= type;
        } else {
            mCurrentFlag &= ~type;
        }
        if (mCallbacks != null) {
            mCallbacks.onFlagChanged(mCurrentFlag);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FilterCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_FLAG, mCurrentFlag);
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface FilterCallbacks {

        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onFlagChanged(int flag);
    }
}
