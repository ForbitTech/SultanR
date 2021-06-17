package com.forbit.sultanr.ui.anim.controlAnimation;



import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.RBody;

import java.util.List;

public interface ControlAnimationContract {

    interface Presenter{
        void getLocations(RBody rBody);
    }

    interface View{
        void updateCamera(List<Location> locationList);
    }
}
