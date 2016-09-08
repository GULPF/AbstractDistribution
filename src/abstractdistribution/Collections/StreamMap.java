package abstractdistribution.Collections;

import abstractdistribution.Functions.Function;
import abstractdistribution.Functions.Function2;
import scala.Tuple2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class StreamMap<K, V> extends AbstractStreamCollection<Tuple2<K, V>> implements IMap<K, V> {

    private static final CharsetDecoder _cs = Charset.forName("UTF-8").newDecoder();

    public StreamMap(Stream<Tuple2<K, V>> stream) {
        setStream(stream);
    }

    public IMap<K, V> reduceByKey(V initialValue, Function2<V, V, V> reducer) {
        final HashMap<K, V> map = new HashMap<>();

        getStream().forEach(tuple -> {
            V currentReduced = map.get(tuple._1);
            if (currentReduced != null) {
                V reducedVal = reducer.apply(currentReduced, tuple._2);
                map.put(tuple._1, reducedVal);
            } else {
                map.put(tuple._1, reducer.apply(initialValue, tuple._2));
            }
        });

        final Stream<Tuple2<K, V>> stream = map.entrySet().parallelStream()
                .map((entry) -> new Tuple2<>(entry.getKey(), entry.getValue()));

        return new StreamMap<>(stream);
    }

    @Override
    public IMap<K, V> sortByKey(boolean ascending, Comparator<K> cmp) {
        return new StreamMap<>(getStream().sorted((t1, t2) -> {
            int result = cmp.compare(t1._1, t2._1);
            if (!ascending) result *= -1;
            return result;
        }));
    }

    @Override
    public IMap<K, V> filter(Function<Tuple2<K, V>, Boolean> predicate) {
        return new StreamMap<>(getStream().filter(predicate::apply));
    }

    @Override
    public IMap<K, V> terminate() {
        terminateStream();
        return this;
    }

    public static IMap<String, String> wholeTextFiles(String path) throws IOException {
        return new StreamMap<>(Files.walk(Paths.get(path))
            .filter(fpath -> !fpath.toFile().isDirectory())
            .flatMap((fpath) -> {
                File file = fpath.toFile();
                FileInputStream fis = null;

                try {
                    fis = new FileInputStream(file);
                    byte[] data = new byte[(int) file.length()];
                    fis.read(data);
                    fis.close();

//                    final CharBuffer decode = _cs.decode(ByteBuffer.wrap(data));
                    return listWrap(new Tuple2<>(fpath.toString(), new String(data, "UTF-8"))).stream();
                } catch (CharacterCodingException e) {
                    System.out.println("Warning! File is not valid UTF8: " + fpath);
                    e.printStackTrace();
                    return new ArrayList<Tuple2<String, String>>().stream();
                } catch (IOException e) {
                    System.out.println("Warning! Failed to open file: " + fpath);
                    return new ArrayList<Tuple2<String, String>>().stream();
                }
            }
        ));
    }

    private static <T> List<T> listWrap(T t) {
        List<T> list = new ArrayList<>(1);
        list.add(t);
        return list;
    }
}
