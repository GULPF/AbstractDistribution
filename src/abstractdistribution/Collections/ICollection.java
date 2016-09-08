package abstractdistribution.Collections;

import abstractdistribution.Functions.Consumer;
import abstractdistribution.Functions.Function;
import scala.Tuple2;

import java.util.Iterator;
import java.util.List;

interface ICollection<T> {
    public void forEach(Consumer<T> looper);
    public List<T> collect();
    public long count();
    public T first();
    public List<T> take(int num);
    public boolean isEmpty();

    public <K2, V2> IMap<K2, V2> mapToPair(Function<T, Tuple2<K2, V2>> mapper);
    public <K2, V2> IMap<K2, V2> flatMapToPair(Function<T, Iterable<Tuple2<K2, V2>>> mapper);
    public <A> IList<A> map(Function<T, A> mapper);
    public <V2> IList<V2> flatMap(Function<T, Iterable<V2>> mapper);

    public void forEachPartition(Consumer<Iterator<T>> looper);
}
