package top.product;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class TopNCollector {

    public static class Purchase {
        public Purchase(long timestamp, int productId) {
            this.timestamp = timestamp;
            this.productId = productId;
        }

        public long timestamp;
        public int productId;
    }

    private final Map<Integer, List<Map.Entry<Integer, Long>>> map = new ConcurrentHashMap<>();
    private final ReentrantLock loc = new ReentrantLock();
    private final AtomicLong lastEventTime = new AtomicLong();
    private final int window;

    public TopNCollector(int n) {
        this.window = n;
    }

    public void onEvent(Purchase event) {
        int currentMinuteOfHour = (int) (event.timestamp / 1000 / 60 % (window + 1));
        long currentMinute = (event.timestamp / 1000 / 60);
        map.compute(currentMinuteOfHour, (k, v) -> {
            if (v == null || currentMinute > lastEventTime.get()) {
                List<Map.Entry<Integer, Long>> list = new ArrayList<>();
                // skip buckets if no events after last event
                for (int i = (int) ((lastEventTime.get() + 1) % (window + 1)); i < currentMinuteOfHour; i++) {
                    map.put(i, new ArrayList<>());
                }
                lastEventTime.set(currentMinute);
                list.add(new AbstractMap.SimpleEntry<>(event.productId, event.timestamp));
                return list;
            } else {
                loc.lock();
                v.add(new AbstractMap.SimpleEntry<>(event.productId, event.timestamp));
                loc.unlock();
                return v;
            }
        });
    }

    // Returns top products received last n minutes
    public List<Map.Entry<Integer, Long>> topNProducts(long currentTime, int top) {
        long currentSecondInMinute = (currentTime / 1000 % 60);
        long currentMinute = (currentTime / 1000 / 60);
        List<Integer> products = new ArrayList<>();
        loc.lock();
        map.forEach((key, value) -> {
            int oldestMinute = (int) ((currentMinute - window) % (window + 1));
            if (oldestMinute == key) {
                //adds products from this partial bucket
                if (value.size() > 0) {
                    for (Map.Entry<Integer, Long> integerLongEntry : value) {
                        if (currentSecondInMinute <= integerLongEntry.getValue() % 60) {
                            products.add(integerLongEntry.getKey());
                        }
                    }
                }
            } else if (oldestMinute < key && key <= ((lastEventTime.get()) % (window + 1))) {
                //adds all products from this bucket
                products.addAll(value.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
            }
        });
        loc.unlock();
        return products.stream().collect(Collectors.groupingBy(p -> p, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(top)
                .collect(Collectors.toList());
    }
}
