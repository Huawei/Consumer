/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.iapdemo.common;

public class Key {
    private static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkW4zxYG8CgJfwUHUGsxNhCD8yxaRVfTMuI4EUy7Y3D46EWTcJtTvEycj+JjTbf4oKx1qspAAT7G+rIkiYubnPezI2KCEvp4PQmawxG6UB7bnWEiF9TdEXw+CrKONKgMKllhvB3AL39S8B/vPOlYw3cF/aKpcWY6CqlDn1Um6E3O3dfoIJ0iMpOMyzJE7chipZ7zxvL61kpEzlSP1hU2LyfRZtEBocmB6wj3FApdc33ao0wBsWtL7Ulvo5V6gCZrMo+AgYFgv+NKowD6MPWSRFpNqz+as+yfXah/dNTcWieGeFxA7kb8VqOj+HJEH4gjWBSN7ATXEYEkHBOpTLFK+1QIDAQAB";

    /**
     * get the publicKey of the application
     * During the encoding process, avoid storing the public key in clear text.
     * @return
     */
    public static String getPublicKey(){
        return publicKey;
    }
}
