package annotations;

import java.lang.reflect.Field;

public class JsonSerializer {
    public static String toJson(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        StringBuilder jsonBuilder = new StringBuilder("{");

        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            field.setAccessible(true);
            String jsonKey = field.getName();

            if (field.isAnnotationPresent(JsonKey.class)) {
                JsonKey annotation = field.getAnnotation(JsonKey.class);
                jsonKey = annotation.value();
            }

            Object value = field.get(obj);
            jsonBuilder.append("\"").append(jsonKey).append("\":");
            if (value instanceof String) {
                jsonBuilder.append("\"").append(value).append("\"");
            } else {
                jsonBuilder.append(value);
            }
            if (i < fields.length - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    public static String toJson(Iterable<?> collection) throws IllegalAccessException {
        StringBuilder jsonBuilder = new StringBuilder("[\n");

        boolean isFirst = true;
        for (Object item : collection) {
            if (!isFirst) {
                jsonBuilder.append(",\n");
            }
            jsonBuilder.append("  ").append(toJson(item));
            isFirst = false;
        }
        jsonBuilder.append("\n]");
        return jsonBuilder.toString();
    }
}