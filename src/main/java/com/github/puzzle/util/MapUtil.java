package com.github.puzzle.util;

//public class MapUtil {
//
//    Object map;
//
//    public MapUtil(Object map) {
//        this.map = map;
//    }
//
//    public Set<?> getKeys() {
//        if (map instanceof Map<?,?>) return ((Map<?, ?>) map).keySet();
//        Set<Object> set = new HashSet<>();
//        List<Method> methods = Arrays.stream(map.getClass().getDeclaredMethods()).filter((m) -> m.getName().equals("keys")).toList();
//        Method method = methods.get(0);
//        try {
//            Object keyz = method.invoke(map);
//            List<Method> keyMethodList = Arrays.stream(keyz.getClass().getDeclaredMethods()).filter(m -> m.getName().equals("toArray")).toList();
//            for (Object o : (Array<Object>) keyMethodList.get(0).invoke(keyz))
//                set.add(o);
//            return set;
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public Set<MiniEntry<?, ?>> getEntries() {
//        if (map instanceof Map<?,?>) return (Set<MiniEntry<?, ?>>) ((Map) map).entrySet();
//        Set<MiniEntry<?, ?>> set = new HashSet<>();
//        List<Method> methods = Arrays.stream(map.getClass().getDeclaredMethods()).filter((m) -> m.getName().equals("keys")).toList();
//        Method method = methods.get(0);
//        try {
//            Object keyz = method.invoke(map);
//            List<Method> keyMethodList = Arrays.stream(keyz.getClass().getDeclaredMethods()).filter(m -> m.getName().equals("entries")).toList();
//            for (Object o : (Array<Object>) keyMethodList.get(0).invoke(keyz))
//                set.add(o);
//            return set;
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static String toString(Map<?, ?> map) {
//        return toString(0, new MapUtil(map));
//    }
//
//    public static String toString(Object map) {
//        return toString(0, new MapUtil(map));
//    }
//
//    public static String toString(MapUtil map) {
//        return toString(0, map);
//    }
//
//    static String toString(int depth, Object obj) {
//        if (obj instanceof Map<?,?> map1) return toString(depth, new MapUtil(map1));
//        if (obj instanceof MapUtil map1) return toString(depth, map1);
//        return obj.toString();
//    }
//
//    static String toString(int depth, MapUtil map0) {
//        StringBuilder strDepth = new StringBuilder();
//        strDepth.append(" ".repeat(Math.max(0, depth)));
//
//        StringBuilder builder = new StringBuilder();
//        builder.append("{\n");
//        Object[] array = map0.getKeys().toArray();
//        for (int i = 0; i < array.length; i++) {
//            Object key = array[i];
//
//            Object value = map0.get(key);
//
//            builder.append(strDepth);
//            builder.append(key.toString());
//            builder.append(": ");
//            builder.append(toString(depth + 1, value));
//            if (i < array.length - 1)
//                builder.append(", \n");
//        }
//        builder.append("\n}");
//        return builder.toString();
//    }
//
//    public static class MiniEntry<K, V> implements Map.Entry<K, V> {
//
//        Object o;
//
//        public MiniEntry(Object o) {
//            this.o = o;
//        }
//
//        public K getKey() {
//            try {
//                return Reflection.getFieldContents(o, "key");
//            } catch (Exception e) {
//                try {
//                    return (K) Reflection.getMethod(o.getClass(), "getKey").invoke(o);
//                } catch (InvocationTargetException | IllegalAccessException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        }
//
//        public V getValue() {
//            try {
//                return Reflection.getFieldContents(o, "value");
//            } catch (Exception e) {
//                try {
//                    return (V) Reflection.getMethod(o.getClass(), "getValue").invoke(o);
//                } catch (InvocationTargetException | IllegalAccessException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        }
//
//        @Override
//        public Object setValue(Object value) {
//            return null;
//        }
//    }
//
//}
