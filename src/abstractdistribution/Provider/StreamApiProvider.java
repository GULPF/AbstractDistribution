package abstractdistribution.Provider;

import abstractdistribution.Accumulator.IAccumulator;
import abstractdistribution.Accumulator.IAccumulatorFactory;
import abstractdistribution.Accumulator.LocalAccumulatorFactory;
import abstractdistribution.Collections.IList;
import abstractdistribution.Collections.IMap;
import abstractdistribution.Collections.StreamList;
import abstractdistribution.Collections.StreamMap;

import java.io.IOException;

public class StreamApiProvider implements IApiProvider {

    private final IAccumulatorFactory _afac;

    public StreamApiProvider() {
        _afac = new LocalAccumulatorFactory();
    }

    @Override
    public IAccumulator<Integer> accumulator(Integer initialValue) {
        return _afac.create(initialValue);
    }

    @Override
    public IMap<String, String> wholeTextFiles(String path) {
        try {
            return StreamMap.wholeTextFiles(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IList<String> textFile(String path) {
        try {
            return StreamList.textFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
