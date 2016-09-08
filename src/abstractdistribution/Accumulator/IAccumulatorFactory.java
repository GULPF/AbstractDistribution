package abstractdistribution.Accumulator;

public interface IAccumulatorFactory {
    public IAccumulator<Integer> create(Integer initialValue);
}
