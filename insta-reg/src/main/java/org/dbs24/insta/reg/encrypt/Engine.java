/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.encrypt;

import java.io.PrintStream;

/**
 *
 * @author kdg
 */
public class Engine implements Blake2b {

    // ---------------------------------------------------------------------
    // Blake2b State(+) per reference implementation
    // ---------------------------------------------------------------------
    // REVU: address last_node TODO part of the Tree/incremental
    static final class State {

        /**
         * per spec
         */
        public final long[] h = new long[8];
        /**
         * per spec
         */
        public final long[] t = new long[2];
        /**
         * per spec
         */
        public final long[] f = new long[2];
        /**
         * per spec (tree)
         */
        public boolean last_node = false;
        /**
         * pulled up 2b optimal
         */
        public final long[] m = new long[16];
        /**
         * pulled up 2b optimal
         */
        public final long[] v = new long[16];

        /**
         * compressor cache buffer
         */
        public final byte[] buffer;
        /**
         * compressor cache buffer offset/cached data length
         */
        public int buflen;

        /**
         * digest length from init param - copied here on init
         */
        public final int outlen;

        public final int digestType;

        State(int digestLength, boolean isMac) {
            this.buffer = new byte[Blake2b.Spec.block_bytes];
            this.outlen = digestLength;
            // do not use zero, so we can detect serialization errors
            this.digestType = isMac ? 1 : 2;
        }

        public ResumeHandle toResumableForm() {
            ResumeHandle state = new ResumeHandle();
            state.h = h;
            state.t = t;
            state.f = f;
            state.last_node = last_node;
            state.m = m;
            state.v = v;
            state.buffer = buffer;
            state.buflen = buflen;
            state.outlen = outlen;
            state.type = digestType;
            return state;
        }
    }

    private final State state;
    /**
     * configuration params
     */
    private final Param param;

    /**
     * read only
     */
    private static final byte[] zeropad = new byte[Blake2b.Spec.block_bytes];

    /**
     * a little bit of semantics
     */
    interface flag {

        int last_block = 0;
        int last_node = 1;
    }

    /**
     * to support update(byte)
     */
    private final byte[] oneByte = new byte[1];

    // ---------------------------------------------------------------------
    // Ctor & Initialization
    // ---------------------------------------------------------------------
    /**
     * Basic use constructor pending (TODO) JCA/JCE compliance
     */
    Engine() {
        this(new Param());
    }

    Engine(State state, Param param) {
        assert state != null : "state is null";
        assert param != null : "param is null";
        this.state = state;
        this.param = param;
    }

    /**
     * User provided Param for custom configurations
     */
    Engine(final Param param) {
        assert param != null : "param is null";
        this.param = param;
        state = new State(param.getDigestLength(), this instanceof Mac);
        if (param.getDepth() > Param.Default.depth) {
            final int ndepth = param.getNodeDepth();
            final long nxoff = param.getNodeOffset();
            if (ndepth == param.getDepth() - 1) {
                state.last_node = true;
                assert nxoff == 0 : "root must have offset of zero";
            } else if (nxoff == param.getFanout() - 1) {
                this.state.last_node = true;
            }
        }

        initialize();
    }

    public ResumeHandle state() {
        return state.toResumableForm();
    }

    private void initialize() {
        // state vector h - copy values to address reset() requests
        System.arraycopy(param.initialized_H(), 0, this.state.h, 0, Blake2b.Spec.state_space_len);

        // if we have a key update initial block
        // Note param has zero padded key_bytes to Spec.max_key_bytes
        if (param.hasKey) {
            this.update(param.key_bytes, 0, Blake2b.Spec.block_bytes);
        }
    }

    public static void main(String... args) {
        Blake2b mac = Mac.newInstance("LOVE".getBytes());
        final byte[] hash = mac.digest("Salaam!".getBytes());
//			Debug.dumpBuffer(System.out, "-- mac hash --", hash);
    }

    // ---------------------------------------------------------------------
    // interface: Blake2b API
    // ---------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    final public void reset() {
        // reset cache
        this.state.buflen = 0;
        for (int i = 0; i < state.buffer.length; i++) {
            state.buffer[i] = (byte) 0;
        }

