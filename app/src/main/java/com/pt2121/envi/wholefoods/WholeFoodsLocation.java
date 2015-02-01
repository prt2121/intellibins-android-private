package com.pt2121.envi.wholefoods;

import com.pt2121.envi.model.Loc;
import com.pt2121.envi.model.LocType;

import rx.Observable;

/**
 * Created by pt2121 on 1/27/15.
 */
public class WholeFoodsLocation implements IFindWholeFoods {

    public static final String NAME = "Whole Foods";

    @Override
    public Observable<Loc> getLocs() {
        Loc[] locs = {
                new Loc(NAME, "270 Greenwich Street New York, NY 10007",
                        40.71553, -74.01147,
                        LocType.WHOLE_FOODS),
                new Loc(NAME, "95 East Houston St., New York, NY 10002",
                        40.72381, -73.99235,
                        LocType.WHOLE_FOODS),
                new Loc(NAME, "4 Union Square South, New York, NY 10003",
                        40.73474, -73.99138,
                        LocType.WHOLE_FOODS),
                new Loc(NAME, "250 7th Ave., New York, NY 10001",
                        40.74508, -73.99535,
                        LocType.WHOLE_FOODS),
                new Loc(NAME, "808 Columbus Avenue",
                        40.7936629, -73.9687856,
                        LocType.WHOLE_FOODS),
                new Loc(NAME, "226 East 57th Street Between 2nd & 3rd Avenue, New York, NY 10022",
                        40.75864, -73.96871,
                        LocType.WHOLE_FOODS),
                new Loc(NAME,
                        "10 Columbus Circle Lower Concourse Level of the Time Warner Center, New York, NY 10019",
                        40.7687, -73.99182,
                        LocType.WHOLE_FOODS),
                new Loc(NAME, "214 3rd Street, Brooklyn, NY 11215",
                        40.67533, -73.98856,
                        LocType.WHOLE_FOODS)
        };
        return Observable.from(locs);
    }
}
