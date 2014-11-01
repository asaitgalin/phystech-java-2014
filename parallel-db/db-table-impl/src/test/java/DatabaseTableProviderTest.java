import org.apache.commons.io.FileUtils;
import org.junit.*;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTable;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableProvider;
import ru.phystech.java2.asaitgalin.db.table.impl.TableProviderFactoryImpl;
import ru.phystech.java2.asaitgalin.table.api.Table;
import ru.phystech.java2.asaitgalin.table.api.TableProviderFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTableProviderTest {
    private static final File DB_TEST_PATH = new File("./testsProvider");

    private TableProviderFactory<DatabaseTableProvider> factory = new TableProviderFactoryImpl();
    private DatabaseTableProvider provider;
    private DatabaseTable table;

    @BeforeClass
    public static void globalSetUp() {
        DB_TEST_PATH.mkdir();
    }

    @AfterClass
    public static void globalTearDown() {
        try {
            FileUtils.deleteDirectory(DB_TEST_PATH);
        } catch (IOException ioe) {
            //
        }
    }

    @Before
    public void setUp() throws Exception {
        provider = factory.create(DB_TEST_PATH.getAbsolutePath());
        List<Class<?>> columns = new ArrayList<>();
        columns.add(Integer.class);
        columns.add(String.class);
        table = provider.createTable("dbTable", columns);
    }

    @After
    public void tearDown() throws Exception {
        provider.removeTable("dbTable");
    }

    @Test
    public void testCreateTable() throws Exception {
        List<Class<?>> list = new ArrayList<>();
        list.add(Integer.class);
        Assert.assertNotNull(provider.createTable("table1", list));
        Assert.assertNull(provider.createTable("table1", list));
        provider.removeTable("table1");
    }

    @Test(expected = RuntimeException.class)
    public void testCreateTableBadSymbols() throws Exception {
        List<Class<?>> list = new ArrayList<>();
        list.add(Integer.class);
        provider.createTable(":123+", list);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTableBadSymbols() throws Exception {
        provider.getTable(":123+");
    }

    @Test
    public void testGetTableInstance() throws Exception {
        List<Class<?>> list = new ArrayList<>();
        list.add(String.class);
        Table table = provider.createTable("newtable", list);
        Assert.assertSame(provider.getTable("newtable"), table);
        Assert.assertSame(provider.getTable("newtable"), provider.getTable("newtable"));
        provider.removeTable("newtable");
    }

    @Test
    public void testGetTable() {
        List<Class<?>> list = new ArrayList<>();
        list.add(Integer.class);
        try {
            provider.createTable("table2", list);
            Assert.assertNotNull(provider.getTable("table2"));
            provider.removeTable("table2");
        } catch (IOException ioe) {
            //
        }

    }

    @Test
    public void testGetNonExistingTable() {
        Assert.assertNull(provider.getTable("unknownTable"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTableWithNull() throws Exception {
        provider.getTable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTableWithNull() throws Exception {
        provider.createTable(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveTableWithNull() throws Exception {
        provider.removeTable(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveNotExistingTable() throws Exception {
        provider.removeTable("notExistingTable");
    }
}
