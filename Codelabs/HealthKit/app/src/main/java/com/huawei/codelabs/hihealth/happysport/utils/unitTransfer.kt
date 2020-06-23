package com.huawei.codelabs.hihealth.happysport.utils

// m to km
fun Long.mToKm(): String {
    return (this.toFloat() / 1000).toString()
}

// mm/s to km/h
fun Long.mpsToKph(): String {
    return String.format("%.1f", this.toFloat() / 1000000 * 60 * 60)
}