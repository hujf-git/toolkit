package hujf.toolkit.test;

import hujf.toolkit.profiler.time.Stopwatch;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhaoyan.hjf
 * @since 2018-09-13
 */
public class TestStopwatch {

    @Test
    public void testElapse() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread.sleep(1000);
        //前后误差不超过10ms
        long elapsed = stopwatch.elapsedMillis();
        System.out.println("elapsed [" + elapsed + "] ms");
        Assert.assertTrue(elapsed < 1010 && elapsed > 990);
    }

}
