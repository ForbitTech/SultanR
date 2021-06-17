package com.forbit.sultanr.ui.report.table;



import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.Span;

import java.util.List;

public interface TableContract {

    interface Presenter{
        void processLocations(List<Location> locationList);
    }

    interface View{
        void clearPreviousRecord();
        void updateSpan(Span span);
    }
}
