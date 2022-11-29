package cn.sliew.scaleph.engine.sql.service.factory;

import static cn.sliew.scaleph.engine.sql.service.factory.FlinkMysqlCatalogFactoryOption.PASSWORD;
import static cn.sliew.scaleph.engine.sql.service.factory.FlinkMysqlCatalogFactoryOption.URL;
import static cn.sliew.scaleph.engine.sql.service.factory.FlinkMysqlCatalogFactoryOption.USERNAME;

import cn.sliew.scaleph.engine.sql.service.impl.FlinkCatalogImpl;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.table.catalog.Catalog;
import org.apache.flink.table.factories.CatalogFactory;
import org.apache.flink.table.factories.FactoryUtil;

import java.util.HashSet;
import java.util.Set;

import static org.apache.flink.table.factories.FactoryUtil.PROPERTY_VERSION;

public class FlinkMysqlCatalogFactory implements CatalogFactory {
    @Override
    public String factoryIdentifier() {
        return FlinkMysqlCatalogFactoryOption.IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        final Set<ConfigOption<?>> options = new HashSet<>();
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        final Set<ConfigOption<?>> options = new HashSet<>();
        options.add(USERNAME);
        options.add(PASSWORD);
        options.add(URL);
        options.add(PROPERTY_VERSION);
        return options;
    }

    @Override
    public Catalog createCatalog(Context context) {
        final FactoryUtil.CatalogFactoryHelper helper =
                FactoryUtil.createCatalogFactoryHelper(this, context);
        helper.validate();

        return new FlinkCatalogImpl(
                context.getName(),
                helper.getOptions().get(URL),
                helper.getOptions().get(USERNAME),
                helper.getOptions().get(PASSWORD));
    }

}