package org.futuredev.workbench.helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class simplifies the retrieval of
 * classes from a HashMap.
 *
 * Basically, it removes the need to cast when retrieving values.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class ClassMap<K extends Class<?>, V> implements Map<K, V> {

    HashMap<K, V> wrapped = new HashMap<K, V>();

    public ClassMap () {
        this(256);
    }

    public ClassMap (int size) {
        this.wrapped = new HashMap<K, V>(size);
    }

    public V get (Object type) {
        if (!this.wrapped.containsKey(type))
            return null;

        return this.wrapped.get(type);
    }

    public V put (K key, V value) {
        return this.wrapped.put(key, value);
    }

    public V remove (Object obj) {
        return this.wrapped.remove(obj);
    }

    public void putAll (Map<? extends K, ? extends V> map) {
        this.wrapped.putAll(map);
    }

    public Collection<V> values () {
        return this.wrapped.values();
    }

    public Set<K> keySet () {
        return this.wrapped.keySet();
    }

    public Set<Entry<K, V>> entrySet () {
        return this.wrapped.entrySet();
    }

    public boolean containsKey (Object obj) {
        return this.wrapped.containsKey(obj);
    }

    public boolean containsValue (Object obj) {
        return this.wrapped.containsValue(obj);
    }

    public boolean isEmpty () {
        return this.wrapped.isEmpty();
    }

    public int size () {
        return this.wrapped.size();
    }

    public void clear () {
        this.wrapped.clear();
    }

    public int hashCode () {
        return this.wrapped.hashCode();
    }

    public boolean equals (Object obj) {
        return this.wrapped.equals(obj);
    }

    public static <K extends Class<?>, V> ClassMap fromExisting (HashMap<K, V> map) {
        ClassMap<K, V> result = new ClassMap<K, V>();
        result.wrapped = map;
        return result;
    }

}