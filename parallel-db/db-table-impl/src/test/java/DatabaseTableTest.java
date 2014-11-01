import org.apache.commons.io.FileUtils;
import org.junit.*;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTable;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableProvider;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRow;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRowProvider;
import ru.phystech.java2.asaitgalin.db.table.impl.DatabaseTableProviderImpl;
import ru.phystech.java2.asaitgalin.db.table.impl.DatabaseTableRowProviderImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTableTest {
    private static final File DB_TEST_PATH = new File("./testsTable");

    private static DatabaseTableProvider provider;
    private static DatabaseTableRowProvider rowProvider;
    private static DatabaseTable testTable;

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
        List<Class<?>> columns = new ArrayList<>();
        columns.add(Integer.class);
        columns.add(String.class);
        provider = new DatabaseTableProviderImpl(DB_TEST_PATH);
        testTable = provider.createTable("table3", columns);
        rowProvider = new DatabaseTableRowProviderImpl();
    }

    @After
    public void tearDown() throws Exception {
        provider.removeTable("table3");
    }

    @Test
    public void testGetName() throws Exception {
        Assert.assertEquals(testTable.getName(), "table3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNull() throws Exception {
        testTable.get(null);
    }

    @Test
    public void testGet() throws Exception {
        testTable.put("key", rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>"));
        DatabaseTableRow st = testTable.get("key");
        Assert.assertNotNull(st);
        Assert.assertEquals(st.getIntAt(0), Integer.valueOf(5));
        Assert.assertNull(testTable.get("not_existent_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() throws Exception {
        testTable.put(null, rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullValue() throws Exception {
        testTable.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAlienStorable() throws Exception {
        List<Class<?>> types = new ArrayList<>();
        types.add(Integer.class);
        DatabaseTable otherTable = provider.createTable("otherTable", types);
        testTable.put("alienstorable", rowProvider.deserialize(otherTable, "<row><col>5</col></row>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutKeyWithSpaces()  throws Exception {
        testTable.put("key    with spaces", rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithSpaces() throws Exception {
        testTable.put("   ", rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>"));
    }

    @Test
    public void testPut() throws Exception {
        DatabaseTableRow st = rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>");
        Assert.assertNull(testTable.put("new_key", st));
        Assert.assertEquals(testTable.put("new_key", rowProvider.deserialize(testTable,
                "<row><col>5</col><col>testval</col></row>")).toString(), st.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNull() throws Exception {
        testTable.remove(null);
    }

    @Test
    public void testRemove() throws Exception {
        testTable.put("1", rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>"));
        Assert.assertNull(testTable.remove("not_existing_key"));
        Assert.assertNull(testTable.remove("not_existing_key2"));
        Assert.assertEquals(testTable.remove("1").toString(), "[5,value]");
    }

    @Test
    public void testSize() throws Exception {
        for (int i = 0; i < 7; ++i) {
            testTable.put("sizeKey" + i, rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>"));
        }
        Assert.assertEquals(7, testTable.size());
        testTable.remove("sizeKey5");
        Assert.assertEquals(6, testTable.size());
        testTable.rollback();
        Assert.assertEquals(0, testTable.size());
    }

    @Test
    public void testCommit() throws Exception {
        testTable.put("1", rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>"));
        testTable.put("3", rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>"));
        testTable.put("5", rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>"));
        Assert.assertEquals(3, testTable.commit());
    }

    @Test
    public void testRollback() throws Exception {
        for (int i = 0; i < 7; ++i) {
            testTable.put("rollbackKey" + i, rowProvider.deserialize(testTable,
                    "<row><col>5</col><col>value</col></row>"));
        }
        testTable.commit();
        testTable.put("rollbackKey4", rowProvider.deserialize(testTable, "<row><col>5</col><col>value222</col></row>"));
        testTable.put("newKey", rowProvider.deserialize(testTable, "<row><col>6</col><col>value</col></row>"));
        Assert.assertEquals(2, testTable.rollback());
        Assert.assertNull(testTable.get("newKey"));
        Assert.assertEquals(testTable.get("rollbackKey4").toString(),
                rowProvider.deserialize(testTable, "<row><col>5</col><col>value</col></row>").toString());
    }

    @Test
    public void testGetColumnsCount() throws Exception {
        Assert.assertEquals(testTable.getColumnsCount(), 2);
    }


    @Test
    public void testGetColumnType() throws Exception {
        Assert.assertEquals(testTable.getColumnType(0).getSimpleName(), "Integer");
        Assert.assertEquals(testTable.getColumnType(1).getSimpleName(), "String");
    }

}
