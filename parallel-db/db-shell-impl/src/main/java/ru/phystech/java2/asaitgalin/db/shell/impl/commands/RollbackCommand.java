package ru.phystech.java2.asaitgalin.db.shell.impl.commands;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.db.shell.impl.AbstractDbCommand;

import java.io.IOException;

@Service
public class RollbackCommand extends AbstractDbCommand {
    @Override
    public String getName() {
        return "rollback";
    }

    @Override
    public void execute(String[] args) throws IOException {
        if (state.table == null) {
            System.out.println("no table");
        } else {
            System.out.println(state.table.rollback());
        }
    }

    @Override
    public int getArgsCount() {
        return 0;
    }
}
