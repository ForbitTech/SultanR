package com.forbit.sultanr.ui.anim.rawAnim;



import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.RBody;
import com.forbit.sultanr.ui.anim.base.BaseAnimActivity;
import com.forbit.sultanr.utils.Constant;
import com.forbit.sultanr.utils.MyUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RawAnim extends BaseAnimActivity implements RawAnimContract.View {
    private RawAnimPresenter mPresenter;
    @Override
    public void initData() {
        this.mPresenter = new RawAnimPresenter(this);
        Date date = (Date) getIntent().getSerializableExtra(Constant.SELECTED_DATE);
        locationList = new ArrayList<>();
        vType = getIntent().getIntExtra(Constant.V_TYPE,1);
        String id = getIntent().getStringExtra(Constant.DEVICE_ID);
        RBody requestBody = new RBody();
        requestBody.setDevice_time(MyUtil.getReqDate(date));
        requestBody.setId(id);
        mPresenter.getLocations(requestBody);
    }

    @Override
    public void updateLocations(List<Location> locationList) {
        this.locationList = locationList;
        animate();
    }
}
