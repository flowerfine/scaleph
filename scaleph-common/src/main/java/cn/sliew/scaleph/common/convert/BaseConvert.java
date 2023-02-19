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

package cn.sliew.scaleph.common.convert;

import java.util.List;

/**
 * do和dto对象转换接口
 *
 * @param <E>
 * @param <D>
 * @author gleiyu
 */
public interface BaseConvert<E, D> {
    /**
     * dto转换为do
     *
     * @param dto dto object
     * @return E entity
     */
    E toDo(D dto);

    /**
     * do转换为dto
     *
     * @param entity do object
     * @return D dto
     */
    D toDto(E entity);

    /**
     * dto list 转换为 do list
     *
     * @param dtoList dto List
     * @return List<E> entity list
     */
    List<E> toDo(List<D> dtoList);

    /**
     * do list 转换为 dto list
     *
     * @param entityList do list
     * @return List<D> dto list
     */
    List<D> toDto(List<E> entityList);

}
