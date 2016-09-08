package abstractdistribution.Functions;

import java.io.Serializable;

public interface Function<A, R> extends Serializable{
    public R apply(A a);
}
