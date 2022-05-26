package cn.sliew.scaleph.plugin.seatunnel.flink;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Properties;

/**
 *         env
 *                     jdbc
 *         source      file
 *                     kafka
 *                     split
 * conf    transform   rename
 *                     filter
 *                     jdbc
 *         sink        file
 *                     kafka
 */
public interface ConfConverter {

    ObjectNode create(Properties properties);
}
