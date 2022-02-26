/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.encrypt;

/**
 *
 * @author kdg
 */
public final class Digest extends Engine implements Blake2b {

    public Digest(final Param p) {
        super(p);
    }

    public Digest() {
        super();
    }

    public Digest(Param p, State state) {
        super(state, p);
    }

    public static Digest newInstance() {
        return new Digest();
    }

    public static Digest newInstance(final int digestLength) {
        return new Digest(new Param().setDigestLength(digestLength));
    }

    public static Digest newInstance(Param p) {
        return new Digest(p);
    }

    public static Digest newInstance(Param p, State state) {
        return new Digest(p, state);
    }
}

