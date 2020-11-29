package top.product;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Group)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class Test {

    private TopNCollector collector;
    private ThreadLocalRandom random;

    @Setup
    public void up() {
        collector = new TopNCollector(5);
        random = ThreadLocalRandom.current();
    }

    @Benchmark
    @Group("g")
    @GroupThreads(5)
    public void inc() {
        collector.onEvent(new TopNCollector.Purchase(System.currentTimeMillis(), random.nextInt()));
    }

    @Benchmark
    @Group("g")
    @GroupThreads()
    public List<Map.Entry<Integer, Long>> get() {
        return collector.topNProducts(System.currentTimeMillis(), 5);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Test.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
