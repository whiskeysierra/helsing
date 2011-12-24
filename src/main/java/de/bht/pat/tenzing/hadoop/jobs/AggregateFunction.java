package de.bht.pat.tenzing.hadoop.jobs;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AggregateFunction {

    String value();

}
