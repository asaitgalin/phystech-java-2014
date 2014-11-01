package ru.phystech.java2.asaitgalin.shell.api;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public abstract class Command<T> {
    protected T state;

    @Autowired
    public void setState(T state) {
        this.state = state;
    }

    public abstract String getName();
    public abstract void execute(String[] args) throws IOException;
    public abstract int getArgsCount();
}