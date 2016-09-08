package abstractdistribution.Accumulator;

public class DistributedAccumulator<T> implements IAccumulator<T> {

    private org.apache.spark.Accumulator<T> _sparkAccum;

    public DistributedAccumulator(org.apache.spark.Accumulator<T> sparkAccum) {
        _sparkAccum = sparkAccum;
    }

    @Override
    public void add(T t) {
        _sparkAccum.add(t);
    }

    @Override
    public T get() {
        return _sparkAccum.value();
    }
}
