package com.exelcia.testauto.oreka.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RapportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Rapport getRapportSample1() {
        return new Rapport()
            .id(1L)
            .nom("nom1")
            .dateStr("dateStr1")
            .environnement("environnement1")
            .nbtests(1)
            .nbtestsOk(1)
            .nbtestsKo(1)
            .logo("logo1");
    }

    public static Rapport getRapportSample2() {
        return new Rapport()
            .id(2L)
            .nom("nom2")
            .dateStr("dateStr2")
            .environnement("environnement2")
            .nbtests(2)
            .nbtestsOk(2)
            .nbtestsKo(2)
            .logo("logo2");
    }

    public static Rapport getRapportRandomSampleGenerator() {
        return new Rapport()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .dateStr(UUID.randomUUID().toString())
            .environnement(UUID.randomUUID().toString())
            .nbtests(intCount.incrementAndGet())
            .nbtestsOk(intCount.incrementAndGet())
            .nbtestsKo(intCount.incrementAndGet())
            .logo(UUID.randomUUID().toString());
    }
}
