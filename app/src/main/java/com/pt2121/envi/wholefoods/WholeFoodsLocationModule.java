package com.pt2121.envi.wholefoods;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pt2121 on 1/27/15.
 */
@Module
public class WholeFoodsLocationModule {

    @Provides
    @Singleton
    public IFindWholeFoods provideWholeFoodsLocation() {
        return new WholeFoodsLocation();
    }
}
