package abstractdistribution.Collections;

import abstractdistribution.Functions.Consumer;
import abstractdistribution.Functions.Function;
import scala.Tuple2;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

abstract class AbstractStreamCollection<T> implements ICollection<T> {

    private Stream<T> _stream;
    private Collection<T> _terminationResult;

    protected Stream<T> getStream() {
        if (_stream == null) {
            return _terminationResult.parallelStream();
        }
        return _stream;
    }

    protected void setStream(Stream<T> stream) {
        _stream = stream;
    }

    /**
     * Before calling a terminating method (an 'action' in sparks terminology),
     * this method needs to be called.
     *
     * Note: with a big stream, this is currently BAD due to how arraylists works.
     */
    protected void terminateStream() {
        // Yes, this is slow. I don't know if there's any way around this.
        // For some terminating methods, `peek()` can be used instead of `forEach()`. But not for all.
        _terminationResult = new ArrayDeque<>();
        _stream.forEachOrdered(_terminationResult::add);
        _stream = null;
    }

    // Spark generally works with iterables and streams generally works with other streams.
    // Since streams are not iterable in java (boooh), the stream collections sometimes need this helper method.
    protected static <T> Stream<T> toStream(Iterable<T> itrable) {
        return StreamSupport.stream(itrable.spliterator(), true);
    }

    @Override
    public void forEach(Consumer<T> looper) {
        getStream().forEach(looper::apply);
    }

    @Override
    public List<T> collect() {
        return getStream().collect(Collectors.toList());
    }

    @Override
    public long count() {
        return getStream().count();
    }

    @Override
    public T first() {
        final Optional<T> first = getStream().findFirst();

        if (first.isPresent()) {
            return first.get();
        } else {
            return null;
        }
    }

    @Override
    public boolean isEmpty() {
        return getStream().findAny().isPresent();
    }

    @Override
    public <K, V> IMap<K, V> mapToPair(Function<T, Tuple2<K, V>> mapper) {
        return new StreamMap<>(getStream().map(mapper::apply));
    }

    @Override
    public <K, V> IMap<K, V> flatMapToPair(Function<T, Iterable<Tuple2<K, V>>> mapper) {
        return new StreamMap<>(getStream().flatMap((tuple) -> toStream(mapper.apply(tuple))));
    }

    @Override
    public <A> IList<A> map(Function<T, A> mapper) {
        return new StreamList<>(getStream().map(mapper::apply));
    }

    @Override
    public <V2> StreamList<V2> flatMap(Function<T, Iterable<V2>> mapper) {
        return new StreamList<>(getStream().flatMap((t) -> toStream(mapper.apply(t))));
    }

    @Override
    public List<T> take(int num) {
        return getStream().limit(num).collect(Collectors.toList());
    }

    @Override
    public void forEachPartition(Consumer<Iterator<T>> looper) {
        // Because StreamCollections are not distributed, there is only one partition.
        looper.apply(getStream().iterator());
    }
}
