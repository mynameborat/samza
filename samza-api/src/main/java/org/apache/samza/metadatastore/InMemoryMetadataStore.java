/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.samza.metadatastore;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;


/**
 * In-memory {@link MetadataStore} with no persistence on disk.
 */
public class InMemoryMetadataStore implements MetadataStore {
  private static final String KEY_SEPARATOR = ":";
  private static final String SUB_KEY_SEPARATOR = "-";
  private final ConcurrentHashMap<String, byte[]> memStore = new ConcurrentHashMap<>();

  @Override
  public void init() { }

  @Override
  public byte[] get(String key) {
    return memStore.get(key);
  }

  @Override
  public byte[] get(MetadataKey key) {
    return get(buildStoreKeyFromMetadataKey(key));
  }

  @Override
  public void put(String key, byte[] value) {
    memStore.put(key, value);
  }

  @Override
  public void put(MetadataKey key, byte[] value) {
    put(buildStoreKeyFromMetadataKey(key), value);
  }

  @Override
  public void delete(String key) {
    memStore.remove(key);
  }

  @Override
  public void delete(MetadataKey key) {
    delete(buildStoreKeyFromMetadataKey(key));
  }

  @Override
  public Map<String, byte[]> all() {
    return ImmutableMap.copyOf(memStore);
  }

  @Override
  public void flush() { }

  @Override
  public void close() { }

  private String buildStoreKeyFromMetadataKey(MetadataKey metadataKey) {
    Preconditions.checkNotNull(metadataKey.getNamespace(), "Namespace cannot be null");
    Preconditions.checkArgument(StringUtils.isNotEmpty(metadataKey.getAppId()), "Invalid application id");

    StringBuilder physicalKeyBuilder = new StringBuilder();

    physicalKeyBuilder
        .append(metadataKey.getNamespace())
        .append(KEY_SEPARATOR)
        .append(metadataKey.getAppId());

    String subKey = constructSubKeys(metadataKey.getKeys());
    if (StringUtils.isNotEmpty(subKey)) {
      physicalKeyBuilder.append(KEY_SEPARATOR)
          .append(subKey);
    }

    if (metadataKey.getVersion() > 0) {
      physicalKeyBuilder.append(KEY_SEPARATOR)
          .append(metadataKey.getVersion());
    }

    return physicalKeyBuilder.toString();
  }

  private String constructSubKeys(String[] keys) {
    StringBuilder subKeyBuilder = new StringBuilder();

    for (int i = 0; i < keys.length - 1; i++) {
      subKeyBuilder.append(keys[i])
          .append(SUB_KEY_SEPARATOR);
    }

    subKeyBuilder.append(keys[keys.length - 1]);
    return subKeyBuilder.toString();
  }
}
