package org.raki.sampleapp.application;

public interface Command<T> {
    void dispatch(T command);
}
