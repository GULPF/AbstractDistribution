package abstractdistribution.Accumulator;

import java.io.Serializable;

public interface IAccumulator<T> extends Serializable {
    public void add(T t);
    public T get();
}
