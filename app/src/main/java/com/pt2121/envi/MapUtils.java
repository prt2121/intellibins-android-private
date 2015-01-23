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

package com.pt2121.envi;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.pt2121.envi.model.Loc;
import com.pt2121.envi.model.LocType;

import android.location.Location;
import android.util.Log;
import android.util.Pair;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by prt2121 on 1/19/15.
 */
public class MapUtils {

    private static final String TAG = MapUtils.class.getSimpleName();

    /**
     * Show the pins on the map.
     *
     * @param pivot       user's location or the center location.
     * @param things      bin locations or things
     * @param map         the map to be showed
     * @param maxLocation the number of things
     * @param hue         the hue of the pin
     * @return Subscription
     */
    public static Subscription showPins(Observable<Location> pivot, Observable<Loc> things,
            GoogleMap map, int maxLocation, int hue) {
        if (map == null) {
            Log.e(TAG, "map is NULL");
            return Subscriptions.empty();
        }
        return Observable.zip(pivot.repeat(), things,
                (location, loc) -> {
                    Location l = new Location(loc.name);
                    l.setLatitude(loc.latitude);
                    l.setLongitude(loc.longitude);
                    return new Pair<>(location.distanceTo(l), loc);
                }).toSortedList((p1, p2) -> p1.first.compareTo(p2.first))
                .flatMap(Observable::from)
                .map(p -> p.second)
                .take(maxLocation)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Loc>() {

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Loc loc) {
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(loc.latitude, loc.longitude))
                                .title(loc.name)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(hue))); //HSL: 175° 100% 34% //175
                    }
                });
    }

    /**
     * Show the pins on the map.
     *
     * @param pivot       user's location or the center location.
     * @param things      bin locations or things
     * @param map         the map to be showed
     * @param maxLocation the number of things
     * @return Subscription
     */
    public static Subscription showPins(Observable<Location> pivot, Observable<Loc> things,
            GoogleMap map, int maxLocation) {
        if (map == null) {
            Log.e(TAG, "map is NULL");
            return Subscriptions.empty();
        }
        return Observable.zip(pivot.repeat(), things,
                (location, loc) -> {
                    Location l = new Location(loc.name);
                    l.setLatitude(loc.latitude);
                    l.setLongitude(loc.longitude);
                    return new Pair<>(location.distanceTo(l), loc);
                }).toSortedList((p1, p2) -> p1.first.compareTo(p2.first))
                .flatMap(Observable::from)
                .map(p -> p.second)
                .take(maxLocation)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Loc>() {

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Loc loc) {

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(loc.latitude, loc.longitude))
                                .title(loc.name)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(
                                                getColor(loc.type))));
                    }
                });
    }

    private static float getColor(int type) {
        switch (type) {
            case LocType.USER:
                return BitmapDescriptorFactory.HUE_BLUE;
            case LocType.BIN:
                return 175f;
            case LocType.DROPOFF:
                return BitmapDescriptorFactory.HUE_YELLOW;
            default:
                return BitmapDescriptorFactory.HUE_GREEN;
        }
    }

}
