package ru.phystech.java2.asaitgalin.db.shell.impl.commands;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.db.shell.impl.AbstractDbCommand;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRow;

import java.io.IOException;
import java.text.ParseException;

@Service
public class PutCommand extends AbstractDbCommand {
    @Override
    public String getName() {
       return "put";
    }

    @Override
    public void execute(String[] args) throws IOException {
        if (state.table == null) {
            System.out.println("no table");
        } else {
            try {
                DatabaseTableRow newValue = state.tableRowProvider.deserialize(state.table, args[2]);
                DatabaseTableRow prev = state.table.put(args[1], newValue);
                if (prev != null) {
                    System.out.println("overwrite");
                    System.out.println("old " + prev);
                } else {
                    System.out.println("new");
                }
            } catch (ParseException pe) {
                System.err.println("put: wrong input" + pe.getMessage());
            }
        }
    }

    @Override
    public int getArgsCount() {
        return 2;
    }
}
