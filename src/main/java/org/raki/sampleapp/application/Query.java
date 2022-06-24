package org.raki.sampleapp.application;

public interface Query<T, U> {
    U ask(T query);
}
