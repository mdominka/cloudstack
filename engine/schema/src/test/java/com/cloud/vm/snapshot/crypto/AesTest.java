package com.cloud.vm.snapshot.crypto;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AesTest {

  private static final String PASSWORD = "verySave1";

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void encrypt() {
    final String encrypted = Aes.encrypt(PASSWORD);
    Assert.assertNotNull(encrypted);
  }

  @Test
  public void decrypt() {
    final String encrypted = Aes.encrypt(PASSWORD);
    assertEquals(PASSWORD, Aes.decrypt(encrypted));
  }
}