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

package cn.sliew.scaleph.storage.service;

import java.io.InputStream;

/**
 * @deprecated file directory is not suitable for object store, try to remove directory.
 */
@Deprecated
public interface StorageService {

    boolean exists(String filePath);

    void delete(String filePath, String fileName);

    void mkdirs(String filePath);

    String upload(InputStream inputStream, String filePath, String fileName);

    boolean isDirectory(String filePath);

    InputStream get(String filePath, String fileName);

    Long getFileSize(String filePath, String fileName);
}
