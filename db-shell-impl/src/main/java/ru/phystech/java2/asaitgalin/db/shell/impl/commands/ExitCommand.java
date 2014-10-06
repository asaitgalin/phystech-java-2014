package ru.phystech.java2.asaitgalin.db.shell.impl.commands;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.db.shell.impl.AbstractDbCommand;

import java.io.IOException;

@Service
public class ExitCommand extends AbstractDbCommand {
    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public void execute(String[] args) throws IOException {
        System.exit(0);
    }

    @Override
    public int getArgsCount() {
        return 0;
    }
}
