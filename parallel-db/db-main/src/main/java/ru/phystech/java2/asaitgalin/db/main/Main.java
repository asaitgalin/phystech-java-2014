package ru.phystech.java2.asaitgalin.db.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.phystech.java2.asaitgalin.db.shell.impl.DatabaseShellRunner;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ContextConfig.class);
        DatabaseShellRunner runner = ctx.getBean(DatabaseShellRunner.class);
        runner.run(System.in, System.out, System.err);
    }
}
