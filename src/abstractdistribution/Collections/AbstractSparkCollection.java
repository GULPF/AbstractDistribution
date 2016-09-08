package abstractdistribution.Collections;

import abstractdistribution.Functions.Consumer;
import abstractdistribution.Functions.Function;
import org.apache.spark.api.java.JavaRDDLike;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.Iterator;
import java.util.List;

/**
 * @param <T> The type the collection contains
 * @param <SparkClass> Due to how JavaRDDLike is implemented, we need to know which
 *                    spark class the extender is implementing.
 */
abstract class AbstractSparkCollection<T,
        SparkClass extends JavaRDDLike<T, SparkClass>>
            implements ICollection<T> {

    private static StorageLevel _storageLvl = StorageLevel.MEMORY_ONLY();

    protected static StorageLevel getStorageLevel() {
        return _storageLvl;
    }

    protected abstract JavaRDDLike<T, SparkClass> getRDD();

    public static void setStorageLevel(StorageLevel newLvl) {
        _storageLvl = newLvl;
    }

    @Override
    public boolean isEmpty() {
        return getRDD().isEmpty();
    }

    @Override
    public long count() {
        return getRDD().count();
    }

    @Override
    public T first() {
        return getRDD().first();
    }

    @Override
    public List<T> collect() {
        return getRDD().collect();
    }

    @Override
    public List<T> take(int num) {
        return getRDD().take(num);
    }

    @Override
    public void forEach(Consumer<T> looper) {
        getRDD().foreach(looper::apply);
    }

    @Override
    public <K2, V2> IMap<K2, V2> mapToPair(Function<T, Tuple2<K2, V2>> mapper) {
        return new SparkMap<>(getRDD().mapToPair(mapper::apply));
    }

    @Override
    public <K2, V2> IMap<K2, V2> flatMapToPair(Function<T, Iterable<Tuple2<K2, V2>>> mapper) {
        return new SparkMap<>(getRDD().flatMapToPair(mapper::apply));
    }


    @Override
    public <A> IList<A> map(Function<T, A> mapper) {
        return new SparkList<>(getRDD().map(mapper::apply));
    }

    @Override
    public <V2> IList<V2> flatMap(Function<T, Iterable<V2>> mapper) {
        return new SparkList<>(getRDD().flatMap(mapper::apply));
    }

    @Override
    public void forEachPartition(Consumer<Iterator<T>> looper) {
        getRDD().foreachPartition(looper::apply);
    }
}
