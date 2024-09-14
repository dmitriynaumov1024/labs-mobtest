package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

public class IntervalsAdjacencyDetector {

    /**
     * @return true if the intervals are adjacent, but don't overlap
     */
    public boolean isAdjacent(Interval a, Interval b) {
        return a.getStart() == b.getEnd() || a.getEnd() == b.getStart();
    }

    private boolean isSameIntervals(Interval interval1, Interval interval2) {
        return interval1.getStart() == interval2.getStart() && interval1.getEnd() == interval2.getEnd();
    }

}
