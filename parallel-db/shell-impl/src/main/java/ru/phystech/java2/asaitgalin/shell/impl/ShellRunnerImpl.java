package ru.phystech.java2.asaitgalin.shell.impl;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.phystech.java2.asaitgalin.shell.api.Command;
import ru.phystech.java2.asaitgalin.shell.api.ShellRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ShellRunnerImpl<T> implements ShellRunner {
    private static final Logger log = LoggerFactory.getLogger(ShellRunner.class);
    private Map<String, Command<T>> table;

    @Autowired
    public void setCommands(List<Command<T>> commands) {
        table = new HashMap<>();
        for (Command<T> cmd : commands) {
            table.put(cmd.getName(), cmd);
        }
    }

    @Override
    public void run(InputStream in, PrintStream out, PrintStream err) {
        Scanner scanner = new Scanner(in);
        out.print("$ ");
        while (scanner.hasNext()) {
            try {
                executeCommandLine(scanner.nextLine());
            } catch (IOException ioe) {
                err.println(ioe.getMessage());
            }
            out.print("$ ");
        }
    }

    @Override
    public void run(String[] args, PrintStream err) {
        Joiner joiner = Joiner.on(" ").skipNulls();
        String commandLine = joiner.join(args);
        try {
            executeCommandLine(commandLine);
        } catch (IOException ioe) {
            err.println(ioe.getMessage());
            System.exit(1);
        }
    }

    private void executeCommandLine(String cmdLine) throws IOException {
        String[] commands = cmdLine.trim().split("\\s*;\\s*");
        for (String s : commands) {
            log.debug("executing \"" + s + "\"");
            String cmdName = s.split("\\s+", 2)[0];
            Command<T> cmd = table.get(cmdName);
            if (cmd != null) {
                String[] cmdArgs = s.split("\\s+(?![^\\(]*\\))"); // split by spaces except expressions in brackets
                if (cmdArgs == null) {
                    throw new IOException(cmd.getName() + ": error while parsing command");
                }
                if (cmdArgs.length - 1 != cmd.getArgsCount()) {
                    String msg = String.format("%s: wrong argument count. Expected %d args", cmd.getName(),
                            cmd.getArgsCount());
                    throw new IOException(msg);
                }
                cmd.execute(cmdArgs);
            } else {
                throw new IOException(cmdName + ": unrecognized command");
            }
        }
    }
}
