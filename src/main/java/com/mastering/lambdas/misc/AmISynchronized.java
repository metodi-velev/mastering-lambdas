package com.mastering.lambdas.misc;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

@JCStressTest
@State
@Outcome(id = "1", expect = ACCEPTABLE, desc = "Actor2 is executed before Actor1")
@Outcome(id = "2", expect = ACCEPTABLE, desc = "Actor2 is executed after y = 2 and before x = 3 in Actor1")
@Outcome(id = "3", expect = FORBIDDEN, desc = "y = 2 can not be reordered with x = 3 as we have volatile on x (happens before guarantee)")
@Outcome(id = "6", expect = ACCEPTABLE, desc = "Actor2 executed after Actor1")
public class AmISynchronized {
    volatile int x = 1;
    int y = 1;

    @Actor
    public void actor1() {
        y = 2;
        x = 3;
    }

    @Actor
    public void actor2(I_Result r) {
        r.r1 = x * y;          //since expressions in Java are evaluated from left to right and
    }                          //x is volatile, if actor2 reads in x's value,
}                              //then y = 2 is visible to thread actor2 because of happens before guarantee of JMM

