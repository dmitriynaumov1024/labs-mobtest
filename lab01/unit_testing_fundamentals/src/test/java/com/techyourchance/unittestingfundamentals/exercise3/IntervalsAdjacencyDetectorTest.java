package com.techyourchance.unittestingfundamentals.exercise3;
import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.*;
import static org.hamcrest.CoreMatchers.is;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector sut;

    void assertAdjacency (Interval a, Interval b, boolean expected) {
        Assert.assertThat(this.sut.isAdjacent(a, b), is(expected));
        Assert.assertThat(this.sut.isAdjacent(b, a), is(expected));
    }

    @Before
    public void setup () {
        this.sut = new IntervalsAdjacencyDetector();
    }

    @Test
    public void onABwithBC () {
        assertAdjacency(new Interval(1, 2), new Interval(2, 5), true);
    }

    @Test
    public void onACwithBC () {
        assertAdjacency(new Interval(0, 9), new Interval(4, 9), false);
    }

    @Test
    public void onABwithCD () {
        assertAdjacency(new Interval(4, 6), new Interval(7, 9), false);
    }

    @Test
    public void onABwithAB () {
        assertAdjacency(new Interval(4, 7), new Interval(4, 7), false);
    }
}