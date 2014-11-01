package ru.phystech.java2.asaitgalin.db.shell.impl.commands;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.db.shell.impl.AbstractDbCommand;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTable;

import java.io.IOException;

@Service
public class UseCommand extends AbstractDbCommand {
    @Override
    public String getName() {
        return "use";
    }

    @Override
    public void execute(String[] args) throws IOException {
        if (state.table != null) {
            int changes = state.table.getChangesCount();
            if (changes != 0) {
                System.out.println(changes + " unsaved changes");
            } else {
                changeTable(args[1]);
            }
        } else {
            changeTable(args[1]);
        }
    }

    @Override
    public int getArgsCount() {
        return 1;
    }

    private void changeTable(String name) {
        DatabaseTable table = state.tableProvider.getTable(name);
        if (table != null) {
            state.table = table;
            System.out.println("using " + name);
        } else {
            System.out.println(name + " not exists");
        }
    }
}
