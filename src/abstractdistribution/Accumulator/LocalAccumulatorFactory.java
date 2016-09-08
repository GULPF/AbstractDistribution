package abstractdistribution.Accumulator;

public class LocalAccumulatorFactory implements IAccumulatorFactory {

    @Override
    public IAccumulator<Integer> create(Integer initialValue) {
        return new LocalAccumulator(initialValue);
    }
}
