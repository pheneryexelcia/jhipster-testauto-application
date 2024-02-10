import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Rapport e2e test', () => {
  const rapportPageUrl = '/rapport';
  const rapportPageUrlPattern = new RegExp('/rapport(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const rapportSample = { nom: 'clac habile', dateStr: 'turquoise', environnement: 'reconstituer' };

  let rapport;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rapports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rapports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rapports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rapport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rapports/${rapport.id}`,
      }).then(() => {
        rapport = undefined;
      });
    }
  });

  it('Rapports menu should load Rapports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rapport');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Rapport').should('exist');
    cy.url().should('match', rapportPageUrlPattern);
  });

  describe('Rapport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rapportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Rapport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/rapport/new$'));
        cy.getEntityCreateUpdateHeading('Rapport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rapportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rapports',
          body: rapportSample,
        }).then(({ body }) => {
          rapport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rapports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rapport],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(rapportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Rapport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rapport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rapportPageUrlPattern);
      });

      it('edit button click should load edit Rapport page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Rapport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rapportPageUrlPattern);
      });

      it('edit button click should load edit Rapport page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Rapport');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rapportPageUrlPattern);
      });

      it('last delete button click should delete instance of Rapport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rapport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rapportPageUrlPattern);

        rapport = undefined;
      });
    });
  });

  describe('new Rapport page', () => {
    beforeEach(() => {
      cy.visit(`${rapportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Rapport');
    });

    it('should create an instance of Rapport', () => {
      cy.get(`[data-cy="nom"]`).type('prestataire de services');
      cy.get(`[data-cy="nom"]`).should('have.value', 'prestataire de services');

      cy.get(`[data-cy="dateStr"]`).type('habile jusque');
      cy.get(`[data-cy="dateStr"]`).should('have.value', 'habile jusque');

      cy.get(`[data-cy="environnement"]`).type('hors de');
      cy.get(`[data-cy="environnement"]`).should('have.value', 'hors de');

      cy.get(`[data-cy="nbtests"]`).type('11484');
      cy.get(`[data-cy="nbtests"]`).should('have.value', '11484');

      cy.get(`[data-cy="nbtestsOk"]`).type('10313');
      cy.get(`[data-cy="nbtestsOk"]`).should('have.value', '10313');

      cy.get(`[data-cy="nbtestsKo"]`).type('10381');
      cy.get(`[data-cy="nbtestsKo"]`).should('have.value', '10381');

      cy.get(`[data-cy="logo"]`).type('hi recta');
      cy.get(`[data-cy="logo"]`).should('have.value', 'hi recta');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        rapport = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', rapportPageUrlPattern);
    });
  });
});
