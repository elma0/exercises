import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Test {
    public static void main(String[] args) {
        System.out.println(distributeStock(100, ImmutableMap.of("i1", 12.0, "i2", 10.0)));
        System.out.println(distributeStock(100, ImmutableMap.of()));
        System.out.println(distributeStock(100, ImmutableMap.of("i1", 12.0, "i2", 12.0)));
        System.out.println(distributeStock(100, ImmutableMap.of("i1", 0.000000001, "i2", 1_200_000_000_000.0)));
        System.out.println(distributeStock(100, ImmutableMap.of("i1", 38088.7636081, "i2", 384.7349859)));       
    }

    static Map<String, Long> distributeStock(long numberOfShares, Map<String, Double> moneyByUser) {
        Map<String, Long> result = new HashMap<>();
        double totalMoney = moneyByUser.values().stream().mapToDouble(v -> v).sum();
        for (Map.Entry<String, Double> moneys : moneyByUser.entrySet()) {
            result.put(moneys.getKey(), Math.round(moneys.getValue() * numberOfShares / totalMoney));
        }
        return result;
    }
}
