一. CountDownLatch和CyclicBarrier的比较

        1.CountDownLatch是线程组之间的等待，即一个(或多个)线程等待N个线程完成某件事情之后再执行；
          而CyclicBarrier则是线程组内的等待，即每个线程相互等待，即N个线程都被拦截之后，然后依次执行。

        2.CountDownLatch是减计数方式，而CyclicBarrier是加计数方式。

        3.CountDownLatch计数为0无法重置，而CyclicBarrier计数达到初始值，则可以重置。

        4.CountDownLatch不可以复用，而CyclicBarrier可以复用