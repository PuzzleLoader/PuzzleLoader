package dev.crmodders.puzzle.core.loader.launch.internal.minimixin;

import dev.crmodders.puzzle.core.loader.launch.Piece;
import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.IPropertyKey;

public class Blackboard implements IGlobalPropertyService {
    
    /**
     * Property key
     */
    static class Key implements IPropertyKey {
        
        private final String key;

        Key(String key) {
            this.key = key;
        }
        
        @Override
        public String toString() {
            return this.key;
        }
    }

    public Blackboard() {
        Piece.classLoader.hashCode();
    }
    
    @Override
    public IPropertyKey resolveKey(String name) {
        return new Key(name);
    }

    /**
     * Get a value from the blackboard and duck-type it to the specified type
     * 
     * @param key blackboard key
     * @param <T> duck type
     * @return value
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <T> T getProperty(IPropertyKey key) {
        return (T) Piece.blackboard.get(key.toString());
    }

    /**
     * Put the specified value onto the blackboard
     * 
     * @param key blackboard key
     * @param value new value
     */
    @Override
    public final void setProperty(IPropertyKey key, Object value) {
        Piece.blackboard.put(key.toString(), value);
    }
    
    /**
     * Get the value from the blackboard but return <tt>defaultValue</tt> if the
     * specified key is not set.
     * 
     * @param key blackboard key
     * @param defaultValue value to return if the key is not set or is null
     * @param <T> duck type
     * @return value from blackboard or default value
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <T> T getProperty(IPropertyKey key, T defaultValue) {
        Object value = Piece.blackboard.get(key.toString());
        return value != null ? (T)value : defaultValue;
    }
    
    /**
     * Get a string from the blackboard, returns default value if not set or
     * null.
     * 
     * @param key blackboard key
     * @param defaultValue default value to return if the specified key is not
     *      set or is null
     * @return value from blackboard or default
     */
    @Override
    public final String getPropertyString(IPropertyKey key, String defaultValue) {
        Object value = Piece.blackboard.get(key.toString());
        return value != null ? value.toString() : defaultValue;
    }

}
