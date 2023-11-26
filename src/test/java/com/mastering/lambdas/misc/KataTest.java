package com.mastering.lambdas.misc;

import static com.mastering.lambdas.misc.Kata.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class KataTest {
    @Test
    public void tests() {
        assertArrayEquals(new int[]{1, 3, 2, 5, 3}, digitize(35231));
        assertArrayEquals(new int[]{0}, digitize(0));
    }
}

class GrassHopperTest {
    @Test
    public void test1() {
        assertEquals(1,
                GrassHopper.summation(1));
    }
    @Test
    public void test2() {
        assertEquals(36,
                GrassHopper.summation(8));
    }
}

class BioTest {
    @Test
    public void testDna() throws Exception {
        Bio b = new Bio();
        assertEquals("UUUU", b.dnaToRna("TTTT"));
    }

    @Test
    public void testDna2() throws Exception {
        Bio b = new Bio();
        assertEquals("GCAU", b.dnaToRna("GCAT"));
    }
}