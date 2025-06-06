/*
 * Copyright (c) 2020, 2025, Oracle and/or its affiliates.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License, version 2.0, as published by
 * the Free Software Foundation.
 *
 * This program is designed to work with certain software that is licensed under separate terms, as designated in a particular file or component or in
 * included license documentation. The authors of MySQL hereby grant you an additional permission to link the program and your derivative works with the
 * separately licensed software that they have either included with the program or referenced in the documentation.
 *
 * Without limiting anything contained in the foregoing, this file, which is part of MySQL Connector/J, is also subject to the Universal FOSS Exception,
 * version 1.0, a copy of which can be found at http://oss.oracle.com/licenses/universal-foss-exception.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License, version 2.0, for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package com.mysql.cj.sasl;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

import com.mysql.cj.exceptions.ExceptionFactory;

/**
 * A {@link SaslClient} implementation for SCRAM-SHA-1, as specified in <a href="https://tools.ietf.org/html/rfc5802">RFC 5802</a>.
 *
 * The IANA-registered mechanism was renamed to "MYSQLCJ-SCRAM-SHA-1" in order to avoid future conflicts with an officially supported implementation.
 * When there is a Java-supported implementation for SCRAM-SHA-1, it will have to be thoroughly tested with Connector/J and if that works, this code can be
 * obsoleted.
 */
public class ScramSha1SaslClient extends ScramShaSaslClient {

    public static final String IANA_MECHANISM_NAME = "SCRAM-SHA-1";
    public static final String MECHANISM_NAME = "MYSQLCJ-" + IANA_MECHANISM_NAME;

    private static final String SHA1_ALGORITHM = "SHA-1";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String PBKCF2_HMAC_SHA1_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int SHA1_HASH_LENGTH = 20; // SHA-1 produces 20 Bytes long hashes.

    public ScramSha1SaslClient(String authorizationId, String authenticationId, String password) throws SaslException {
        super(authorizationId, authenticationId, password);
    }

    @Override
    String getIanaMechanismName() {
        return IANA_MECHANISM_NAME;
    }

    @Override
    public String getMechanismName() {
        return MECHANISM_NAME;
    }

    /**
     * The "H(str)" cryptographic hash function as described in <a href="https://tools.ietf.org/html/rfc5802#section-2.2">RFC 5802, Section 2.2</a>. This
     * implementation corresponds to SHA-1.
     *
     * @param str
     *            the string to hash.
     * @return
     *         the hash value of the given string.
     */
    @Override
    byte[] h(byte[] str) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance(SHA1_ALGORITHM);
            return sha1.digest(str);
        } catch (NoSuchAlgorithmException e) {
            throw ExceptionFactory.createException("Failed computing authentication hashes", e);
        }
    }

    /**
     * The "HMAC(key, str)" HMAC keyed hash algorithm as described in <a href="https://tools.ietf.org/html/rfc5802#section-2.2">RFC 5802, Section 2.2</a>.
     * This implementation corresponds to 'HmacSHA1'.
     *
     * @param key
     *            the hash key.
     * @param str
     *            the input string.
     * @return
     *         the hashed value of the given params.
     */
    @Override
    byte[] hmac(byte[] key, byte[] str) {
        Mac hmacSha1;
        try {
            hmacSha1 = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            hmacSha1.init(new SecretKeySpec(key, HMAC_SHA1_ALGORITHM));

            return hmacSha1.doFinal(str);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw ExceptionFactory.createException("Failed computing authentication hashes", e);
        }
    }

    /**
     * The "Hi(str, salt, i)" PBKDF2 function as described in <a href="https://tools.ietf.org/html/rfc5802#section-2.2">RFC 5802, Section 2.2</a>.
     * This implementation corresponds to 'PBKDF2WithHmacSHA1'.
     *
     * @param str
     *            the string value to use as the internal HMAC key.
     * @param salt
     *            the input string to hash in the initial iteration.
     * @param iterations
     *            the number of iterations to run the algorithm.
     *
     * @return
     *         an hash value with an output length equal to the length of H(str).
     */
    @Override
    byte[] hi(String str, byte[] salt, int iterations) {
        KeySpec spec = new PBEKeySpec(str.toCharArray(), salt, iterations, SHA1_HASH_LENGTH * 8);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKCF2_HMAC_SHA1_ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw ExceptionFactory.createException(e.getMessage());
        }
    }

}
