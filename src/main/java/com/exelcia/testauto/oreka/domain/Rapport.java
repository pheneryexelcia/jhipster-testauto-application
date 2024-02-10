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
 * A Rapport.
 */
@Entity
@Table(name = "rapport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rapport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "nom", nullable = false, unique = true)
    private String nom;

    @NotNull
    @Column(name = "date_str", nullable = false, unique = true)
    private String dateStr;

    @NotNull
    @Column(name = "environnement", nullable = false)
    private String environnement;

    @Column(name = "nbtests")
    private Integer nbtests;

    @Column(name = "nbtests_ok")
    private Integer nbtestsOk;

    @Column(name = "nbtests_ko")
    private Integer nbtestsKo;

    @Column(name = "logo")
    private String logo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rapport")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etapeTests", "rapport" }, allowSetters = true)
    private Set<TestAuto> testAutos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rapport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Rapport nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDateStr() {
        return this.dateStr;
    }

    public Rapport dateStr(String dateStr) {
        this.setDateStr(dateStr);
        return this;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getEnvironnement() {
        return this.environnement;
    }

    public Rapport environnement(String environnement) {
        this.setEnvironnement(environnement);
        return this;
    }

    public void setEnvironnement(String environnement) {
        this.environnement = environnement;
    }

    public Integer getNbtests() {
        return this.nbtests;
    }

    public Rapport nbtests(Integer nbtests) {
        this.setNbtests(nbtests);
        return this;
    }

    public void setNbtests(Integer nbtests) {
        this.nbtests = nbtests;
    }

    public Integer getNbtestsOk() {
        return this.nbtestsOk;
    }

    public Rapport nbtestsOk(Integer nbtestsOk) {
        this.setNbtestsOk(nbtestsOk);
        return this;
    }

    public void setNbtestsOk(Integer nbtestsOk) {
        this.nbtestsOk = nbtestsOk;
    }

    public Integer getNbtestsKo() {
        return this.nbtestsKo;
    }

    public Rapport nbtestsKo(Integer nbtestsKo) {
        this.setNbtestsKo(nbtestsKo);
        return this;
    }

    public void setNbtestsKo(Integer nbtestsKo) {
        this.nbtestsKo = nbtestsKo;
    }

    public String getLogo() {
        return this.logo;
    }

    public Rapport logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<TestAuto> getTestAutos() {
        return this.testAutos;
    }

    public void setTestAutos(Set<TestAuto> testAutos) {
        if (this.testAutos != null) {
            this.testAutos.forEach(i -> i.setRapport(null));
        }
        if (testAutos != null) {
            testAutos.forEach(i -> i.setRapport(this));
        }
        this.testAutos = testAutos;
    }

    public Rapport testAutos(Set<TestAuto> testAutos) {
        this.setTestAutos(testAutos);
        return this;
    }

    public Rapport addTestAuto(TestAuto testAuto) {
        this.testAutos.add(testAuto);
        testAuto.setRapport(this);
        return this;
    }

    public Rapport removeTestAuto(TestAuto testAuto) {
        this.testAutos.remove(testAuto);
        testAuto.setRapport(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rapport)) {
            return false;
        }
        return getId() != null && getId().equals(((Rapport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rapport{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", dateStr='" + getDateStr() + "'" +
            ", environnement='" + getEnvironnement() + "'" +
            ", nbtests=" + getNbtests() +
            ", nbtestsOk=" + getNbtestsOk() +
            ", nbtestsKo=" + getNbtestsKo() +
            ", logo='" + getLogo() + "'" +
            "}";
    }
}
