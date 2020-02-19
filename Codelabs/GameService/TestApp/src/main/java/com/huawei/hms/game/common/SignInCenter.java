
/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.hms.game.common;

import com.huawei.hms.support.hwid.result.AuthHuaweiId;

public class SignInCenter {

    private static SignInCenter INS = new SignInCenter();

    private static AuthHuaweiId currentAuthHuaweiId;

    public static SignInCenter get() {
        return INS;
    }

    public void updateAuthHuaweiId(AuthHuaweiId AuthHuaweiId) {
        currentAuthHuaweiId = AuthHuaweiId;
    }

    public AuthHuaweiId getAuthHuaweiId() {
        return currentAuthHuaweiId;
    }
}
