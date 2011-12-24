package de.bht.pat.tenzing.hadoop.io;

import com.google.common.base.Function;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;

public class TextArray extends ForwardingList<Text> implements Writable {

    private final List<Text> texts;

    public TextArray() {
        this.texts = Lists.newArrayList();
    }

    public TextArray(List<String> values) {
        this.texts = Lists.transform(values, new Function<String, Text>() {

            @Override
            public Text apply(@Nullable String input) {
                return new Text(input);
            }

        });
    }

    @Override
    protected List<Text> delegate() {
        return texts;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        for (Text text : texts) {
            text.write(output);
            output.writeUTF("\t");
        }
    }

    @Override
    public void readFields(DataInput input) throws IOException {
        try {
            final Text text = new Text();
            text.readFields(input);
            input.readUTF();
            texts.add(text);
        } catch (EOFException e) {

        }
    }

}
