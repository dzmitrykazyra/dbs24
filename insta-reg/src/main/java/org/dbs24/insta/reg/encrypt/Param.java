/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.encrypt;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import static org.dbs24.insta.reg.encrypt.Engine.Assert.*;
import static org.dbs24.insta.reg.encrypt.Engine.LittleEndian.*;

public class Param implements AlgorithmParameterSpec {

    interface Xoff {

        int digest_length = 0;
        int key_length = 1;
        int fanout = 2;
        int depth = 3;
        int leaf_length = 4;
        int node_offset = 8;
        int node_depth = 16;
        int inner_length = 17;
        int reserved = 18;
        int salt = 32;
        int personal = 48;
    }

    public interface Default {

        byte digest_length = Blake2b.Spec.max_digest_bytes;
        byte key_length = 0;
        byte fanout = 1;
        byte depth = 1;
        int leaf_length = 0;
        long node_offset = 0;
        byte node_depth = 0;
        byte inner_length = 0;
    }

    /**
     * default bytes of Blake2b parameter block
     */
    final static byte[] default_bytes = new byte[Blake2b.Spec.param_bytes];

    /**
     * initialize default_bytes
     */
    static {
        default_bytes[Xoff.digest_length] = Default.digest_length;
        default_bytes[Xoff.key_length] = Default.key_length;
        default_bytes[Xoff.fanout] = Default.fanout;
        default_bytes[Xoff.depth] = Default.depth;
        /* def. leaf_length is 0 fill and already set by new byte[] */
 /* def. node_offset is 0 fill and already set by new byte[] */
        default_bytes[Xoff.node_depth] = Default.node_depth;
        default_bytes[Xoff.inner_length] = Default.inner_length;
        /* def. salt is 0 fill and already set by new byte[] */
 /* def. personal is 0 fill and already set by new byte[] */
    }

    /**
     * default Blake2b h vector
     */
    final static long[] default_h = new long[Blake2b.Spec.state_space_len];

    static {
        default_h[0] = readLong(default_bytes, 0);
        default_h[1] = readLong(default_bytes, 8);
        default_h[2] = readLong(default_bytes, 16);
        default_h[3] = readLong(default_bytes, 24);
        default_h[4] = readLong(default_bytes, 32);
        default_h[5] = readLong(default_bytes, 40);
        default_h[6] = readLong(default_bytes, 48);
        default_h[7] = readLong(default_bytes, 56);

        default_h[0] ^= Blake2b.Spec.IV[0];
        default_h[1] ^= Blake2b.Spec.IV[1];
        default_h[2] ^= Blake2b.Spec.IV[2];
        default_h[3] ^= Blake2b.Spec.IV[3];
        default_h[4] ^= Blake2b.Spec.IV[4];
        default_h[5] ^= Blake2b.Spec.IV[5];
        default_h[6] ^= Blake2b.Spec.IV[6];
        default_h[7] ^= Blake2b.Spec.IV[7];
    }

    /**
     *
     */
    public boolean hasKey = false;
    /**
     * not sure how to make this secure - TODO
     */
    public byte[] key_bytes = null;
    /**
     *
     */
    public byte[] bytes = null;
    /**
     *
     */
    public final long[] h = new long[Blake2b.Spec.state_space_len];

    /**
     *
     */
    public Param() {
        System.arraycopy(default_h, 0, h, 0, Blake2b.Spec.state_space_len);
    }

    /**
     *
     */
    public long[] initialized_H() {
        return h;
    }

    /**
     * package only - copy returned - do not use in functional loops
     */
    public byte[] getBytes() {
        lazyInitBytes();
        byte[] copy = new byte[bytes.length];
        System.arraycopy(bytes, 0, copy, 0, bytes.length);
        return copy;
    }

    final byte getByteParam(final int xoffset) {
        byte[] _bytes = bytes;
        if (_bytes == null) {
            _bytes = Param.default_bytes;
        }
        return _bytes[xoffset];
    }

