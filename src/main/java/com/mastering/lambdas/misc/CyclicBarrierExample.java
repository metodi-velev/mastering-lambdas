package com.mastering.lambdas.misc;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * A CyclicBarrier is a synchronizer that allows a set of threads to wait for each other to reach a common execution point, also called a barrier.
 *
 * CyclicBarriers are used in programs in which we have a fixed number of threads that must wait for each other to reach a common point before continuing execution.
 */
public class CyclicBarrierExample implements Runnable
{
    public static CyclicBarrier newBarrier = new CyclicBarrier(3);

    public static void main(String[] args)
    {
        // parent thread
        CyclicBarrierExample test = new CyclicBarrierExample();

        Thread t1 = new Thread(test);
        t1.start();
    }
    public void run()
    {
        System.out.println("Number of parties required to trip the barrier = "+
                newBarrier.getParties());
        System.out.println("Sum of product and sum = " + (Computation1.product +
                Computation2.sum));

        // objects on which the child thread has to run
        Computation1 comp1 = new Computation1();
        Computation2 comp2 = new Computation2();

        // creation of child thread
        Thread t1 = new Thread(comp1);
        Thread t2 = new Thread(comp2);

        // number of parties waiting at the barrier
        System.out.println("Number of parties waiting at the barrier "+
                "at this point = " + CyclicBarrierExample.newBarrier.getNumberWaiting());

        // moving child thread to runnable state
        t1.start();
        t2.start();

        try
        {
            CyclicBarrierExample.newBarrier.await();

            // number of parties waiting at the barrier
            System.out.println("Number of parties waiting at the barrier "+
                    "at this point = " + CyclicBarrierExample.newBarrier.getNumberWaiting());
        }
        catch (InterruptedException | BrokenBarrierException e)
        {
            e.printStackTrace();
        }

        // barrier breaks as the number of thread waiting for the barrier
        // at this point = 3
        System.out.println("Sum of product and sum = " + (Computation1.product +
                Computation2.sum));

        // Resetting the newBarrier
        newBarrier.reset();
        System.out.println("Barrier reset successful");
    }
}

class Computation1 implements Runnable
{
    public static int product = 0;
    public void run()
    {
        product = 2 * 3;
        try
        {
            CyclicBarrierExample.newBarrier.await();

            // number of parties waiting at the barrier
            System.out.println("Number of parties waiting at the barrier "+
                    "at this point = " + CyclicBarrierExample.newBarrier.getNumberWaiting());
        }
        catch (InterruptedException | BrokenBarrierException e)
        {
            e.printStackTrace();
        }
    }
}

class Computation2 implements Runnable
{
    public static int sum = 0;
    public void run()
    {
        // check if newBarrier is broken or not
        System.out.println("Is the barrier broken? - " + CyclicBarrierExample.newBarrier.isBroken());
        sum = 10 + 20;
        try
        {
            CyclicBarrierExample.newBarrier.await();

            // number of parties waiting at the barrier
            System.out.println("Number of parties waiting at the barrier "+
                    "at this point = " + CyclicBarrierExample.newBarrier.getNumberWaiting());
        }
        catch (InterruptedException | BrokenBarrierException e)
        {
            e.printStackTrace();
        }
    }
}