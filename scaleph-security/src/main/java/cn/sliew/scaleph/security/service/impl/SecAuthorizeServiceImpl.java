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

package cn.sliew.scaleph.security.service.impl;

import cn.sliew.scaleph.security.service.SecAuthorizeService;
import cn.sliew.scaleph.security.service.SecResourceWebService;
import cn.sliew.scaleph.security.service.dto.SecResourceWebDTO;
import cn.sliew.scaleph.security.service.dto.UmiRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecAuthorizeServiceImpl implements SecAuthorizeService {

    @Autowired
    private SecResourceWebService secResourceWebService;

    /**
     * fixme 这里没有获取用户自己的资源，先获取的所有资源
     */
    @Override
    public List<UmiRoute> getWebRoute() {
        return buildRouteByPid(0L);
    }

    private List<UmiRoute> buildRouteByPid(Long pid) {
        List<SecResourceWebDTO> secResourceWebDTOS = secResourceWebService.listByPid(pid, null);
        List<UmiRoute> routes = new ArrayList<>(secResourceWebDTOS.size());
        for (SecResourceWebDTO secResourceWebDTO : secResourceWebDTOS) {
            UmiRoute route = new UmiRoute();
            route.setName(secResourceWebDTO.getName());
            route.setPath(secResourceWebDTO.getPath());
            route.setRedirect(secResourceWebDTO.getRedirect());
            route.setIcon(secResourceWebDTO.getIcon());
            route.setComponent(secResourceWebDTO.getComponent());
            List<UmiRoute> childRoutes = buildRouteByPid(secResourceWebDTO.getId());
            if (CollectionUtils.isEmpty(childRoutes) == false) {
                route.setRoutes(childRoutes);
            }
            routes.add(route);
        }
        return routes;
    }
}
