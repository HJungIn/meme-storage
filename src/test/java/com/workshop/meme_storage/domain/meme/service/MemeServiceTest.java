package com.workshop.meme_storage.domain.meme.service;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.junit.jupiter.api.Test;

class MemeServiceTest {

    @Test
    public void hashSpeedTest() {
        System.out.print("md5 : ");
        hash(Hashing.md5());
        System.out.print("goodFastHash : ");
        hash(Hashing.goodFastHash(128));
        System.out.print("murmur3_32 : ");
        hash(Hashing.murmur3_32());
        System.out.print("murmur3_128 : ");
        hash(Hashing.murmur3_128());
        System.out.print("sha256 : ");
        hash(Hashing.sha256());
        System.out.print("sha512 : ");
        hash(Hashing.sha512());
    }

    private void hash(HashFunction hf) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            hf.newHasher()
              .putLong(i)
              .putString("name", Charsets.UTF_8)
              .hash();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println("-------------------");
    }
}