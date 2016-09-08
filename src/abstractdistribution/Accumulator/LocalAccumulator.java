package abstractdistribution.Accumulator;

public class LocalAccumulator implements IAccumulator<Integer> {

    private int _sum;

    LocalAccumulator(int initialValue) {
        _sum = initialValue;
    }

    @Override
    public synchronized void add(Integer integer) {
        _sum += integer;
    }

    @Override
    public synchronized Integer get() {
        return _sum;
    }
}
