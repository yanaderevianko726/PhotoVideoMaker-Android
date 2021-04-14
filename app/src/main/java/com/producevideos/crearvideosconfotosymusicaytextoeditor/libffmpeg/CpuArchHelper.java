package com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg;

import android.os.Build;

class CpuArchHelper {
    CpuArchHelper() {
    }

    static CpuArch getCpuArch() {
        Log.m20d(Build.CPU_ABI);
        if (Build.CPU_ABI.equals(getx86CpuAbi())) {
            return CpuArch.x86;
        }
        if (Build.CPU_ABI.equals(getArmeabiv7CpuAbi())) {
            return CpuArch.ARMv7;
        }
        if (Build.CPU_ABI.equals(getX86_64CpuAbi())) {
            return CpuArch.x86;
        }
        if (Build.CPU_ABI.equals(getArm64CpuAbi())) {
            return CpuArch.ARMv7;
        }
        return CpuArch.NONE;
    }

    static String getx86CpuAbi() {
        return "x86";
    }

    static String getArmeabiv7CpuAbi() {
        return "armeabi-v7a";
    }

    static String getX86_64CpuAbi() {
        return "x86_64";
    }

    static String getArm64CpuAbi() {
        return "arm64-v8a";
    }
}
