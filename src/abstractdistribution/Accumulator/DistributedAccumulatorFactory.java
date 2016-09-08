package abstractdistribution.Accumulator;

import org.apache.spark.api.java.JavaSparkContext;

public class DistributedAccumulatorFactory implements IAccumulatorFactory {

    private JavaSparkContext _sc;

    public DistributedAccumulatorFactory(JavaSparkContext sc) {
        _sc = sc;
    }

    @Override
    public IAccumulator<Integer> create(Integer initialValue) {
        return new DistributedAccumulator<Integer>(_sc.accumulator(initialValue));
    }
}
