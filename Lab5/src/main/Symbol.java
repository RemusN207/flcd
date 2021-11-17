package main;

import java.util.Objects;

public class Symbol {
    public final String info;

    protected Symbol(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Symbol))
            return false;
        return Objects.equals(info, ((Symbol) obj).info);
    }

    @Override
    public String toString() {
        return info;
    }
}
