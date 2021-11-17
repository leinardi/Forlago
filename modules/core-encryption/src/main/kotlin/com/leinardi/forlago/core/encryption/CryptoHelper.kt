/*
 * Copyright 2021 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leinardi.forlago.core.encryption

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.security.crypto.MasterKeys
import com.google.crypto.tink.Aead
import com.google.crypto.tink.DeterministicAead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.PublicKeySign
import com.google.crypto.tink.PublicKeyVerify
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.daead.DeterministicAeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.signature.SignatureConfig
import com.google.crypto.tink.subtle.Base64
import java.io.IOException
import java.security.GeneralSecurityException

/**
 * Class used to encrypt, decrypt, sign ad verify data.
 *
 * <pre>
 *     val cryptoHelper = CryptoHelper.Builder(context).build()
 *     // Encrypt
 *     val cypherText = cryptoHelper.encrypt(text)
 *     // Decrypt
 *     val plainText = cryptoHelper.decrypt(cypherText)
 *     // Sign
 *     val signature = cryptoHelper.sign(text.toByteArray())
 *     // Verify
 *     val verified = cryptoHelper.verify(signature, text.toByteArray())
 * </pre>
 */
@Suppress("unused")
class CryptoHelper(
    private val aead: Aead,
    private val deterministicAead: DeterministicAead,
    private val signer: PublicKeySign,
    private val verifier: PublicKeyVerify,
) {
    /**
     * Builder class to configure CryptoHelper
     */
    class Builder(
        // Required parameters
        private val context: Context,
    ) {
        // Optional parameters
        private var masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        private var keysetPrefName = KEYSET_PREF_NAME
        private var keysetAlias = KEYSET_ALIAS
        private var aeadKeyTemplate: KeyTemplate
        private var deterministicAeadKeyTemplate: KeyTemplate
        private var signKeyTemplate: KeyTemplate

        init {
            AeadConfig.register()
            DeterministicAeadConfig.register()
            SignatureConfig.register()
            aeadKeyTemplate = KeyTemplates.get("AES256_GCM")
            deterministicAeadKeyTemplate = KeyTemplates.get("AES256_SIV")
            signKeyTemplate = KeyTemplates.get("ECDSA_P256")
        }

        /**
         * @param masterKey The SharedPreferences file to store the keyset.
         * @return This Builder
         */
        fun setMasterKey(masterKey: String): Builder {
            this.masterKeyAlias = masterKey
            return this
        }

        /**
         * @param keysetPrefName The SharedPreferences file to store the keyset.
         * @return This Builder
         */
        fun setKeysetPrefName(keysetPrefName: String): Builder {
            this.keysetPrefName = keysetPrefName
            return this
        }

        /**
         * @param keysetAlias The alias in the SharedPreferences file to store the keyset.
         * @return This Builder
         */
        fun setKeysetAlias(keysetAlias: String): Builder {
            this.keysetAlias = keysetAlias
            return this
        }

        /**
         * @param keyTemplate If the keyset for Aead encryption is not found or valid, generates a new one using keyTemplate.
         * @return This Builder
         */
        fun setAeadKeyTemplate(keyTemplate: KeyTemplate): Builder {
            this.aeadKeyTemplate = keyTemplate
            return this
        }

        /**
         * @param keyTemplate If the keyset for deterministic Aead encryption is not found or valid, generates a new one using keyTemplate.
         * @return This Builder
         */
        fun setDeterministicAeadKeyTemplate(keyTemplate: KeyTemplate): Builder {
            this.deterministicAeadKeyTemplate = keyTemplate
            return this
        }

        /**
         * @param keyTemplate If the keyset for signing/verifying is not found or valid, generates a new one using keyTemplate.
         * @return This Builder
         */
        fun setSignKeyTemplate(keyTemplate: KeyTemplate): Builder {
            this.signKeyTemplate = keyTemplate
            return this
        }

        /**
         * @return An CryptoHelper with the specified parameters.
         */
        @WorkerThread
        @Throws(GeneralSecurityException::class, IOException::class)
        fun build(): CryptoHelper {
            val aeadKeysetHandle = AndroidKeysetManager.Builder()
                .withKeyTemplate(aeadKeyTemplate)
                .withSharedPref(context, keysetAlias + "_aead__", keysetPrefName)
                .withMasterKeyUri(KEYSTORE_PATH_URI + masterKeyAlias)
                .build().keysetHandle

            val deterministicAeadKeysetHandle = AndroidKeysetManager.Builder()
                .withKeyTemplate(deterministicAeadKeyTemplate)
                .withSharedPref(context, keysetAlias + "_daead__", keysetPrefName)
                .withMasterKeyUri(KEYSTORE_PATH_URI + masterKeyAlias)
                .build().keysetHandle

            val signKeysetHandle = AndroidKeysetManager.Builder()
                .withKeyTemplate(signKeyTemplate)
                .withSharedPref(context, keysetAlias + "_sign__", keysetPrefName)
                .withMasterKeyUri(KEYSTORE_PATH_URI + masterKeyAlias)
                .build().keysetHandle

            val aead = aeadKeysetHandle.getPrimitive(Aead::class.java)
            val deterministicAead = deterministicAeadKeysetHandle.getPrimitive(DeterministicAead::class.java)
            val signer = signKeysetHandle.getPrimitive(PublicKeySign::class.java)
            val verifier = signKeysetHandle.publicKeysetHandle.getPrimitive(PublicKeyVerify::class.java)

            return CryptoHelper(aead, deterministicAead, signer, verifier)
        }
    }

    @WorkerThread
    fun encrypt(plainText: ByteArray, associatedData: ByteArray = ByteArray(0)): ByteArray =
        aead.encrypt(plainText, associatedData)

    @WorkerThread
    fun encrypt(plainText: String): String = Base64.encodeToString(encrypt(plainText.toByteArray()), Base64.DEFAULT)

    @WorkerThread
    fun decrypt(cipherText: ByteArray, associatedData: ByteArray = ByteArray(0)): ByteArray =
        aead.decrypt(cipherText, associatedData)

    @WorkerThread
    fun decrypt(cipherText: String): String = String(decrypt(Base64.decode(cipherText, Base64.DEFAULT)), Charsets.UTF_8)

    @WorkerThread
    fun encryptDeterministically(plainText: ByteArray, associatedData: ByteArray = ByteArray(0)): ByteArray =
        deterministicAead.encryptDeterministically(plainText, associatedData)

    @WorkerThread
    fun encryptDeterministically(plainText: String): String = Base64.encodeToString(encryptDeterministically(plainText.toByteArray()), Base64.DEFAULT)

    @WorkerThread
    fun decryptDeterministically(cipherText: ByteArray, associatedData: ByteArray = ByteArray(0)): ByteArray =
        deterministicAead.decryptDeterministically(cipherText, associatedData)

    @WorkerThread
    fun decryptDeterministically(cipherText: String): String =
        String(decryptDeterministically(Base64.decode(cipherText, Base64.DEFAULT)), Charsets.UTF_8)

    @WorkerThread
    fun sign(data: ByteArray): ByteArray =
        signer.sign(data)

    @WorkerThread
    fun verify(signature: ByteArray, data: ByteArray): Boolean =
        try {
            verifier.verify(signature, data)
            true
        } catch (e: GeneralSecurityException) {
            false
        }

    companion object {
        private const val KEYSET_ALIAS = "__crypto_helper_keyset"
        private const val KEYSET_PREF_NAME = "__crypto_helper_pref__"
        private const val KEYSTORE_PATH_URI = "android-keystore://"
    }
}
