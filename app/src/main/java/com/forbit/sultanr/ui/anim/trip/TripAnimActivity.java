package com.forbit.sultanr.ui.anim.trip;




import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.ui.anim.base.BaseAnimActivity;
import com.forbit.sultanr.utils.Constant;

import java.util.List;

public class TripAnimActivity extends BaseAnimActivity {
    @Override
    public void initData() {
        locationList = (List<Location>) getIntent().getSerializableExtra(Constant.LOCATION_LIST);
        vType = getIntent().getIntExtra(Constant.V_TYPE,1);
    }
}
