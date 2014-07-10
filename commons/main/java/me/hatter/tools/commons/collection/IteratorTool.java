package me.hatter.tools.commons.collection;

import java.util.Iterator;

import me.hatter.tools.commons.function.IndexedProcedure;
import me.hatter.tools.commons.function.Procedure;

public class IteratorTool<T> {

    private Iterator<T> iterator;

    public IteratorTool(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public IteratorTool(Iterable<T> iterable) {
        this.iterator = iterable.iterator();
    }

    public void each(Procedure<T> procedure) {
        while (iterator.hasNext()) {
            procedure.apply(iterator.next());
        }
    }

    public void each(IndexedProcedure<T> indexedProcedure) {
        for (int i = 0; iterator.hasNext(); i++) {
            indexedProcedure.apply(iterator.next(), i);
        }
    }
}
