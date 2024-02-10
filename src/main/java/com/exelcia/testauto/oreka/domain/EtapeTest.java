package com.exelcia.testauto.oreka.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EtapeTest.
 */
@Entity
@Table(name = "etape_test")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EtapeTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "status")
    private String status;

    @Column(name = "infos")
    private String infos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "etapeTests", "rapport" }, allowSetters = true)
    private TestAuto testAuto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EtapeTest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public EtapeTest nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getStatus() {
        return this.status;
    }

    public EtapeTest status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfos() {
        return this.infos;
    }

    public EtapeTest infos(String infos) {
        this.setInfos(infos);
        return this;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    public TestAuto getTestAuto() {
        return this.testAuto;
    }

    public void setTestAuto(TestAuto testAuto) {
        this.testAuto = testAuto;
    }

    public EtapeTest testAuto(TestAuto testAuto) {
        this.setTestAuto(testAuto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EtapeTest)) {
            return false;
        }
        return getId() != null && getId().equals(((EtapeTest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtapeTest{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", status='" + getStatus() + "'" +
            ", infos='" + getInfos() + "'" +
            "}";
    }
}
