package com.vladimir22;

import java.util.HashMap;
import java.util.Map;


//

public abstract class Digest {

    private Map<byte[], byte[]> cache = new HashMap<byte[], byte[]>(); // diamond

    public byte[] digest(byte[] input) {

        // limit on size



//        if (result == null) {
            synchronized (cache) {
                byte[] result; // mutable, can be accessed,
                result = cache.get(input);

                result.clone(); // JDK Object works perfectly

                if (result == null) {
                    result = doDigest(input);
                    cache.put(input, result);
                }
               return result;
            }
//        }

    }

    protected abstract byte[] doDigest(byte[] input); // no side effects // expensive
}