package de.bht.pat.tenzing.hadoop.functions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface AggregateFunction {

    String value();

}