    final int getIntParam(final int xoffset) {
        byte[] _bytes = bytes;
        if (_bytes == null) {
            _bytes = Param.default_bytes;
        }
        return readInt(_bytes, xoffset);
    }

    final long getLongParam(final int xoffset) {
        byte[] _bytes = bytes;
        if (_bytes == null) {
            _bytes = Param.default_bytes;
        }
        return readLong(_bytes, xoffset);
    }

    // TODO same for tree params depth, fanout, inner, node-depth, node-offset
    public final int getDigestLength() {
        return getByteParam(Xoff.digest_length);
    }

    public final int getKeyLength() {
        return getByteParam(Xoff.key_length);
    }

    public final int getFanout() {
        return getByteParam(Xoff.fanout);
    }

    public final int getDepth() {
        return getByteParam(Xoff.depth);
    }

    public final int getLeafLength() {
        return getIntParam(Xoff.leaf_length);
    }

    public final long getNodeOffset() {
        return getLongParam(Xoff.node_offset);
    }

    public final int getNodeDepth() {
        return getByteParam(Xoff.node_depth);
    }

    public final int getInnerLength() {
        return getByteParam(Xoff.inner_length);
    }

    public final boolean hasKey() {
        return this.hasKey;
    }

    @Override
    public Param clone() {
        final Param clone = new Param();
        System.arraycopy(this.h, 0, clone.h, 0, h.length);
        clone.lazyInitBytes();
        System.arraycopy(this.bytes, 0, clone.bytes, 0, this.bytes.length);

        if (this.hasKey) {
            clone.hasKey = this.hasKey;
            clone.key_bytes = new byte[Blake2b.Spec.max_key_bytes * 2];
            System.arraycopy(this.key_bytes, 0, clone.key_bytes, 0, this.key_bytes.length);
        }
        return clone;
    }

    ////////////////////////////////////////////////////////////////////////
    /// lazy setters - write directly to the bytes image of param block ////
    ////////////////////////////////////////////////////////////////////////
    final void lazyInitBytes() {
        if (bytes == null) {
            bytes = new byte[Blake2b.Spec.param_bytes];
            System.arraycopy(Param.default_bytes, 0, bytes, 0, Blake2b.Spec.param_bytes);
        }
    }

    /* 0-7 inclusive */
    public final Param setDigestLength(int len) {
        assert len > 0 : assertFail("len", len, exclusiveLowerBound, 0);
        assert len <= Blake2b.Spec.max_digest_bytes : assertFail("len", len, inclusiveUpperBound, Blake2b.Spec.max_digest_bytes);

        lazyInitBytes();
        bytes[Xoff.digest_length] = (byte) len;
        h[0] = readLong(bytes, 0);
        h[0] ^= Blake2b.Spec.IV[0];
        return this;
    }

    public final Param setKey(final Key key) {
        assert key != null : "key is null";
        final byte[] keybytes = key.getEncoded();
        assert keybytes != null : "key.encoded() is null";

        return this.setKey(keybytes);
    }

    public final Param setKey(final byte[] key) {
        assert key != null : "key is null";
        assert key.length >= 0 : assertFail("key.length", key.length, inclusiveUpperBound, 0);
        assert key.length <= Blake2b.Spec.max_key_bytes : assertFail("key.length", key.length, inclusiveUpperBound, Blake2b.Spec.max_key_bytes);

        // zeropad keybytes
        this.key_bytes = new byte[Blake2b.Spec.max_key_bytes * 2];
        System.arraycopy(key, 0, this.key_bytes, 0, key.length);
        lazyInitBytes();
        bytes[Xoff.key_length] = (byte) key.length; // checked c ref; this is correct
        h[0] = readLong(bytes, 0);
        h[0] ^= Blake2b.Spec.IV[0];
        this.hasKey = true;
        return this;
    }

    public final Param setFanout(int fanout) {
        assert fanout > 0 : assertFail("fanout", fanout, exclusiveLowerBound, 0);

        lazyInitBytes();
        bytes[Xoff.fanout] = (byte) fanout;
        h[0] = readLong(bytes, 0);
        h[0] ^= Blake2b.Spec.IV[0];
        return this;
    }

