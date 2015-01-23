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

package com.pt2121.envi.binlocation;

import com.google.gson.GsonBuilder;

import com.pt2121.envi.R;
import com.pt2121.envi.model.Loc;
import com.pt2121.envi.model.LocType;
import com.pt2121.envi.model.nyc.BinData;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.InputStream;
import java.util.List;

import rx.Observable;
import rx.observables.StringObservable;

/**
 * Created by prt2121 on 9/27/14.
 */
public class NycBinLocation implements IFindBin {

    private static final String TAG = NycBinLocation.class.getSimpleName();

    private Application mApp;

    public NycBinLocation(Application app) {
        mApp = app;
    }

    private Observable<BinData> parseJson(String jsonText) {
        return Observable.just(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .fromJson(jsonText, BinData.class));
    }

    private Observable<String> getJsonText(Context context) {
        try {
            Resources res = context.getResources();
            InputStream inputStream = res.openRawResource(R.raw.json);
            return StringObservable.stringConcat(
                    StringObservable.from(inputStream)
                            .map(String::new));
        } catch (Exception e) {
            return Observable.empty();
        }
    }

    private Observable<Loc> makeBins(final BinData binData) {
        return Observable.create(subscriber -> {
            List<List<String>> lists = binData.getData();
            for (List<String> strings : lists) {
                try {
                    int len = strings.size();
                    if (strings.contains("null") || strings.contains(null)) {
                        continue;
                    }
                    Loc loc = new Loc.Builder(strings.get(len - 4))
                            .address(strings.get(len - 3))
                            .latitude(Double.parseDouble(strings.get(len - 2)))
                            .longitude(Double.parseDouble(strings.get(len - 1)))
                            .type(LocType.BIN)
                            .build();
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(loc);
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                    subscriber.onError(ex);
                }
            }
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<Loc> getLocs() {
        return getJsonText(mApp.getApplicationContext())
                .flatMap(this::parseJson).flatMap(this::makeBins);
    }

}