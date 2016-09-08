package abstractdistribution.Functions;

import java.io.Serializable;

public interface Consumer<A> extends Serializable {
    public void apply(A a);
}
