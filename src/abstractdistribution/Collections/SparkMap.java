package abstractdistribution.Collections;

import abstractdistribution.Functions.Function;
import abstractdistribution.Functions.Function2;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDDLike;
import scala.Tuple2;

import java.util.Comparator;

public class SparkMap<K, V> extends AbstractSparkCollection<Tuple2<K, V>, JavaPairRDD<K, V>>
        implements IMap<K, V> {

    private JavaPairRDD<K, V> _rdd;

    protected JavaRDDLike<Tuple2<K, V>, JavaPairRDD<K, V>> getRDD() {
        return _rdd;
    }

    public SparkMap(JavaPairRDD<K, V> rdd) {
        _rdd = rdd;
    }

    @Override
    public IMap<K, V> reduceByKey(V initialValue, Function2<V, V, V> reducer) {
        return new SparkMap<>(_rdd.reduceByKey(reducer::apply));
    }

    @Override
    public IMap<K, V> terminate() {
        _rdd.persist(getStorageLevel());
        return this;
    }

    @Override
    public IMap<K, V> sortByKey(boolean ascending, Comparator<K> cmp) {
        return new SparkMap<>(_rdd.sortByKey(cmp, ascending));
    }

    @Override
    public IMap<K, V> filter(Function<Tuple2<K, V>, Boolean> predicate) {
        return new SparkMap<>(_rdd.filter(predicate::apply));
    }
}
