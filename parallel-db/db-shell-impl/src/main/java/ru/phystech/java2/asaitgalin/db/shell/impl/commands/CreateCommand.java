package ru.phystech.java2.asaitgalin.db.shell.impl.commands;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.db.shell.impl.AbstractDbCommand;
import ru.phystech.java2.asaitgalin.db.table.impl.DatabaseTableTypes;

import java.io.IOException;

@Service
public class CreateCommand extends AbstractDbCommand {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public void execute(String[] args) throws IOException {
        // args[1] = tablename
        // args[2] = columns (...)
        String columnData = args[2].substring(1, args[2].length() - 1);
        String[] types = columnData.split("\\s");
        if (state.tableProvider.createTable(args[1], DatabaseTableTypes.getColumnTypes(types)) == null) {
            System.out.println(args[1] + " exists");
        } else {
            System.out.println("created");
        }
    }

    @Override
    public int getArgsCount() {
        return 2;
    }
}
