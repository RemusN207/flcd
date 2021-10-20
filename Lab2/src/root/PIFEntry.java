package root;

import java.util.AbstractMap;
import java.util.Map;

public class PIFEntry<R, T> extends AbstractMap.SimpleEntry<R, T> {
    public PIFEntry(R key, T value) {
        super(key, value);
    }

    public PIFEntry(Map.Entry<? extends R, ? extends T> entry) {
        super(entry);
    }

    @Override
    public String toString() {
        return getKey() + ", " + getValue();
    }
}
