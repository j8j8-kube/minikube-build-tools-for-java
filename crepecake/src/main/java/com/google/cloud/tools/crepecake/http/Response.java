/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.tools.crepecake.http;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.cloud.tools.crepecake.blob.BlobStream;
import com.google.cloud.tools.crepecake.blob.BlobStreams;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;

/** Lazily captures an HTTP response. */
public class Response implements Closeable {

  private final HttpRequest request;

  @Nullable private HttpResponse response;

  Response(HttpRequest request) {
    this.request = request;
  }

  public int getResponseCode() throws IOException {
    executeRequest();
    return response.getStatusCode();
  }

  public List<String> getHeader(String headerName) throws IOException {
    executeRequest();
    return response.getHeaders().getHeaderStringValues(headerName);
  }

  public BlobStream getContentStream() throws IOException {
    executeRequest();

    return BlobStreams.from(response.getContent());
  }

  @Override
  public void close() throws IOException {
    if (response == null) {
      return;
    }

    response.disconnect();
  }

  private void executeRequest() throws IOException {
    if (response != null) {
      return;
    }

    response = request.execute();
  }
}