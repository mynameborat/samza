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
import org.apache.commons.lang3.StringUtils;


public class MetadataKey {
  private final String appId;
  private final String[] keys;
  private final Namespace namespace;
  private final int version;

  private MetadataKey(String appId, String[] keys, Namespace namespace, int version) {
    Preconditions.checkNotNull(namespace, "Namespace cannot be null");
    Preconditions.checkArgument(StringUtils.isNotEmpty(appId), "Application id cannot be empty");
    this.appId = appId;
    this.keys = keys;
    this.namespace = namespace;
    this.version = version;
  }

  public String getAppId() {
    return appId;
  }

  public String[] getKeys() {
    return keys;
  }

  public Namespace getNamespace() {
    return namespace;
  }

  public int getVersion() {
    return version;
  }

  public static class Builder {
    private String appId;
    private String[] keys;
    private Namespace namespace;
    private int version;

    public Builder() {
      this.version = -1;
    }

    public Builder setAppId(String appId) {
      this.appId = appId;
      return this;
    }

    public Builder setKeys(String... keys) {
      this.keys = keys;
      return this;
    }

    public Builder setNamespace(Namespace namespace) {
      this.namespace = namespace;
      return this;
    }

    public Builder setVersion(int version) {
      this.version = version;
      return this;
    }

    public MetadataKey build() {
      return new MetadataKey(appId, keys, namespace, version);
    }
  }
}
