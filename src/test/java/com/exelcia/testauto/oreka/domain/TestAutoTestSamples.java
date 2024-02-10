package com.exelcia.testauto.oreka.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TestAutoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TestAuto getTestAutoSample1() {
        return new TestAuto().id(1L).nom("nom1").status("status1").categorie("categorie1").infos("infos1");
    }

    public static TestAuto getTestAutoSample2() {
        return new TestAuto().id(2L).nom("nom2").status("status2").categorie("categorie2").infos("infos2");
    }

    public static TestAuto getTestAutoRandomSampleGenerator() {
        return new TestAuto()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .categorie(UUID.randomUUID().toString())
            .infos(UUID.randomUUID().toString());
    }
}
