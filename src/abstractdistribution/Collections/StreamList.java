package abstractdistribution.Collections;

import abstractdistribution.Functions.Function;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class StreamList<T> extends AbstractStreamCollection<T> implements IList<T> {

    public StreamList(Stream<T> stream) {
        setStream(stream);
    }

    @Override
    public StreamList<T> filter(Function<T, Boolean> predicate) {
        return new StreamList<>(getStream().filter(predicate::apply));
    }

    @Override
    public IList<T> terminate() {
        terminateStream();
        return this;
    }

    public static StreamList<String> textFile(String path) throws IOException {
        final Path in = Paths.get(path);

        Stream<String> lines = Files.walk(in).flatMap((p) -> {
            File file = p.toFile();
            if (file.isDirectory()) return new ArrayList<String>().stream();

            try {
                return new BufferedReader(new FileReader(file)).lines().parallel();
            } catch (FileNotFoundException e) {
                return new ArrayList<String>().stream().parallel();
            }
        }).parallel();

        return new StreamList<>(lines);
    }
}
