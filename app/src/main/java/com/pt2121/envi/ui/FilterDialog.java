package com.pt2121.envi.ui;

import com.pt2121.envi.R;
import com.pt2121.envi.model.LocType;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TableLayout;

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
        FilterDialog f = new FilterDialog();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        TableLayout layout = (TableLayout) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
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
