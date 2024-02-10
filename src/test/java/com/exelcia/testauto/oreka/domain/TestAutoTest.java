package com.exelcia.testauto.oreka.domain;

import static com.exelcia.testauto.oreka.domain.EtapeTestTestSamples.*;
import static com.exelcia.testauto.oreka.domain.RapportTestSamples.*;
import static com.exelcia.testauto.oreka.domain.TestAutoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.exelcia.testauto.oreka.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TestAutoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestAuto.class);
        TestAuto testAuto1 = getTestAutoSample1();
        TestAuto testAuto2 = new TestAuto();
        assertThat(testAuto1).isNotEqualTo(testAuto2);

        testAuto2.setId(testAuto1.getId());
        assertThat(testAuto1).isEqualTo(testAuto2);

        testAuto2 = getTestAutoSample2();
        assertThat(testAuto1).isNotEqualTo(testAuto2);
    }

    @Test
    void etapeTestTest() throws Exception {
        TestAuto testAuto = getTestAutoRandomSampleGenerator();
        EtapeTest etapeTestBack = getEtapeTestRandomSampleGenerator();

        testAuto.addEtapeTest(etapeTestBack);
        assertThat(testAuto.getEtapeTests()).containsOnly(etapeTestBack);
        assertThat(etapeTestBack.getTestAuto()).isEqualTo(testAuto);

        testAuto.removeEtapeTest(etapeTestBack);
        assertThat(testAuto.getEtapeTests()).doesNotContain(etapeTestBack);
        assertThat(etapeTestBack.getTestAuto()).isNull();

        testAuto.etapeTests(new HashSet<>(Set.of(etapeTestBack)));
        assertThat(testAuto.getEtapeTests()).containsOnly(etapeTestBack);
        assertThat(etapeTestBack.getTestAuto()).isEqualTo(testAuto);

        testAuto.setEtapeTests(new HashSet<>());
        assertThat(testAuto.getEtapeTests()).doesNotContain(etapeTestBack);
        assertThat(etapeTestBack.getTestAuto()).isNull();
    }

    @Test
    void rapportTest() throws Exception {
        TestAuto testAuto = getTestAutoRandomSampleGenerator();
        Rapport rapportBack = getRapportRandomSampleGenerator();

        testAuto.setRapport(rapportBack);
        assertThat(testAuto.getRapport()).isEqualTo(rapportBack);

        testAuto.rapport(null);
        assertThat(testAuto.getRapport()).isNull();
    }
}
