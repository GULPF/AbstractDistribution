package abstractdistribution.Collections;

import abstractdistribution.Functions.Function;
import abstractdistribution.Functions.Function2;
import scala.Tuple2;

import java.util.Comparator;

public interface IMap<K, V> extends ICollection<Tuple2<K, V>> {
    public IMap<K, V> reduceByKey(V initialValue, Function2<V, V, V> reducer);
    public IMap<K, V> sortByKey(boolean ascending, Comparator<K> cmp);
    public IMap<K, V> filter(Function<Tuple2<K, V>, Boolean> predicate);

    public IMap<K, V> terminate();
}
