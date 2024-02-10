package com.exelcia.testauto.oreka.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestAuto.
 */
@Entity
@Table(name = "test_auto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestAuto implements Serializable {

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

    @Column(name = "categorie")
    private String categorie;

    @Column(name = "infos")
    private String infos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "testAuto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "testAuto" }, allowSetters = true)
    private Set<EtapeTest> etapeTests = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "testAutos" }, allowSetters = true)
    private Rapport rapport;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestAuto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public TestAuto nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getStatus() {
        return this.status;
    }

    public TestAuto status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public TestAuto categorie(String categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getInfos() {
        return this.infos;
    }

    public TestAuto infos(String infos) {
        this.setInfos(infos);
        return this;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    public Set<EtapeTest> getEtapeTests() {
        return this.etapeTests;
    }

    public void setEtapeTests(Set<EtapeTest> etapeTests) {
        if (this.etapeTests != null) {
            this.etapeTests.forEach(i -> i.setTestAuto(null));
        }
        if (etapeTests != null) {
            etapeTests.forEach(i -> i.setTestAuto(this));
        }
        this.etapeTests = etapeTests;
    }

    public TestAuto etapeTests(Set<EtapeTest> etapeTests) {
        this.setEtapeTests(etapeTests);
        return this;
    }

    public TestAuto addEtapeTest(EtapeTest etapeTest) {
        this.etapeTests.add(etapeTest);
        etapeTest.setTestAuto(this);
        return this;
    }

    public TestAuto removeEtapeTest(EtapeTest etapeTest) {
        this.etapeTests.remove(etapeTest);
        etapeTest.setTestAuto(null);
        return this;
    }

    public Rapport getRapport() {
        return this.rapport;
    }

    public void setRapport(Rapport rapport) {
        this.rapport = rapport;
    }

    public TestAuto rapport(Rapport rapport) {
        this.setRapport(rapport);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestAuto)) {
            return false;
        }
        return getId() != null && getId().equals(((TestAuto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestAuto{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", status='" + getStatus() + "'" +
            ", categorie='" + getCategorie() + "'" +
            ", infos='" + getInfos() + "'" +
            "}";
    }
}
