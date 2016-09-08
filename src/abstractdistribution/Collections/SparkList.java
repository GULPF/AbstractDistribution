package abstractdistribution.Collections;

import abstractdistribution.Functions.Function;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaRDDLike;

public class SparkList<T> extends AbstractSparkCollection<T, JavaRDD<T>> implements IList<T> {

    private JavaRDD<T> _rdd;

    protected JavaRDDLike<T, JavaRDD<T>> getRDD() {
        return _rdd;
    }

    public SparkList(JavaRDD<T> rdd) {
        _rdd = rdd;
    }

    @Override
    public IList<T> terminate() {
        _rdd.persist(getStorageLevel());
        return this;
    }

    @Override
    public IList<T> filter(Function<T, Boolean> predicate) {
        return new SparkList<>(_rdd.filter(predicate::apply));
    }
}
