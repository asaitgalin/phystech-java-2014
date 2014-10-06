import org.junit.Before;
import org.junit.Test;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableProvider;
import ru.phystech.java2.asaitgalin.db.table.impl.TableProviderFactoryImpl;
import ru.phystech.java2.asaitgalin.table.api.TableProviderFactory;

import java.io.IOException;

public class TableProviderFactoryTest {
    private static final String BAD_PATH = "/notexistingdir";
    private TableProviderFactory<DatabaseTableProvider> factory;

    @Before
    public void setUp() throws Exception {
        factory = new TableProviderFactoryImpl();
    }

    @Test(expected = IOException.class)
    public void testCreateProviderUnavailable() throws Exception {
        factory.create(BAD_PATH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNull() throws Exception {
        factory.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithSpaces() throws Exception  {
        factory.create("   ");
    }
}
