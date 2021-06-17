package com.forbit.sultanr.ui.report.chart;



import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.Span;

import java.util.List;

public interface ChartContract {

    interface Presenter{
        void processLocations(List<Location> locationList);
    }

    interface View{
        void updateChart(List<Span> spanList);
    }
}
