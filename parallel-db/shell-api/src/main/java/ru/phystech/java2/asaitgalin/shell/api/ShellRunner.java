package ru.phystech.java2.asaitgalin.shell.api;

import java.io.InputStream;
import java.io.PrintStream;

public interface ShellRunner<T> {
    void run(InputStream in, PrintStream out, PrintStream err);
    void run(String[] args, PrintStream err);
}