    public final Param setDepth(int depth) {
        assert depth > 0 : assertFail("depth", depth, exclusiveLowerBound, 0);

        lazyInitBytes();
        bytes[Xoff.depth] = (byte) depth;
        h[0] = readLong(bytes, 0);
        h[0] ^= Blake2b.Spec.IV[0];
        return this;
    }

    public final Param setLeafLength(int leaf_length) {
        assert leaf_length >= 0 : assertFail("leaf_length", leaf_length, inclusiveLowerBound, 0);

        lazyInitBytes();
        writeInt(leaf_length, bytes, Xoff.leaf_length);
        h[0] = readLong(bytes, 0);
        h[0] ^= Blake2b.Spec.IV[0];
        return this;
    }

    /* 8-15 inclusive */
    public final Param setNodeOffset(long node_offset) {
        assert node_offset >= 0 : assertFail("node_offset", node_offset, inclusiveLowerBound, 0);

        lazyInitBytes();
        writeLong(node_offset, bytes, Xoff.node_offset);
        h[1] = readLong(bytes, Xoff.node_offset);
        h[1] ^= Blake2b.Spec.IV[1];
        return this;
    }

    /* 16-23 inclusive */
    public final Param setNodeDepth(int node_depth) {
        assert node_depth >= 0 : assertFail("node_depth", node_depth, inclusiveLowerBound, 0);

        lazyInitBytes();
        bytes[Xoff.node_depth] = (byte) node_depth;
        h[2] = readLong(bytes, Xoff.node_depth);
        h[2] ^= Blake2b.Spec.IV[2];
        h[3] = readLong(bytes, Xoff.node_depth + 8);
        h[3] ^= Blake2b.Spec.IV[3];
        return this;
    }

    public final Param setInnerLength(int inner_length) {
        assert inner_length >= 0 : assertFail("inner_length", inner_length, inclusiveLowerBound, 0);

        lazyInitBytes();
        bytes[Xoff.inner_length] = (byte) inner_length;
        h[2] = readLong(bytes, Xoff.node_depth);
        h[2] ^= Blake2b.Spec.IV[2];
        h[3] = readLong(bytes, Xoff.node_depth + 8);
        h[3] ^= Blake2b.Spec.IV[3];
        return this;
    }

    /* 24-31 masked by reserved and remain unchanged */

 /* 32-47 inclusive */
    public final Param setSalt(final byte[] salt) {
        assert salt != null : "salt is null";
        assert salt.length <= Blake2b.Spec.max_salt_bytes : assertFail("salt.length", salt.length, inclusiveUpperBound, Blake2b.Spec.max_salt_bytes);

        lazyInitBytes();
        Arrays.fill(bytes, Xoff.salt, Xoff.salt + Blake2b.Spec.max_salt_bytes, (byte) 0);
        System.arraycopy(salt, 0, bytes, Xoff.salt, salt.length);
        h[4] = readLong(bytes, Xoff.salt);
        h[4] ^= Blake2b.Spec.IV[4];
        h[5] = readLong(bytes, Xoff.salt + 8);
        h[5] ^= Blake2b.Spec.IV[5];
        return this;
    }

    /* 48-63 inclusive */
    public final Param setPersonal(byte[] personal) {
        assert personal != null : "personal is null";
        assert personal.length <= Blake2b.Spec.max_personalization_bytes : assertFail("personal.length", personal.length, inclusiveUpperBound, Blake2b.Spec.max_personalization_bytes);

        lazyInitBytes();
        Arrays.fill(bytes, Xoff.personal, Xoff.personal + Blake2b.Spec.max_personalization_bytes, (byte) 0);
        System.arraycopy(personal, 0, bytes, Xoff.personal, personal.length);
        h[6] = readLong(bytes, Xoff.personal);
        h[6] ^= Blake2b.Spec.IV[6];
        h[7] = readLong(bytes, Xoff.personal + 8);
        h[7] ^= Blake2b.Spec.IV[7];
        return this;
    }
}
