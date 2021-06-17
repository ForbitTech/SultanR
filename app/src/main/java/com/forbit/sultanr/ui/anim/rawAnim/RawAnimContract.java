package com.forbit.sultanr.ui.anim.rawAnim;



import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.RBody;

import java.util.List;

public interface RawAnimContract {

    interface Presenter{
        void getLocations(RBody rBody);
    }

    interface View{
        void updateLocations(List<Location> locationList);
    }
}
