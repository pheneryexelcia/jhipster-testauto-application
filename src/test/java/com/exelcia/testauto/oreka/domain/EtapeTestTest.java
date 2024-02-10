package com.exelcia.testauto.oreka.domain;

import static com.exelcia.testauto.oreka.domain.EtapeTestTestSamples.*;
import static com.exelcia.testauto.oreka.domain.TestAutoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.exelcia.testauto.oreka.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EtapeTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EtapeTest.class);
        EtapeTest etapeTest1 = getEtapeTestSample1();
        EtapeTest etapeTest2 = new EtapeTest();
        assertThat(etapeTest1).isNotEqualTo(etapeTest2);

        etapeTest2.setId(etapeTest1.getId());
        assertThat(etapeTest1).isEqualTo(etapeTest2);

        etapeTest2 = getEtapeTestSample2();
        assertThat(etapeTest1).isNotEqualTo(etapeTest2);
    }

    @Test
    void testAutoTest() throws Exception {
        EtapeTest etapeTest = getEtapeTestRandomSampleGenerator();
        TestAuto testAutoBack = getTestAutoRandomSampleGenerator();

        etapeTest.setTestAuto(testAutoBack);
        assertThat(etapeTest.getTestAuto()).isEqualTo(testAutoBack);

        etapeTest.testAuto(null);
        assertThat(etapeTest.getTestAuto()).isNull();
    }
}
