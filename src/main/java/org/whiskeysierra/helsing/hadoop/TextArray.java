package org.whiskeysierra.helsing.hadoop;

import com.google.common.collect.Ordering;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

final class TextArray extends ArrayWritable implements WritableComparable<TextArray> {

    TextArray() {
        super(Text.class);
    }

    public TextArray(Text[] values) {
        super(Text.class, values);
    }

    @Override
    public int compareTo(TextArray that) {
        final String[] left = this.toStrings();
        final String[] right = that.toStrings();

        final int max = Math.min(left.length, right.length);

        for (int i = 0; i < max; i++) {
            final int result = Ordering.natural().compare(left[i], right[i]);
            if (result == 0) {
                continue;
            } else {
                return result;
            }
        }

        return 0;
    }

}
