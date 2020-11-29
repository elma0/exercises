package product.top;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import top.product.TopNCollector;

public class CollectorTest {

    private TopNCollector collector;

    @Before
    public void up() {
        collector = new TopNCollector(5);
    }

    @Test
    public void testCollector2() {
        collector.onEvent(new TopNCollector.Purchase(59000, 1));
        collector.onEvent(new TopNCollector.Purchase(119000, 1));
        collector.onEvent(new TopNCollector.Purchase(110000, 1));
        collector.onEvent(new TopNCollector.Purchase(179000, 2));
        collector.onEvent(new TopNCollector.Purchase(239000, 1));
        collector.onEvent(new TopNCollector.Purchase(299000, 3));
        List<Map.Entry<Integer, Long>> result = collector.topNProducts(300000, 10);

        List<Map.Entry<Integer, Long>> expected = new ArrayList<>();
        expected.add(new AbstractMap.SimpleEntry<>(1, 4L));
        expected.add(new AbstractMap.SimpleEntry<>(2, 1L));
        expected.add(new AbstractMap.SimpleEntry<>(3, 1L));
        Assert.assertEquals(expected, result);

        collector.onEvent(new TopNCollector.Purchase(359000, 1));
        collector.onEvent(new TopNCollector.Purchase(419000, 1));
        collector.onEvent(new TopNCollector.Purchase(410000, 1));
        collector.onEvent(new TopNCollector.Purchase(479000, 2));
        collector.onEvent(new TopNCollector.Purchase(539000, 1));
        collector.onEvent(new TopNCollector.Purchase(599000, 3));
        collector.onEvent(new TopNCollector.Purchase(700000, 3));
        result = collector.topNProducts(800000, 10); // top products in last 5 minutes [8.333 , 13.333]
        expected = new ArrayList<>();
        expected.add(new AbstractMap.SimpleEntry<>(3, 2L));
        expected.add(new AbstractMap.SimpleEntry<>(1, 1L));
        Assert.assertEquals(expected, result);
    }
}
