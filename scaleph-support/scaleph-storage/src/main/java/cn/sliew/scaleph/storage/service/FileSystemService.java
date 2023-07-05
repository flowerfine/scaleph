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

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileSystemService {

    FileSystem getFileSystem();

    boolean isDistributedFS();

    boolean exists(String path) throws IOException;

    List<String> list(String path) throws IOException;

    InputStream get(String path) throws IOException;

    Path upload(InputStream inputStream, String path) throws IOException;

    boolean delete(String path) throws IOException;

    Long getFileSize(String path) throws IOException;

    List<FileStatus> listStatus(String path) throws IOException;
}
