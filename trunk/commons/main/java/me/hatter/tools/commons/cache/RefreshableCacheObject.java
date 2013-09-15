package me.hatter.tools.commons.cache;

import java.util.concurrent.Callable;

public abstract class RefreshableCacheObject<T> implements CacheObject<T> {

    private boolean isSet;
    protected T     object;

    public T getObject() {
        try {
            synchronized (this) {
                if ((!isSet) || shouldRefresh()) {
                    Callable<T> refreshAction = new Callable<T>() {

                        @Override
                        public T call() throws Exception {
                            return refreshObject();
                        }
                    };
                    updateRefreshObject(refreshAction);
                    isSet = true;
                }
            }
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateRefreshObject(Callable<T> refreshAction) throws Exception {
        this.object = refreshAction.call();
    }

    abstract protected boolean shouldRefresh();

    abstract protected T refreshObject() throws Exception;
}
