package abstractdistribution.Provider;

import abstractdistribution.Accumulator.IAccumulator;
import abstractdistribution.Collections.IList;
import abstractdistribution.Collections.IMap;

public interface IApiProvider {
    public IAccumulator<Integer> accumulator(Integer initialValue);
    public IMap<String, String> wholeTextFiles(String path);
    public IList<String> textFile(String path);
}
