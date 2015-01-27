package com.pt2121.envi.wholefoods;

import com.pt2121.envi.model.Loc;

import rx.Observable;

/**
 * Created by pt2121 on 1/27/15.
 */
public interface IFindWholeFoods {

    public Observable<Loc> getLocs();
}
