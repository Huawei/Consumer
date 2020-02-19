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

package com.huawei.hms.nearby.beaconcanteen.model;

/**
 * Canteen Adapter Info
 *
 * @since 2019-12-13
 */
public class CanteenAdapterInfo {
    private int canteenImage;
    private String canteenName;
    private String canteenDesc;
    private String canteenScore;
    private boolean isShowNotice;
    private String notice;
    private int requestCode;

    /**
     * Construct
     */
    public CanteenAdapterInfo() {}

    /**
     * Construct
     *
     * @param canteenName The Image of the Canteen
     * @param canteenDesc The Image of the Canteen
     * @param canteenScore The Image of the Canteen
     */
    public CanteenAdapterInfo(String canteenName, String canteenDesc, String canteenScore) {
        this.canteenName = canteenName;
        this.canteenDesc = canteenDesc;
        this.canteenScore = canteenScore;
    }

    public boolean isShowNotice() {
        return isShowNotice;
    }

    public void setShowNotice(boolean showNotice) {
        isShowNotice = showNotice;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getCanteenImage() {
        return canteenImage;
    }

    public void setCanteenImage(int canteenImage) {
        this.canteenImage = canteenImage;
    }

    public String getCanteenName() {
        return canteenName;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public String getCanteenDesc() {
        return canteenDesc;
    }

    public void setCanteenDesc(String canteenDesc) {
        this.canteenDesc = canteenDesc;
    }

    public String getCanteenScore() {
        return canteenScore;
    }

    public void setCanteenScore(String canteenScore) {
        this.canteenScore = canteenScore;
    }
}
