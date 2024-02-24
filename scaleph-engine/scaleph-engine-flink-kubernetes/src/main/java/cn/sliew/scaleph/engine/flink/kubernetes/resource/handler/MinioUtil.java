/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.engine.flink.kubernetes.resource.handler;

import cn.sliew.scaleph.system.snowflake.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;

@Slf4j
public enum MinioUtil {
    ;

    /* The common local hosts and ips */
    private static final String[] LOCAL_IPS = {
            "localhost", "127.0.0.1", "0.0.0.0"
    };

    /**
     * If the endpoint pointing to a local address,
     * we should replace the local address with PUBLIC address
     * to make sure all pods/container can access to the minio.
     *
     * <p>
     * Generally, the endpoint should be a uri.
     * So we firstly parse it to a uri and get the host.
     * Then check if the host is a local address.
     * </p>
     * <p>
     * If exception occurs in parsing uri,
     * we replace the endpoint in hard coded ways.
     * </p>
     *
     * @param endpoint The minio endpoint, Generally in uri format
     * @return Replaced endpoint.
     */
    public static String replaceLocalhost(String endpoint) {
        String result = endpoint;
        try {
            URI uri = URI.create(endpoint);
            String host = uri.getHost();
            InetAddress inetAddress = InetAddress.getByName(host);
            if (inetAddress.isLoopbackAddress() || inetAddress.isAnyLocalAddress()) {
                log.debug("Host {}, address = {} is a local address", host, inetAddress);
                InetAddress publicAddress = NetUtils.getLocalInetAddress();
                log.debug("Public address is {}", publicAddress);
                if (validateInetAddress(publicAddress)) {
                    log.debug("Public address {} is valid!", publicAddress);
                    URI res = new URI(uri.getScheme(),
                            uri.getUserInfo(),
                            publicAddress.getHostAddress(),
                            uri.getPort(),
                            uri.getPath(),
                            uri.getQuery(),
                            uri.getFragment());
                    result = res.toString();
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            try {
                InetAddress publicAddress = NetUtils.getLocalInetAddress();
                log.debug("Public address is {}", publicAddress);
                if (validateInetAddress(publicAddress)) {
                    log.debug("Public address {} is valid!", publicAddress);
                    for (String localIp : LOCAL_IPS) {
                        if (result.contains(localIp)) {
                            log.debug("Endpoint {} contains local ip {}", result, localIp);
                            result = result.replace(localIp, publicAddress.getHostAddress());
                        }
                    }
                }
            } catch (SocketException ex) {
                log.error(ex.getLocalizedMessage(), ex);
            }
        }
        log.debug("Final endpoint is {}", result);
        return result;
    }

    private static boolean validateInetAddress(InetAddress inetAddress) {
        return !inetAddress.isLinkLocalAddress()
                && !inetAddress.isLoopbackAddress()
                && !inetAddress.isAnyLocalAddress();
    }
}
