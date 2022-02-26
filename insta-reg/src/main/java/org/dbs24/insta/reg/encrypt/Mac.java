/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.encrypt;

import java.security.Key;

/**
 *
 * @author kdg
 */
public class Mac extends Engine implements Blake2b {

    public Mac(final Param p, Engine.State state) {
        super(state, p);
    }

    public Mac(final Param p) {
        super(p);
    }

    public Mac() {
        super();
    }

    /**
     * Mac 512 - using default Blake2b.Spec settings with given key
     */
    public static Mac newInstance(final byte[] key) {
        return new Mac(new Param().setKey(key));
    }

    /**
     * Mac - using default Blake2b.Spec settings with given key, with
     * given digest length
     */
    public static Mac newInstance(final byte[] key, final int digestLength) {
        return new Mac(new Param().setKey(key).setDigestLength(digestLength));
    }

    /**
     * Mac - using default Blake2b.Spec settings with given
     * java.security.Key, with given digest length
     */
    public static Mac newInstance(final Key key, final int digestLength) {
        return new Mac(new Param().setKey(key).setDigestLength(digestLength));
    }

    /**
     * Mac - using the specified Parameters.
     *
     * @param p asserted valid configured Param with key
     */
    public static Mac newInstance(Param p) {
        assert p != null : "Param (p) is null";
        assert p.hasKey() : "Param (p) not configured with a key";
        return new Mac(p);
    }
}
