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

package cn.sliew.scaleph.system.snowflake.utils;

import cn.sliew.milky.common.exception.Rethrower;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * NetUtils
 */

@Slf4j
public class NetUtils {

    private static final String ETHERNET_PREFIX = "eth";
    private static final String WIRELESS_LAN_PREFIX = "wlp";

    /**
     * Pre-loaded local address
     */
    public static InetAddress localAddress;

    static {
        try {
            localAddress = getLocalInetAddress();
        } catch (SocketException e) {
            throw new RuntimeException("fail to get local ip.");
        }
    }

    private NetUtils() {
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
        NetworkInterface ni = networkInterfaces.stream()
                // Find NetworkInterfaces named eth{n}, This means wired network
                .filter(networkInterface -> networkInterface.getName().startsWith(ETHERNET_PREFIX))
                .findAny()
                .orElseGet(() -> {
                    log.info("No network interface name starts with {}, trying to find wireless...", ETHERNET_PREFIX);
                    return networkInterfaces
                            .stream()
                            // Then try to find NetworkInterfaces named wlp{n}s{n}, This means wireless network
                            .filter(networkInterface -> networkInterface.getName().startsWith(WIRELESS_LAN_PREFIX))
                            .findAny()
                            .orElseGet(() -> {
                                log.info("No network interface name starts with {}, trying to find by index...", WIRELESS_LAN_PREFIX);
                                return networkInterfaces
                                        // Then find NetworkInterfaces by its index
                                        // Generally the wired and wireless network are ahead of those
                                        // who are created by docker/minikube and other softwares.
                                        .stream()
                                        .mapToInt(NetworkInterface::getIndex)
                                        .sorted()
                                        .findFirst()
                                        .stream()
                                        .mapToObj(index -> {
                                            try {
                                                NetworkInterface byIndex = NetworkInterface.getByIndex(index);
                                                log.info("Index {} with interface name {}", index, byIndex.getName());
                                                return byIndex;
                                            } catch (SocketException e) {
                                                log.error(e.getLocalizedMessage(), e);
                                            }
                                            return null;
                                        })
                                        .filter(Objects::nonNull)
                                        .findFirst()
                                        // Finally get the first network interface in the list
                                        .orElseGet(() -> {
                                            log.info("No network interface by index, trying to get the first one or return null");
                                            return networkInterfaces
                                                    .stream()
                                                    .findAny()
                                                    .orElse(null);
                                        });
                            });
                });
        if (ni != null) {
            log.info("Found network interface: index = {}, name = {}, mac = {}",
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
                    if (!address.isReachable(1000)) {
                        log.warn("Ip {} of network interface {} is not reachable", address, ni.getName());
                    }
                } catch (IOException e) {
                    log.warn(e.getLocalizedMessage(), e);
                }
                return address;
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

    private static List<NetworkInterface> getNetworkInterfaces() throws SocketException {
        List<NetworkInterface> networkInterfaces = new ArrayList<>();
        Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();
        while (enu.hasMoreElements()) {
            NetworkInterface ni = enu.nextElement();
            // Skip the interface which is loopback or is down or is virtual
            if (ni.isLoopback() || !ni.isUp() || ni.isVirtual()) {
                log.info("Invalid network interface: index = {}, name = {}, mac = {}",
                        ni.getIndex(), ni.getName(), bytesToMac(ni.getHardwareAddress()));
                continue;
            }
            networkInterfaces.add(ni);
            log.info("Valid network interface: index = {}, name = {}, mac = {}",
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
}
