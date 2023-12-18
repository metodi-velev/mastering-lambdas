package com.mastering.lambdas.misc;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;

@JCStressTest
@State
@Outcome(id = "1, 1", expect = ACCEPTABLE, desc = "Both actors have finished in the same time")
@Outcome(id = "0, 1", expect = ACCEPTABLE, desc = "First Actor has finished before second")
@Outcome(id = "1, 0", expect = ACCEPTABLE, desc = "Second Actor has finished before first")
@Outcome(id = "0, 0", expect = ACCEPTABLE, desc = "Intel can reorder Stores with Load")
public class StoreLoad {
    private int x, y;

    @Actor
    public void actor1(II_Result r) {
        x = 1;         //store
        r.r1 = y;      //load
    }

    @Actor
    public void actor2(II_Result r) {
        y = 1;         //store
        r.r2 = x;      //load
    }
}
