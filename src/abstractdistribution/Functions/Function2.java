package abstractdistribution.Functions;

import java.io.Serializable;

public interface Function2<A1, A2, R> extends Serializable {
    public R apply(A1 a1, A2 a2);
}
