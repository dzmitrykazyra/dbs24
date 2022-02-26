/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.encrypt;

import java.io.Serializable;

public final class ResumeHandle implements Serializable {

    /**
     * per spec
     */
    public long[] h = new long[8];
    /**
     * per spec
     */
    public long[] t = new long[2];
    /**
     * per spec
     */
    public long[] f = new long[2];
    /**
     * per spec (tree)
     */
    public boolean last_node = false;
    /**
     * pulled up 2b optimal
     */
    public long[] m = new long[16];
    /**
     * pulled up 2b optimal
     */
    public long[] v = new long[16];

    /**
     * compressor cache buffer
     */
    public byte[] buffer;
    /**
     * compressor cache buffer offset/cached data length
     */
    public int buflen;

    /**
     * digest length from init param - copied here on init
     */
    public int outlen;

    public int type;

    /**
     * Create a reconstituted Black2b digest from
     *
     * @param param
     * @return
     */
    public Blake2b resume(Param param) {
        assert this.buffer != null && this.buffer.length == Blake2b.Spec.block_bytes;
        assert this.h != null && this.h.length == 8
                && this.t != null && this.t.length == 2
                && this.f != null && this.f.length == 2
                && this.m != null && this.m.length == 16
                && this.v != null && this.v.length == 16 : "Data is corrupted";
        assert this.outlen == param.getDigestLength() : "Not originally initialized from this param";
        assert this.type == 1 || this.type == 2 : "Unknown type " + this.type;
        Engine.State state = new Engine.State(outlen, this.type == 1);
        System.arraycopy(this.h, 0, state.h, 0, state.h.length);
        System.arraycopy(this.t, 0, state.t, 0, state.t.length);
        System.arraycopy(this.f, 0, state.f, 0, state.f.length);
        System.arraycopy(this.m, 0, m, 0, m.length);
        System.arraycopy(this.v, 0, v, 0, v.length);
        System.arraycopy(this.buffer, 0, state.buffer, 0, state.buffer.length);
        state.buflen = buflen;
        return type == 1 ? new Mac(param, state) : new Digest(param, state);
    }
}
