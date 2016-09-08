A proof-of-concept of how Javas stream-api can be combined with Spark
to create an abstraction layer allowing code to choose at runtime whether to
run in a parallelized mode (streams) or a distributed mode (Spark).

Choosing parallelized or distributed
----------------
Running in parallelized mode
```java
    IApiProvider api = new StreamApiProvider();
    new Algorithm().run(api "/path/to/local/file");
```

Running in distributed mode.
```java
    SparkConf conf = new SparkConf().setAppName("Spark Demo");
    JavaSparkContext sc = new JavaSparkContext(conf);
    IApiProvider api = new SparkApiProvider(sc);
    new Algorithm().run(api "hdfs:///path/to/distributed/file");
```
Using the dataset
----------------

Calculating chess win ratios from a dataset of .pgn files (f.ex, https://github.com/rozim/ChessData).

```java
    public class Algorithm implements Serializable {
        private enum Winner {Black, White, Draw, Invalid}

        public void run(IApiProvider api, String path) {
            final IList<String> lines = api.textFile(path);

            lines = lines.filter((line) -> line.startsWith("[Result"));
            
            DistributedMap<Winner, Integer> results = lines.mapToPair((line) -> {
                char khar = line.charAt(11);
                if (khar == '0') return new Tuple2<>(Winner.White, 1);
                if (khar == '1') return new Tuple2<>(Winner.Black, 1);
                if (khar == '2') return new Tuple2<>(Winner.Draw, 1);
                return new Tuple2<>(Winner.Invalid, 1);
            });
            
            results = results.reduceByKey(0, (a, b) -> a + b);
            results.collect().forEach(System.out::println);
        }
    }
```

Or, with accumulators.
```java
    public class Algorithm implements Serializable {
        private enum Winner {Black, White, Draw, Invalid}

        public void run(IApiProvider api, String path) {
            final IList<String> lines = api.textFile(path);
            final Accumulator<Integer> black   = api.accumulator(0);
            final Accumulator<Integer> white   = api.accumulator(0);
            final Accumulator<Integer> draw    = api.accumulator(0);
            final Accumulator<Integer> invalid = api.accumulator(0);

            lines.forEach((line) -> {
                if (!line.startsWith("[Result")) return;

                char khar = line.charAt(11);
                if      (khar == '0') white.add(1);
                else if (khar == '1') black.add(1);
                else if (khar == '2') draw.add(1);
                else                  invalid.add(1);
            });

            System.out.println("black: "   + black.get());
            System.out.println("white: "   + white.get());
            System.out.println("draw: "    + draw.get());
            System.out.println("invalid: " + invalid.get());
        }
    }
```