        // reset flags
        this.state.f[0] = 0L;
        this.state.f[1] = 0L;

        // reset counters
        this.state.t[0] = 0L;
        this.state.t[1] = 0L;

        // reset state vector
        // NOTE: keep as last stmt as init calls update0 for MACs.
        initialize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public void update(final byte[] b, int off, int len) {
        if (b == null) {
            throw new IllegalArgumentException("input buffer (b) is null");
        }
        /* zero or more calls to compress */
        final long[] t = state.t;
        final byte[] buffer = state.buffer;
        while (len > 0) {
            if (state.buflen == 0) {
                /* try compressing direct from input ? */
                while (len > Blake2b.Spec.block_bytes) {
                    t[0] += Blake2b.Spec.block_bytes;
                    t[1] += (t[0] < 0 && state.buflen > -t[0]) ? 1 : 0;
                    compress(b, off);
                    len -= Blake2b.Spec.block_bytes;
                    off += Blake2b.Spec.block_bytes;
                }
            } else if (state.buflen == Blake2b.Spec.block_bytes) {
                /* flush */
                t[0] += Blake2b.Spec.block_bytes;
                t[1] += t[0] == 0 ? 1 : 0;
                compress(buffer, 0);
                state.buflen = 0;
                continue;
            }

            // "are we there yet?"
            if (len == 0) {
                return;
            }

            final int cap = Blake2b.Spec.block_bytes - state.buflen;
            final int fill = len > cap ? cap : len;
            System.arraycopy(b, off, buffer, state.buflen, fill);
            state.buflen += fill;
            len -= fill;
            off += fill;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public void update(byte b) {
        oneByte[0] = b;
        update(oneByte, 0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public void update(byte[] input) {
        update(input, 0, input.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public void digest(byte[] output, int off, int len) {
        // zero pad last block; set last block flags; and compress
        System.arraycopy(zeropad, 0, state.buffer, state.buflen, Blake2b.Spec.block_bytes - state.buflen);
        if (state.buflen > 0) {
            this.state.t[0] += state.buflen;
            this.state.t[1] += this.state.t[0] == 0 ? 1 : 0;
        }

        this.state.f[flag.last_block] = 0xFFFFFFFFFFFFFFFFL;
        this.state.f[flag.last_node] = this.state.last_node ? 0xFFFFFFFFFFFFFFFFL : 0x0L;

        // compres and write final out (truncated to len) to output
        compress(state.buffer, 0);
        haShut(output, off, len);

        reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public byte[] digest() throws IllegalArgumentException {
        final byte[] out = new byte[state.outlen];
        digest(out, 0, state.outlen);
        return out;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public byte[] digest(byte[] input) {
        update(input, 0, input.length);
        return digest();
    }

    // ---------------------------------------------------------------------
    // Internal Ops
    // ---------------------------------------------------------------------
    /**
     * write out the digest output from the 'h' registers. truncate full output
     * if necessary.
     */
    private void haShut(final byte[] out, final int offset, final int hashlen) {
        // write max number of whole longs
        final int lcnt = hashlen >>> 3;
        long v = 0;
        int i = offset;
        final long[] h = state.h;
        for (int w = 0; w < lcnt; w++) {
            v = h[w];
            out[i] = (byte) v;
            v >>>= 8;
            out[i + 1] = (byte) v;
            v >>>= 8;
            out[i + 2] = (byte) v;
            v >>>= 8;
            out[i + 3] = (byte) v;
            v >>>= 8;
            out[i + 4] = (byte) v;
            v >>>= 8;
            out[i + 5] = (byte) v;
            v >>>= 8;
            out[i + 6] = (byte) v;
            v >>>= 8;
            out[i + 7] = (byte) v;
            i += 8;
        }

        // basta?
        if (hashlen == Blake2b.Spec.max_digest_bytes) {
            return;
        }

        // write the remaining bytes of a partial long value
        v = h[lcnt];
        i = lcnt << 3;
        while (i < hashlen) {
            out[offset + i] = (byte) v;
            v >>>= 8;
            ++i;
        }
    }

    ////////////////////////////////////////////////////////////////////////
    /// Compression Kernel /////////////////////////////////////////// BEGIN
    ////////////////////////////////////////////////////////////////////////
    /**
     * compress Spec.block_bytes data from b, from offset
     */
    private void compress(final byte[] b, final int offset) {

        // set m registers
        final long[] m = state.m;
        m[0] = LittleEndian.readLong(b, offset);
        m[1] = LittleEndian.readLong(b, offset + 8);
        m[2] = LittleEndian.readLong(b, offset + 16);
        m[3] = LittleEndian.readLong(b, offset + 24);
        m[4] = LittleEndian.readLong(b, offset + 32);
        m[5] = LittleEndian.readLong(b, offset + 40);
        m[6] = LittleEndian.readLong(b, offset + 48);
        m[7] = LittleEndian.readLong(b, offset + 56);
        m[8] = LittleEndian.readLong(b, offset + 64);
        m[9] = LittleEndian.readLong(b, offset + 72);
        m[10] = LittleEndian.readLong(b, offset + 80);
        m[11] = LittleEndian.readLong(b, offset + 88);
        m[12] = LittleEndian.readLong(b, offset + 96);
        m[13] = LittleEndian.readLong(b, offset + 104);
        m[14] = LittleEndian.readLong(b, offset + 112);
        m[15] = LittleEndian.readLong(b, offset + 120);

        // set v registers
        final long[] v = state.v;
        final long[] h = state.h;
        final long[] t = state.t;
        final long[] f = state.f;
        v[0] = h[0];
        v[1] = h[1];
        v[2] = h[2];
        v[3] = h[3];
        v[4] = h[4];
        v[5] = h[5];
        v[6] = h[6];
        v[7] = h[7];
        v[8] = 0x6a09e667f3bcc908L;
        v[9] = 0xbb67ae8584caa73bL;
        v[10] = 0x3c6ef372fe94f82bL;
        v[11] = 0xa54ff53a5f1d36f1L;
        v[12] = t[0] ^ 0x510e527fade682d1L;
        v[13] = t[1] ^ 0x9b05688c2b3e6c1fL;
        v[14] = f[0] ^ 0x1f83d9abfb41bd6bL;
        v[15] = f[1] ^ 0x5be0cd19137e2179L;

        // do the rounds
        round_0(v, m);
        round_1(v, m);
        round_2(v, m);
        round_3(v, m);
        round_4(v, m);
        round_5(v, m);
        round_6(v, m);
        round_7(v, m);
        round_8(v, m);
        round_9(v, m);
        round_0(v, m); // round 10 is identical to round 0
        round_1(v, m); // round 11 is identical to round 1

        // Update state vector h
        h[0] ^= v[0] ^ v[8];
        h[1] ^= v[1] ^ v[9];
        h[2] ^= v[2] ^ v[10];
        h[3] ^= v[3] ^ v[11];
        h[4] ^= v[4] ^ v[12];
        h[5] ^= v[5] ^ v[13];
        h[6] ^= v[6] ^ v[14];
        h[7] ^= v[7] ^ v[15];

        /* kaamil */
    }

    private void round_0(final long[] v, final long[] m) {
        v[0] = v[0] + v[4] + m[0];
        v[12] ^= v[0];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[0] = v[0] + v[4] + m[1];
        v[12] ^= v[0];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] << 1) | (v[4] >>> 63);

        v[1] = v[1] + v[5] + m[2];
        v[13] ^= v[1];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[1] = v[1] + v[5] + m[3];
        v[13] ^= v[1];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[2] = v[2] + v[6] + m[4];
        v[14] ^= v[2];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[2] = v[2] + v[6] + m[5];
        v[14] ^= v[2];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[3] = v[3] + v[7] + m[6];
        v[15] ^= v[3];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[3] = v[3] + v[7] + m[7];
        v[15] ^= v[3];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[0] = v[0] + v[5] + m[8];
        v[15] ^= v[0];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[0] = v[0] + v[5] + m[9];
        v[15] ^= v[0];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[1] = v[1] + v[6] + m[10];
        v[12] ^= v[1];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[1] = v[1] + v[6] + +m[11];
        v[12] ^= v[1];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[2] = v[2] + v[7] + m[12];
        v[13] ^= v[2];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[2] = v[2] + v[7] + m[13];
        v[13] ^= v[2];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[3] = v[3] + v[4] + m[14];
        v[14] ^= v[3];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[3] = v[3] + v[4] + m[15];
        v[14] ^= v[3];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] << 1) | (v[4] >>> 63);
    }

    private void round_1(final long[] v, final long[] m) {
        v[0] = v[0] + v[4] + m[14];
        v[12] ^= v[0];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[0] = v[0] + v[4] + m[10];
        v[12] ^= v[0];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] << 1) | (v[4] >>> 63);

        v[1] = v[1] + v[5] + m[4];
        v[13] ^= v[1];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[1] = v[1] + v[5] + m[8];
        v[13] ^= v[1];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[2] = v[2] + v[6] + m[9];
        v[14] ^= v[2];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[2] = v[2] + v[6] + m[15];
        v[14] ^= v[2];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[3] = v[3] + v[7] + m[13];
        v[15] ^= v[3];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[3] = v[3] + v[7] + m[6];
        v[15] ^= v[3];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[0] = v[0] + v[5] + m[1];
        v[15] ^= v[0];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[0] = v[0] + v[5] + m[12];
        v[15] ^= v[0];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[1] = v[1] + v[6] + m[0];
        v[12] ^= v[1];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[1] = v[1] + v[6] + +m[2];
        v[12] ^= v[1];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[2] = v[2] + v[7] + m[11];
        v[13] ^= v[2];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[2] = v[2] + v[7] + m[7];
        v[13] ^= v[2];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[3] = v[3] + v[4] + m[5];
        v[14] ^= v[3];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[3] = v[3] + v[4] + m[3];
        v[14] ^= v[3];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] << 1) | (v[4] >>> 63);
    }

