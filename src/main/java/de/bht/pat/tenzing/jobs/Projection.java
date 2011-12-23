package de.bht.pat.tenzing.jobs;

import com.google.common.collect.BiMap;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.HashBiMap;
import org.apache.hadoop.io.Writable;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

public final class Projection extends ForwardingMap<String, Integer> implements
    BiMap<String, Integer>, Serializable, Writable {

    private final BiMap<String, Integer> delegate = HashBiMap.create();

    @Override
    protected BiMap<String, Integer> delegate() {
        return delegate;
    }

    @Override
    public Integer forcePut(@Nullable String key, @Nullable Integer value) {
        return delegate().forcePut(key, value);
    }

    @Override
    public BiMap<Integer, String> inverse() {
        return inverse();
    }

    @Override
    public Set<Integer> values() {
        return delegate().values();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        for (Entry<String, Integer> entry : delegate().entrySet()) {
            output.writeUTF(entry.getKey());
            output.writeInt(entry.getValue());
        }
    }

    @Override
    public void readFields(DataInput input) throws IOException {
        while (true) {
            try {
                final String column = input.readUTF();
                final int index = input.readInt();
                delegate().put(column, index);
            } catch (EOFException ignored) {
                break;
            }
        }
    }

}
