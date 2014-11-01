package ru.phystech.java2.asaitgalin.db.shell.impl.commands;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.db.shell.impl.AbstractDbCommand;

import java.io.IOException;

@Service
public class DropCommand extends AbstractDbCommand {
    @Override
    public String getName() {
        return "drop";
    }

    @Override
    public void execute(String[] args) throws IOException {
        try {
            state.tableProvider.removeTable(args[1]);
            if (state.table != null && args[1].equals(state.table.getName())) {
                state.table = null;
            }
            System.out.println("dropped");
        } catch (IllegalStateException e) {
            System.out.println(args[1] + " not exists");
        }
    }

    @Override
    public int getArgsCount() {
        return 1;
    }
}
