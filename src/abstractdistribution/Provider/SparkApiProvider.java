package abstractdistribution.Provider;

import abstractdistribution.Accumulator.IAccumulator;
import abstractdistribution.Accumulator.IAccumulatorFactory;
import abstractdistribution.Accumulator.DistributedAccumulatorFactory;
import abstractdistribution.Collections.IList;
import abstractdistribution.Collections.IMap;
import abstractdistribution.Collections.SparkList;
import abstractdistribution.Collections.SparkMap;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkApiProvider implements IApiProvider {

    public final IAccumulatorFactory _afac;
    public final JavaSparkContext _sc;

    public SparkApiProvider(JavaSparkContext sc) {
        _sc = sc;
        _afac = new DistributedAccumulatorFactory(sc);
    }

    @Override
    public IAccumulator<Integer> accumulator(Integer initialValue) {
        return _afac.create(initialValue);
    }

    @Override
    public IMap<String, String> wholeTextFiles(String path) {
        return new SparkMap<>(_sc.wholeTextFiles(path));
    }

    @Override
    public IList<String> textFile(String path) {
        return new SparkList<>(_sc.textFile(path));
    }
}
