import org.apache.commons.io.FileUtils;
import org.junit.*;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTable;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableProvider;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRow;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRowProvider;
import ru.phystech.java2.asaitgalin.db.table.impl.DatabaseTableRowProviderImpl;
import ru.phystech.java2.asaitgalin.db.table.impl.TableProviderFactoryImpl;
import ru.phystech.java2.asaitgalin.table.api.TableProviderFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTableRowProviderTest {
    private static final File DB_TEST_PATH = new File("./testsProvider");

    private TableProviderFactory<DatabaseTableProvider> factory = new TableProviderFactoryImpl();
    private DatabaseTableProvider provider;
    private DatabaseTableRowProvider rowProvider = new DatabaseTableRowProviderImpl();
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
    public void testCreateFor() throws Exception {
        DatabaseTableRow row = rowProvider.createFor(table);
        Assert.assertNotNull(row);
        row.setColumnAt(0, 54);
        Assert.assertNotNull(row.getIntAt(0));
    }

    @Test
    public void testCreateForWithList() throws Exception {
        List<Object> values = new ArrayList<>();
        values.add(5);
        values.add("value");
        DatabaseTableRow storeable = rowProvider.createFor(table, values);
        Assert.assertNotNull(storeable);
        Assert.assertNotNull(storeable.getIntAt(0));
        Assert.assertEquals(storeable.getIntAt(0), Integer.valueOf(5));
    }

    @Test(expected = ParseException.class)
    public void testDeserializeError() throws Exception {
        rowProvider.deserialize(table, "noxmlcontainingstring");
    }

    @Test
    public void testDeserialize() throws Exception {
        DatabaseTableRow st = rowProvider.deserialize(table, "<row><col>5</col><col>value</col></row>");
        Assert.assertEquals(st.getIntAt(0), Integer.valueOf(5));
        Assert.assertEquals(st.getStringAt(1), "value");
    }

    @Test
    public void testSerialize() throws Exception {
        DatabaseTableRow st = rowProvider.createFor(table);
        st.setColumnAt(0, 5);
        st.setColumnAt(1, "value");
        String serialized = rowProvider.serialize(table, st);
        Assert.assertEquals(serialized, "<row><col>5</col><col>value</col></row>");
    }
}
