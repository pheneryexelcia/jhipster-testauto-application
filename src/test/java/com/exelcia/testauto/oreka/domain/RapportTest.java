package com.exelcia.testauto.oreka.domain;

import static com.exelcia.testauto.oreka.domain.RapportTestSamples.*;
import static com.exelcia.testauto.oreka.domain.TestAutoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.exelcia.testauto.oreka.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RapportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rapport.class);
        Rapport rapport1 = getRapportSample1();
        Rapport rapport2 = new Rapport();
        assertThat(rapport1).isNotEqualTo(rapport2);

        rapport2.setId(rapport1.getId());
        assertThat(rapport1).isEqualTo(rapport2);

        rapport2 = getRapportSample2();
        assertThat(rapport1).isNotEqualTo(rapport2);
    }

    @Test
    void testAutoTest() throws Exception {
        Rapport rapport = getRapportRandomSampleGenerator();
        TestAuto testAutoBack = getTestAutoRandomSampleGenerator();

        rapport.addTestAuto(testAutoBack);
        assertThat(rapport.getTestAutos()).containsOnly(testAutoBack);
        assertThat(testAutoBack.getRapport()).isEqualTo(rapport);

        rapport.removeTestAuto(testAutoBack);
        assertThat(rapport.getTestAutos()).doesNotContain(testAutoBack);
        assertThat(testAutoBack.getRapport()).isNull();

        rapport.testAutos(new HashSet<>(Set.of(testAutoBack)));
        assertThat(rapport.getTestAutos()).containsOnly(testAutoBack);
        assertThat(testAutoBack.getRapport()).isEqualTo(rapport);

        rapport.setTestAutos(new HashSet<>());
        assertThat(rapport.getTestAutos()).doesNotContain(testAutoBack);
        assertThat(testAutoBack.getRapport()).isNull();
    }
}
