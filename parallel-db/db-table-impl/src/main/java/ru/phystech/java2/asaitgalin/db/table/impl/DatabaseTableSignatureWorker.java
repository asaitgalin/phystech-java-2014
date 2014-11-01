package ru.phystech.java2.asaitgalin.db.table.impl;

import com.google.common.base.Joiner;
import ru.phystech.java2.asaitgalin.db.table.api.exceptions.BadSignatureFileException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseTableSignatureWorker {
    private static final String TABLE_SIGNATURE_FILE = "signature.tsv";
    private File signatureFile;

    public DatabaseTableSignatureWorker(File tableDirectory) {
        signatureFile = new File(tableDirectory, TABLE_SIGNATURE_FILE);
    }

    public List<Class<?>> readColumnTypes() {
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(signatureFile));
        } catch (FileNotFoundException fnfe) {
            throw new BadSignatureFileException("bad signature.tsv");
        }
        if (!scanner.hasNextLine()) {
            throw new BadSignatureFileException("bad signature.tsv");
        }
        String[] types = scanner.nextLine().split("\\s");
        for (String s : types) {
            if (DatabaseTableTypes.getClassByName(s) == null) {
                throw new BadSignatureFileException("bad signature.tsv");
            }
        }
        return DatabaseTableTypes.getColumnTypes(types);
    }

    public void writeColumnTypes(List<Class<?>> columnTypes) {
        try {
            if (!signatureFile.exists()) {
                signatureFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(signatureFile));
            List<String> types = new ArrayList<>();
            for (Class<?> cl : columnTypes) {
                types.add(DatabaseTableTypes.getNameByClass(cl));
            }
            Joiner joiner = Joiner.on(" ").skipNulls();
            writer.write(joiner.join(types));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
