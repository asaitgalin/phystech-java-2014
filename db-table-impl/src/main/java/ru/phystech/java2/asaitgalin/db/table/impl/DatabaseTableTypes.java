package ru.phystech.java2.asaitgalin.db.table.impl;

import java.util.*;

public enum DatabaseTableTypes {
    INTEGER("int", Integer.class) {
        @Override
        public Object parseValue(String s) {
            return Integer.parseInt(s);
        }
    },
    LONG("long", Long.class) {
        @Override
        public Object parseValue(String s) {
            return Long.parseLong(s);
        }
    },
    BYTE("byte", Byte.class) {
        @Override
        public Object parseValue(String s) {
            return Byte.parseByte(s);
        }
    },
    FLOAT("float", Float.class) {
        @Override
        public Object parseValue(String s) {
            return Float.parseFloat(s);
        }
    },
    DOUBLE("double", Double.class) {
        @Override
        public Object parseValue(String s) {
            return Double.parseDouble(s);
        }
    },
    BOOLEAN("boolean", Boolean.class) {
        @Override
        public Object parseValue(String s) {
            return Boolean.parseBoolean(s);
        }
    },
    STRING("String", String.class) {
        @Override
        public Object parseValue(String s) {
            return s;
        }
    };

    private final String name;
    private final Class<?> clazz;

    private DatabaseTableTypes(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    private static final Map<String, DatabaseTableTypes> NAME_TO_TYPE;
    private static final Map<Class<?>, DatabaseTableTypes> CLASS_TO_TYPE;

    static {
        Map<String, DatabaseTableTypes> tmpNameToClass = new HashMap<>();
        Map<Class<?>, DatabaseTableTypes> tmpClassToName = new HashMap<>();
        for (DatabaseTableTypes type : values()) {
            tmpNameToClass.put(type.name, type);
            tmpClassToName.put(type.clazz, type);
        }
        NAME_TO_TYPE = Collections.unmodifiableMap(tmpNameToClass);
        CLASS_TO_TYPE = Collections.unmodifiableMap(tmpClassToName);
    }

    public static String getNameByClass(Class<?> clazz) {
        DatabaseTableTypes types = CLASS_TO_TYPE.get(clazz);
        if (types == null) {
            throw new IllegalArgumentException("types: unknown type class");
        }
        return types.name;
    }

    public abstract Object parseValue(String s);

    public static Object parseValueWithClass(String s, Class<?> expectedClass) {
        DatabaseTableTypes types = CLASS_TO_TYPE.get(expectedClass);
        if (types == null) {
            throw new IllegalArgumentException("types: unknown type");
        }
        return types.parseValue(s);
    }

    public static Class<?> getClassByName(String name) {
        DatabaseTableTypes types = NAME_TO_TYPE.get(name);
        if (types == null) {
            throw new IllegalArgumentException("types: unknown type name");
        }
        return types.clazz;
    }

    public static List<Class<?>> getColumnTypes(String[] names) {
        List<Class<?>> columnsList = new ArrayList<>();
        for (String s : names) {
            columnsList.add(getClassByName(s));
        }
        return columnsList;
    }
}
