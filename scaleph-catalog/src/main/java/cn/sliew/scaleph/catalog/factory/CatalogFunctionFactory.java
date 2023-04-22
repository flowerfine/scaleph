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

package cn.sliew.scaleph.catalog.factory;

import cn.sliew.scaleph.catalog.service.dto.CatalogFunctionDTO;
import cn.sliew.scaleph.common.dict.catalog.CatalogFunctionLanguage;
import org.apache.flink.table.catalog.CatalogFunction;
import org.apache.flink.table.catalog.CatalogFunctionImpl;
import org.apache.flink.table.catalog.FunctionLanguage;

public enum CatalogFunctionFactory {
    ;

    public static CatalogFunctionDTO fromFunction(String functionName, CatalogFunction function) {
        CatalogFunctionDTO catalogFunctionDTO = new CatalogFunctionDTO();
        catalogFunctionDTO.setName(functionName);
        catalogFunctionDTO.setClassName(function.getClassName());
        switch (function.getFunctionLanguage()) {
            case JAVA:
                catalogFunctionDTO.setFunctionLanguage(CatalogFunctionLanguage.JAVA);
                break;
            case SCALA:
                catalogFunctionDTO.setFunctionLanguage(CatalogFunctionLanguage.JAVA);
                break;
            case PYTHON:
                catalogFunctionDTO.setFunctionLanguage(CatalogFunctionLanguage.JAVA);
                break;
            default:
        }
        catalogFunctionDTO.setDescription(function.getDescription().orElse(""));
        return catalogFunctionDTO;
    }

    public static CatalogFunction toCatalogFunction(CatalogFunctionDTO catalogFunctionDTO) {
        String className = catalogFunctionDTO.getClassName();
        FunctionLanguage functionLanguage = null;
        switch (catalogFunctionDTO.getFunctionLanguage()) {
            case JAVA:
                functionLanguage = FunctionLanguage.JAVA;
                break;
            case SCALA:
                functionLanguage = FunctionLanguage.SCALA;
                break;
            case PYTHON:
                functionLanguage = FunctionLanguage.PYTHON;
                break;
            default:
        }
        return new CatalogFunctionImpl(className, functionLanguage);
    }

}