    private void round_2(final long[] v, final long[] m) {
        v[0] = v[0] + v[4] + m[11];
        v[12] ^= v[0];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[0] = v[0] + v[4] + m[8];
        v[12] ^= v[0];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] << 1) | (v[4] >>> 63);

        v[1] = v[1] + v[5] + m[12];
        v[13] ^= v[1];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[1] = v[1] + v[5] + m[0];
        v[13] ^= v[1];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[2] = v[2] + v[6] + m[5];
        v[14] ^= v[2];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[2] = v[2] + v[6] + m[2];
        v[14] ^= v[2];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[3] = v[3] + v[7] + m[15];
        v[15] ^= v[3];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[3] = v[3] + v[7] + m[13];
        v[15] ^= v[3];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[0] = v[0] + v[5] + m[10];
        v[15] ^= v[0];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[0] = v[0] + v[5] + m[14];
        v[15] ^= v[0];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[1] = v[1] + v[6] + m[3];
        v[12] ^= v[1];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[1] = v[1] + v[6] + +m[6];
        v[12] ^= v[1];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[2] = v[2] + v[7] + m[7];
        v[13] ^= v[2];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[2] = v[2] + v[7] + m[1];
        v[13] ^= v[2];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[3] = v[3] + v[4] + m[9];
        v[14] ^= v[3];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[3] = v[3] + v[4] + m[4];
        v[14] ^= v[3];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] << 1) | (v[4] >>> 63);
    }

    private void round_3(final long[] v, final long[] m) {
        v[0] = v[0] + v[4] + m[7];
        v[12] ^= v[0];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[0] = v[0] + v[4] + m[9];
        v[12] ^= v[0];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] << 1) | (v[4] >>> 63);

        v[1] = v[1] + v[5] + m[3];
        v[13] ^= v[1];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[1] = v[1] + v[5] + m[1];
        v[13] ^= v[1];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[2] = v[2] + v[6] + m[13];
        v[14] ^= v[2];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[2] = v[2] + v[6] + m[12];
        v[14] ^= v[2];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[3] = v[3] + v[7] + m[11];
        v[15] ^= v[3];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[3] = v[3] + v[7] + m[14];
        v[15] ^= v[3];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[0] = v[0] + v[5] + m[2];
        v[15] ^= v[0];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[0] = v[0] + v[5] + m[6];
        v[15] ^= v[0];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[1] = v[1] + v[6] + m[5];
        v[12] ^= v[1];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[1] = v[1] + v[6] + +m[10];
        v[12] ^= v[1];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[2] = v[2] + v[7] + m[4];
        v[13] ^= v[2];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[2] = v[2] + v[7] + m[0];
        v[13] ^= v[2];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[3] = v[3] + v[4] + m[15];
        v[14] ^= v[3];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[3] = v[3] + v[4] + m[8];
        v[14] ^= v[3];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] << 1) | (v[4] >>> 63);
    }

    private void round_4(final long[] v, final long[] m) {
        v[0] = v[0] + v[4] + m[9];
        v[12] ^= v[0];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[0] = v[0] + v[4] + m[0];
        v[12] ^= v[0];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] << 1) | (v[4] >>> 63);

        v[1] = v[1] + v[5] + m[5];
        v[13] ^= v[1];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[1] = v[1] + v[5] + m[7];
        v[13] ^= v[1];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[2] = v[2] + v[6] + m[2];
        v[14] ^= v[2];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[2] = v[2] + v[6] + m[4];
        v[14] ^= v[2];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[3] = v[3] + v[7] + m[10];
        v[15] ^= v[3];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[3] = v[3] + v[7] + m[15];
        v[15] ^= v[3];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[0] = v[0] + v[5] + m[14];
        v[15] ^= v[0];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[0] = v[0] + v[5] + m[1];
        v[15] ^= v[0];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[1] = v[1] + v[6] + m[11];
        v[12] ^= v[1];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[1] = v[1] + v[6] + +m[12];
        v[12] ^= v[1];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[2] = v[2] + v[7] + m[6];
        v[13] ^= v[2];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[2] = v[2] + v[7] + m[8];
        v[13] ^= v[2];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[3] = v[3] + v[4] + m[3];
        v[14] ^= v[3];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[3] = v[3] + v[4] + m[13];
        v[14] ^= v[3];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] << 1) | (v[4] >>> 63);
    }

    private void round_5(final long[] v, final long[] m) {
        v[0] = v[0] + v[4] + m[2];
        v[12] ^= v[0];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[0] = v[0] + v[4] + m[12];
        v[12] ^= v[0];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] << 1) | (v[4] >>> 63);

        v[1] = v[1] + v[5] + m[6];
        v[13] ^= v[1];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[1] = v[1] + v[5] + m[10];
        v[13] ^= v[1];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[2] = v[2] + v[6] + m[0];
        v[14] ^= v[2];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[2] = v[2] + v[6] + m[11];
        v[14] ^= v[2];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[3] = v[3] + v[7] + m[8];
        v[15] ^= v[3];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[3] = v[3] + v[7] + m[3];
        v[15] ^= v[3];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[0] = v[0] + v[5] + m[4];
        v[15] ^= v[0];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[0] = v[0] + v[5] + m[13];
        v[15] ^= v[0];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[1] = v[1] + v[6] + m[7];
        v[12] ^= v[1];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[1] = v[1] + v[6] + +m[5];
        v[12] ^= v[1];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[2] = v[2] + v[7] + m[15];
        v[13] ^= v[2];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[2] = v[2] + v[7] + m[14];
        v[13] ^= v[2];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[3] = v[3] + v[4] + m[1];
        v[14] ^= v[3];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[3] = v[3] + v[4] + m[9];
        v[14] ^= v[3];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] << 1) | (v[4] >>> 63);
    }

    private void round_6(final long[] v, final long[] m) {
        v[0] = v[0] + v[4] + m[12];
        v[12] ^= v[0];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[0] = v[0] + v[4] + m[5];
        v[12] ^= v[0];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] << 1) | (v[4] >>> 63);

        v[1] = v[1] + v[5] + m[1];
        v[13] ^= v[1];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[1] = v[1] + v[5] + m[15];
        v[13] ^= v[1];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[2] = v[2] + v[6] + m[14];
        v[14] ^= v[2];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[2] = v[2] + v[6] + m[13];
        v[14] ^= v[2];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[3] = v[3] + v[7] + m[4];
        v[15] ^= v[3];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[3] = v[3] + v[7] + m[10];
        v[15] ^= v[3];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[0] = v[0] + v[5] + m[0];
        v[15] ^= v[0];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[0] = v[0] + v[5] + m[7];
        v[15] ^= v[0];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[1] = v[1] + v[6] + m[6];
        v[12] ^= v[1];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[1] = v[1] + v[6] + +m[3];
        v[12] ^= v[1];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[2] = v[2] + v[7] + m[9];
        v[13] ^= v[2];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[2] = v[2] + v[7] + m[2];
        v[13] ^= v[2];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[3] = v[3] + v[4] + m[8];
        v[14] ^= v[3];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[3] = v[3] + v[4] + m[11];
        v[14] ^= v[3];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] << 1) | (v[4] >>> 63);
    }

    private void round_7(final long[] v, final long[] m) {
        v[0] = v[0] + v[4] + m[13];
        v[12] ^= v[0];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[0] = v[0] + v[4] + m[11];
        v[12] ^= v[0];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] << 1) | (v[4] >>> 63);

        v[1] = v[1] + v[5] + m[7];
        v[13] ^= v[1];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[1] = v[1] + v[5] + m[14];
        v[13] ^= v[1];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[2] = v[2] + v[6] + m[12];
        v[14] ^= v[2];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[2] = v[2] + v[6] + m[1];
        v[14] ^= v[2];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[3] = v[3] + v[7] + m[3];
        v[15] ^= v[3];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[3] = v[3] + v[7] + m[9];
        v[15] ^= v[3];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[0] = v[0] + v[5] + m[5];
        v[15] ^= v[0];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[0] = v[0] + v[5] + m[0];
        v[15] ^= v[0];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[1] = v[1] + v[6] + m[15];
        v[12] ^= v[1];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[1] = v[1] + v[6] + +m[4];
        v[12] ^= v[1];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[2] = v[2] + v[7] + m[8];
        v[13] ^= v[2];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[2] = v[2] + v[7] + m[6];
        v[13] ^= v[2];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[3] = v[3] + v[4] + m[2];
        v[14] ^= v[3];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[3] = v[3] + v[4] + m[10];
        v[14] ^= v[3];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] << 1) | (v[4] >>> 63);
    }

    private void round_8(final long[] v, final long[] m) {
        v[0] = v[0] + v[4] + m[6];
        v[12] ^= v[0];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[0] = v[0] + v[4] + m[15];
        v[12] ^= v[0];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] << 1) | (v[4] >>> 63);

        v[1] = v[1] + v[5] + m[14];
        v[13] ^= v[1];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[1] = v[1] + v[5] + m[9];
        v[13] ^= v[1];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[2] = v[2] + v[6] + m[11];
        v[14] ^= v[2];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[2] = v[2] + v[6] + m[3];
        v[14] ^= v[2];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[3] = v[3] + v[7] + m[0];
        v[15] ^= v[3];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[3] = v[3] + v[7] + m[8];
        v[15] ^= v[3];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[0] = v[0] + v[5] + m[12];
        v[15] ^= v[0];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[0] = v[0] + v[5] + m[2];
        v[15] ^= v[0];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[1] = v[1] + v[6] + m[13];
        v[12] ^= v[1];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[1] = v[1] + v[6] + +m[7];
        v[12] ^= v[1];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[2] = v[2] + v[7] + m[1];
        v[13] ^= v[2];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[2] = v[2] + v[7] + m[4];
        v[13] ^= v[2];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[3] = v[3] + v[4] + m[10];
        v[14] ^= v[3];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[3] = v[3] + v[4] + m[5];
        v[14] ^= v[3];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] << 1) | (v[4] >>> 63);
    }

    private void round_9(final long[] v, final long[] m) {
        v[0] = v[0] + v[4] + m[10];
        v[12] ^= v[0];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[0] = v[0] + v[4] + m[2];
        v[12] ^= v[0];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[8] = v[8] + v[12];
        v[4] ^= v[8];
        v[4] = (v[4] << 1) | (v[4] >>> 63);

        v[1] = v[1] + v[5] + m[8];
        v[13] ^= v[1];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[1] = v[1] + v[5] + m[4];
        v[13] ^= v[1];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[9] = v[9] + v[13];
        v[5] ^= v[9];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[2] = v[2] + v[6] + m[7];
        v[14] ^= v[2];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[2] = v[2] + v[6] + m[6];
        v[14] ^= v[2];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[10] = v[10] + v[14];
        v[6] ^= v[10];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[3] = v[3] + v[7] + m[1];
        v[15] ^= v[3];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[3] = v[3] + v[7] + m[5];
        v[15] ^= v[3];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[11] = v[11] + v[15];
        v[7] ^= v[11];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[0] = v[0] + v[5] + m[15];
        v[15] ^= v[0];
        v[15] = (v[15] << 32) | (v[15] >>> 32);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] >>> 24) | (v[5] << 40);
        v[0] = v[0] + v[5] + m[11];
        v[15] ^= v[0];
        v[15] = (v[15] >>> 16) | (v[15] << 48);
        v[10] = v[10] + v[15];
        v[5] ^= v[10];
        v[5] = (v[5] << 1) | (v[5] >>> 63);

        v[1] = v[1] + v[6] + m[9];
        v[12] ^= v[1];
        v[12] = (v[12] << 32) | (v[12] >>> 32);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] >>> 24) | (v[6] << 40);
        v[1] = v[1] + v[6] + +m[14];
        v[12] ^= v[1];
        v[12] = (v[12] >>> 16) | (v[12] << 48);
        v[11] = v[11] + v[12];
        v[6] ^= v[11];
        v[6] = (v[6] << 1) | (v[6] >>> 63);

        v[2] = v[2] + v[7] + m[3];
        v[13] ^= v[2];
        v[13] = (v[13] << 32) | (v[13] >>> 32);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] >>> 24) | (v[7] << 40);
        v[2] = v[2] + v[7] + m[12];
        v[13] ^= v[2];
        v[13] = (v[13] >>> 16) | (v[13] << 48);
        v[8] = v[8] + v[13];
        v[7] ^= v[8];
        v[7] = (v[7] << 1) | (v[7] >>> 63);

        v[3] = v[3] + v[4] + m[13];
        v[14] ^= v[3];
        v[14] = (v[14] << 32) | (v[14] >>> 32);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] >>> 24) | (v[4] << 40);
        v[3] = v[3] + v[4] + m[0];
        v[14] ^= v[3];
        v[14] = (v[14] >>> 16) | (v[14] << 48);
        v[9] = v[9] + v[14];
        v[4] ^= v[9];
        v[4] = (v[4] << 1) | (v[4] >>> 63);
    }

    ////////////////////////////////////////////////////////////////////////
    /// Compression Kernel //////////////////////////////////////////// FINI
    ////////////////////////////////////////////////////////////////////////

    /* TEMP - remove at will */
    public static class Debug {

        public static void dumpState(Engine e, final String mark) {
            System.out.format("-- MARK == @ %s @ ===========\n", mark);
            dumpArray("register t", e.state.t);
            dumpArray("register h", e.state.h);
            dumpArray("register f", e.state.f);
            dumpArray("register offset", new long[]{e.state.buflen});
            System.out.format("-- END MARK =================\n");
        }

        public static void dumpArray(final String label, final long[] b) {
            System.out.format("-- %s -- :\n{\n", label);
            for (int j = 0; j < b.length; ++j) {
                System.out.format("    [%2d] : %016X\n", j, b[j]);
            }
            System.out.format("}\n");
        }

        public static void dumpBuffer(final PrintStream out, final String label, final byte[] b) {
            dumpBuffer(out, label, b, 0, b.length);
        }

        public static void dumpBuffer(final PrintStream out, final byte[] b) {
            dumpBuffer(out, null, b, 0, b.length);
        }

        public static void dumpBuffer(final PrintStream out, final byte[] b, final int offset, final int len) {
            dumpBuffer(out, null, b, offset, len);
        }

        public static void dumpBuffer(final PrintStream out, final String label, final byte[] b, final int offset, final int len) {
            if (label != null) {
                out.format("-- %s -- :\n", label);
            }
            out.format("{\n    ", label);
            for (int j = 0; j < len; ++j) {
                out.format("%02X", b[j + offset]);
                if (j + 1 < len) {
                    if ((j + 1) % 8 == 0) {
                        out.print("\n    ");
                    } else {
                        out.print(' ');
                    }
                }
            }
            out.format("\n}\n");
        }
    }

    /* TEMP - remove at will */

    // ---------------------------------------------------------------------
    // Helper for assert error messages
    // ---------------------------------------------------------------------
    public static final class Assert {

        public final static String exclusiveUpperBound = "'%s' %d is >= %d";
        public final static String inclusiveUpperBound = "'%s' %d is > %d";
        public final static String exclusiveLowerBound = "'%s' %d is <= %d";
        public final static String inclusiveLowerBound = "'%s' %d is < %d";

        static <T extends Number> String assertFail(final String name, final T v, final String err, final T spec) {
            new Exception().printStackTrace();
            return String.format(err, name, v, spec);
        }
    }
    // ---------------------------------------------------------------------
    // Little Endian Codecs (inlined in the compressor)

    /*
         * impl note: these are not library funcs and used in hot loops, so no
         * null or bounds checks are performed. For our purposes, this is OK.
     */
    // ---------------------------------------------------------------------

    public static class LittleEndian {

        private static final byte[] hex_digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        private static final byte[] HEX_digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        /**
         * @return hex rep of byte (lower case).
         */
        static public String toHexStr(final byte[] b) {
            return toHexStr(b, false); // because String class is slower.
        }

        static public String toHexStr(final byte[] b, boolean upperCase) {
            final int len = b.length;
            final byte[] digits = new byte[len * 2];
            final byte[] hex_rep = upperCase ? HEX_digits : hex_digits;
            for (int i = 0; i < len; i++) {
                digits[i * 2] = hex_rep[(byte) (b[i] >> 4 & 0x0F)];
                digits[i * 2 + 1] = hex_rep[(byte) (b[i] & 0x0F)];
            }
            return new String(digits);
        }

        public static int readInt(final byte[] b, final int off) {
            int v0 = ((int) b[off] & 0xFF);
            v0 |= ((int) b[off + 1] & 0xFF) << 8;
            v0 |= ((int) b[off + 2] & 0xFF) << 16;
            return v0 | ((int) b[off + 3]) << 24;
        }

        /**
         * Little endian - byte[] to long
         */
        public static long readLong(final byte[] b, final int off) {
            long v0 = ((long) b[off] & 0xFF);
            v0 |= ((long) b[off + 1] & 0xFF) << 8;
            v0 |= ((long) b[off + 2] & 0xFF) << 16;
            v0 |= ((long) b[off + 3] & 0xFF) << 24;
            v0 |= ((long) b[off + 4] & 0xFF) << 32;
            v0 |= ((long) b[off + 5] & 0xFF) << 40;
            v0 |= ((long) b[off + 6] & 0xFF) << 48;
            return v0 | ((long) b[off + 7]) << 56;
        }

        /**
         * Little endian - long to byte[]
         */
        public static void writeLong(long v, final byte[] b, final int off) {
            b[off] = (byte) v;
            v >>>= 8;
            b[off + 1] = (byte) v;
            v >>>= 8;
            b[off + 2] = (byte) v;
            v >>>= 8;
            b[off + 3] = (byte) v;
            v >>>= 8;
            b[off + 4] = (byte) v;
            v >>>= 8;
            b[off + 5] = (byte) v;
            v >>>= 8;
            b[off + 6] = (byte) v;
            v >>>= 8;
            b[off + 7] = (byte) v;
        }

        /**
         * Little endian - int to byte[]
         */
        public static void writeInt(int v, final byte[] b, final int off) {
            b[off] = (byte) v;
            v >>>= 8;
            b[off + 1] = (byte) v;
            v >>>= 8;
            b[off + 2] = (byte) v;
            v >>>= 8;
            b[off + 3] = (byte) v;
        }
    }
}
