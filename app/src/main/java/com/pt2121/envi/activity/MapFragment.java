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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.pt2121.envi.MapUtils;
import com.pt2121.envi.R;
import com.pt2121.envi.RecycleApp;
import com.pt2121.envi.model.Loc;
import com.pt2121.envi.model.LocType;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.Subscription;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getSimpleName();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Loc mLoc;

    private int mFlag;

    private static final String ARG_LOC = "locations";

    private static final String ARG_FLAG = "flag";

    private OnFragmentInteractionListener mListener;

    private static final float ZOOM = 17f;

    private static final int MAX_LOCATION = Integer.MAX_VALUE;

    private Subscription mSubscription;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameter.
     *
     * @param loc user's location
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance(Loc loc, int flag) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LOC, loc);
        args.putInt(ARG_FLAG, flag);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLoc = getArguments().getParcelable(ARG_LOC);
            mFlag = getArguments().getInt(ARG_FLAG);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpMapIfNeeded(mLoc, mFlag);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded(mLoc, mFlag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void setUpMapIfNeeded(Loc loc, int flag) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(loc, flag);
            }
        }
    }

    private void setUpMap(Loc centerLoc, int flag) {
        LatLng latLng = new LatLng(centerLoc.latitude, centerLoc.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
        mSubscription = updateMarkers(centerLoc, flag);
    }

    public void refreshMarkers(Loc centerLoc, int flag) {
        if (mMap != null) {
            mMap.clear();
            mSubscription = updateMarkers(centerLoc, flag);
        }
    }

    private Subscription updateMarkers(Loc centerLoc, int flag) {
        LatLng latLng = new LatLng(centerLoc.latitude, centerLoc.longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title(centerLoc.name));

        Location mockLocation = new Location(centerLoc.name);
        mockLocation.setLatitude(centerLoc.latitude);
        mockLocation.setLongitude(centerLoc.longitude);
        Observable<Location> mockObservable = Observable.just(mockLocation);

        Observable<Loc> bin = ((flag & LocType.BIN) == LocType.BIN) ?
                RecycleApp.getRecycleMachine(MapFragment.this.getActivity())
                        .findBin()
                        .getLocs() : Observable.<Loc>empty();

        Observable<Loc> dropOff = ((flag & LocType.DROPOFF) == LocType.DROPOFF) ?
                RecycleApp.getRecycleMachine(MapFragment.this.getActivity())
                        .findDropOff()
                        .getLocs() : Observable.<Loc>empty();

        Observable<Loc> wholeFoods = ((flag & LocType.WHOLE_FOODS) == LocType.WHOLE_FOODS) ?
                RecycleApp.getRecycleMachine(MapFragment.this.getActivity())
                        .findWholeFoods()
                        .getLocs() : Observable.<Loc>empty();

        return MapUtils.showPins(getActivity(), mockObservable,
                dropOff.concatWith(bin).concatWith(wholeFoods), mMap, MAX_LOCATION);
    }


}
