package de.bht.pat.tenzing.hadoop;

import com.google.inject.AbstractModule;
import de.bht.pat.tenzing.hadoop.jobs.JobsModule;
import de.bht.pat.tenzing.sql.SqlModule;

final class HadoopModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JobsModule());
        install(new SqlModule());
    }

}
