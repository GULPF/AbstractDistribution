package abstractdistribution.Collections;

import abstractdistribution.Functions.Function;

public interface IList<T> extends ICollection<T> {
    public IList<T> filter(Function<T, Boolean> predicate);

    public IList<T> terminate();
}