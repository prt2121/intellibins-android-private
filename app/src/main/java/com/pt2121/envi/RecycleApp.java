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

import com.pt2121.envi.binlocation.BinLocationModule;
import com.pt2121.envi.dropofflocation.DropOffLocationModule;
import com.pt2121.envi.userlocation.UserLocationModule;

import android.app.Application;
import android.content.Context;

/**
 * Created by prt2121 on 12/28/14.
 */
public class RecycleApp extends Application {

    private RecycleMachine mRecycleMachine;

    @Override
    public void onCreate() {
        super.onCreate();
        mRecycleMachine = Dagger_RecycleMachine.builder()
                .recycleModule(new RecycleModule(this))
                .binLocationModule(new BinLocationModule())
                .userLocationModule(new UserLocationModule(this))
                .dropOffLocationModule(new DropOffLocationModule())
                .build();
    }

    public static RecycleMachine getRecycleMachine(Context context) {
        return ((RecycleApp) context.getApplicationContext()).mRecycleMachine;
    }
}
