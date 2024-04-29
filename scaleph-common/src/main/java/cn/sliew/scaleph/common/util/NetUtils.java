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

package cn.sliew.scaleph.common.util;

import cn.sliew.milky.common.exception.Rethrower;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * NetUtils
 */

@Slf4j
public enum NetUtils {
    ;

    private static final String ETHERNET_PREFIX = "eth";
    private static final String MAC_ETHERNET_PREFIX = "en";
    private static final String WIRELESS_LAN_PREFIX = "wlp";
    private static final String[] LOCAL_IPS = {
            "localhost", "127.0.0.1", "0.0.0.0"
    };

    /**
     * Pre-loaded local address
     */
    public static InetAddress localAddress;

    static {
        try {
            localAddress = getLocalInetAddress();
        } catch (SocketException e) {
            log.error("fail to get local ip.", e);
            throw new RuntimeException("fail to get local ip.", e);
        }
    }

    /**
     * Retrieve the first validated local ip address(the Public and LAN ip addresses are validated).
     *
     * @return the local address
     * @throws SocketException the socket exception
     */
    public static InetAddress getLocalInetAddress() throws SocketException {
        if (localAddress != null) {
            return localAddress;
        }
        // List of all network interfaces
        List<NetworkInterface> networkInterfaces = getNetworkInterfaces();
        log.info("Trying to find ethernet...");
        NetworkInterface ni = getByEthPrefix(networkInterfaces)
                .or(() -> getByEnPrefix(networkInterfaces))
                .or(() -> getByWlpPrefix(networkInterfaces))
                .or(() -> getByFirst(networkInterfaces))
                .orElseThrow();
        if (ni != null) {
            log.info("Found network interface: index = {}, name = {}, mac = {}, ",
                    ni.getIndex(), ni.getName(), bytesToMac(ni.getHardwareAddress()));
            Enumeration<InetAddress> addressEnumeration = ni.getInetAddresses();
            while (addressEnumeration.hasMoreElements()) {
                InetAddress address = addressEnumeration.nextElement();
                // ignores all invalidated addresses
                if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                    log.info("Invalid address: {}", address);
                    continue;
                }
                try {
                    if (address.isSiteLocalAddress() && address.isReachable(1000)) {
                        return address;
                    }
                    log.warn("Ip {} of network interface {} is not reachable", address, ni.getName());
                } catch (IOException e) {
                    log.warn(e.getLocalizedMessage(), e);
                }
            }
        }

        // If no available interface found, return the localhost.
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            log.warn("No available interface found, use localhost: {}", localHost);
            return localHost;
        } catch (Exception e) {
            throw new RuntimeException("No validated local address!");
        }
    }

    private static Optional<NetworkInterface> getByEthPrefix(List<NetworkInterface> networkInterfaces) {
        return networkInterfaces.stream()
                // Find NetworkInterfaces named eth{n}, This means wired network
                .filter(networkInterface -> networkInterface.getName().startsWith(ETHERNET_PREFIX))
                .findAny();
    }

    private static Optional<NetworkInterface> getByEnPrefix(List<NetworkInterface> networkInterfaces) {
        return networkInterfaces.stream()
                // Find NetworkInterfaces named en{n}, This means wired network
                .filter(networkInterface -> networkInterface.getName().startsWith(MAC_ETHERNET_PREFIX))
                .sorted(Comparator.comparingLong(NetworkInterface::getIndex).reversed())
                .findFirst();
    }

    private static Optional<NetworkInterface> getByWlpPrefix(List<NetworkInterface> networkInterfaces) {
        return networkInterfaces
                .stream()
                // Then try to find NetworkInterfaces named wlp{n}s{n}, This means wireless network
                .filter(networkInterface -> networkInterface.getName().startsWith(WIRELESS_LAN_PREFIX))
                .findAny();
    }

    private static Optional<NetworkInterface> getByFirst(List<NetworkInterface> networkInterfaces) {
        return networkInterfaces
                // Then find NetworkInterfaces by its index
                // Generally the wired and wireless network are ahead of those
                // who are created by docker/minikube and other softwares.
                .stream()
                .sorted(Comparator.comparingLong(NetworkInterface::getIndex).reversed())
                .findFirst()
                .filter(Objects::nonNull)
                // Finally get the first network interface in the list
                .or(() -> {
                    log.info("No network interface by index, trying to get the first one or return null");
                    return networkInterfaces
                            .stream()
                            .findAny();
                });
    }

    private static List<NetworkInterface> getNetworkInterfaces() throws SocketException {
        List<NetworkInterface> networkInterfaces = new ArrayList<>();
        Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();
        while (enu.hasMoreElements()) {
            NetworkInterface ni = enu.nextElement();
            // Skip the interface which is loopback or is down or is virtual
            if (ni.isLoopback() || !ni.isUp() || ni.isVirtual()) {
                continue;
            }
            networkInterfaces.add(ni);
            log.debug("Valid network interface: index = {}, name = {}, mac = {}",
                    ni.getIndex(), ni.getName(), bytesToMac(ni.getHardwareAddress()));
        }
        if (networkInterfaces.isEmpty()) {
            log.warn("No validated local address!");
        }
        return networkInterfaces;
    }

    /**
     * Retrieve local address
     *
     * @return the string local address
     */
    public static String getLocalAddress() {
        return localAddress.getHostAddress();
    }

    public static String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    public static String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    /**
     * Convert mac bytes to string
     *
     * @param bytes Mac in bytes
     * @return Mac in string
     */
    private static String bytesToMac(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'd', 'f'};
        StringJoiner stringJoiner = new StringJoiner(":");
        for (byte aByte : bytes) {
            char left = chars[(aByte & 0xF0) >> 4];
            char right = chars[aByte & 0x0F];
            stringJoiner.add(new String(new char[]{left, right}));
        }
        return stringJoiner.toString();
    }

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
