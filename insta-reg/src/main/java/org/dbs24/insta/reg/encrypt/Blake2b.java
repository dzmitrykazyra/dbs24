package org.dbs24.insta.reg.encrypt;


/**
 *
 */
public interface Blake2b {

    // ---------------------------------------------------------------------
    // Specification
    // ---------------------------------------------------------------------
    interface Spec {

        /**
         * pblock size of blake2b
         */
        int param_bytes = 64;

        /**
         * pblock size of blake2b
         */
        int block_bytes = 128;

        /**
         * maximum digest size
         */
        int max_digest_bytes = 64;

        /**
         * maximum key sie
         */
        int max_key_bytes = 64;

        /**
         * maximum salt size
         */
        int max_salt_bytes = 16;

        /**
         * maximum personalization string size
         */
        int max_personalization_bytes = 16;

        /**
         * length of h space vector array
         */
        int state_space_len = 8;

        /**
         * max tree fanout value
         */
        int max_tree_fantout = 0xFF;

        /**
         * max tree depth value
         */
        int max_tree_depth = 0xFF;

        /**
         * max tree leaf length value.Note that this has uint32 semantics and
         * thus 0xFFFFFFFF is used as max value limit.
         */
        int max_tree_leaf_length = 0xFFFFFFFF;

        /**
         * max node offset value. Note that this has uint64 semantics and thus
         * 0xFFFFFFFFFFFFFFFFL is used as max value limit.
         */
        long max_node_offset = 0xFFFFFFFFFFFFFFFFL;

        /**
         * max tree inner length value
         */
        int max_tree_inner_length = 0xFF;

        /**
         * initialization values map ref-Spec IV[i] -> slice iv[i*8:i*8+7]
         */
        long[] IV = {
            0x6a09e667f3bcc908L,
            0xbb67ae8584caa73bL,
            0x3c6ef372fe94f82bL,
            0xa54ff53a5f1d36f1L,
            0x510e527fade682d1L,
            0x9b05688c2b3e6c1fL,
            0x1f83d9abfb41bd6bL,
            0x5be0cd19137e2179L
        };
    }


    /**
     *
     */
    void update(byte[] input);

    /**
     *
     */
    void update(byte input);

    /**
     *
     */
    void update(byte[] input, int offset, int len);

    /**
     *
     */
    byte[] digest();

    /**
     *
     */
    byte[] digest(byte[] input);

    /**
     *
     */
    void digest(byte[] output, int offset, int len);

    /**
     *
     */
    void reset();

    ResumeHandle state();

}
