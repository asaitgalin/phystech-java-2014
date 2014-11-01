package ru.phystech.java2.asaitgalin.db.shell.impl.commands;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.db.shell.impl.AbstractDbCommand;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRow;

import java.io.IOException;

@Service
public class GetCommand extends AbstractDbCommand {
    @Override
    public String getName() {
        return "get";
    }

    @Override
    public void execute(String[] args) throws IOException {
        if (state.table == null) {
            System.out.println("no table");
        } else {
            DatabaseTableRow value = state.table.get(args[1]);
            if (value != null) {
                System.out.println("found");
                System.out.println(value);
            } else {
                System.out.println("not found");
            }
        }
    }

    @Override
    public int getArgsCount() {
        return 1;
    }
}
