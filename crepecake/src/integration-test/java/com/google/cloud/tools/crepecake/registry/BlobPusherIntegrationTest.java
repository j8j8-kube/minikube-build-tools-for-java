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

package com.google.cloud.tools.crepecake.registry;

import com.google.cloud.tools.crepecake.blob.Blob;
import com.google.cloud.tools.crepecake.blob.Blobs;
import com.google.cloud.tools.crepecake.image.DescriptorDigest;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.DigestException;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/** Integration tests for {@link BlobPusher}. */
public class BlobPusherIntegrationTest {

  @ClassRule public static LocalRegistry localRegistry = new LocalRegistry(5000);

  @BeforeClass
  public static void setUpLocalRegistry() throws IOException, InterruptedException {
    localRegistry.pullBusybox();
  }

  @Test
  public void testPush()
      throws DigestException, URISyntaxException, IOException, RegistryException {
    Blob fileABlob = Blobs.from(new File(Resources.getResource("fileA").toURI()));
    DescriptorDigest fakeDigest =
        DescriptorDigest.fromHash(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

    RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "busybox");
    String location = registryClient.pushBlob(fakeDigest, fileABlob);

    System.out.println("Location: " + location);
  }
}