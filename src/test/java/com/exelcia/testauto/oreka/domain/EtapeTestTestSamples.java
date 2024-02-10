package com.exelcia.testauto.oreka.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EtapeTestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EtapeTest getEtapeTestSample1() {
        return new EtapeTest().id(1L).nom("nom1").status("status1").infos("infos1");
    }

    public static EtapeTest getEtapeTestSample2() {
        return new EtapeTest().id(2L).nom("nom2").status("status2").infos("infos2");
    }

    public static EtapeTest getEtapeTestRandomSampleGenerator() {
        return new EtapeTest()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .infos(UUID.randomUUID().toString());
    }
}
