/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2014-2019. All rights reserved.
 */

package com.huawei.pps.hms.test.installreferrer;

public interface InstallReferrerCallback {
    void onSuccuss(String installReferrer, long clickTimestamp, long installTimestamp);

    void onFail(String errMsg);
}
