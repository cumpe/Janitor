/*
 * Decompiled with CFR 0_115.
 */
package net.darepvp.update;

import java.io.PrintStream;
import net.darepvp.util.UtilTime;

public enum UpdateType {
    MIN_64(3840000),
    MIN_32(1920000),
    MIN_16(960000),
    MIN_08(480000),
    MIN_05(300000),
    MIN_04(240000),
    MIN_02(120000),
    MIN_01(60000),
    SLOWEST(32000),
    SLOWER(16000),
    SLOW(4000),
    SEC_05(5000),
    SEC_04(4000),
    SEC_03(3000),
    SEC_02(2000),
    SEC(1000),
    FAST(500),
    FASTER(250),
    FASTEST(125),
    TICK(49);
    
    private long _time;
    private long _last;
    private long _timeSpent;
    private long _timeCount;

    private UpdateType(String time, int n2, long l) {
        this._time = time;
        this._last = System.currentTimeMillis();
    }

    public boolean Elapsed() {
        if (UtilTime.elapsed(this._last, this._time)) {
            this._last = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public void StartTime() {
        this._timeCount = System.currentTimeMillis();
    }

    public void StopTime() {
        this._timeSpent += System.currentTimeMillis() - this._timeCount;
    }

    public void PrintAndResetTime() {
        System.out.println(String.valueOf(this.name()) + " in a second: " + this._timeSpent);
        this._timeSpent = 0;
    }
}

