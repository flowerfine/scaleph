package cn.sliew.scaleph.plugin.seatunnel.flink.converter;

import com.fasterxml.jackson.databind.node.ObjectNode;

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

    ObjectNode create();
}
