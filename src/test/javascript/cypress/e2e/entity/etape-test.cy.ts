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

describe('EtapeTest e2e test', () => {
  const etapeTestPageUrl = '/etape-test';
  const etapeTestPageUrlPattern = new RegExp('/etape-test(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const etapeTestSample = { nom: 'suivre antique après que' };

  let etapeTest;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/etape-tests+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/etape-tests').as('postEntityRequest');
    cy.intercept('DELETE', '/api/etape-tests/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (etapeTest) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/etape-tests/${etapeTest.id}`,
      }).then(() => {
        etapeTest = undefined;
      });
    }
  });

  it('EtapeTests menu should load EtapeTests page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('etape-test');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EtapeTest').should('exist');
    cy.url().should('match', etapeTestPageUrlPattern);
  });

  describe('EtapeTest page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(etapeTestPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EtapeTest page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/etape-test/new$'));
        cy.getEntityCreateUpdateHeading('EtapeTest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', etapeTestPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/etape-tests',
          body: etapeTestSample,
        }).then(({ body }) => {
          etapeTest = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/etape-tests+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [etapeTest],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(etapeTestPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EtapeTest page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('etapeTest');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', etapeTestPageUrlPattern);
      });

      it('edit button click should load edit EtapeTest page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EtapeTest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', etapeTestPageUrlPattern);
      });

      it('edit button click should load edit EtapeTest page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EtapeTest');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', etapeTestPageUrlPattern);
      });

      it('last delete button click should delete instance of EtapeTest', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('etapeTest').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', etapeTestPageUrlPattern);

        etapeTest = undefined;
      });
    });
  });

  describe('new EtapeTest page', () => {
    beforeEach(() => {
      cy.visit(`${etapeTestPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EtapeTest');
    });

    it('should create an instance of EtapeTest', () => {
      cy.get(`[data-cy="nom"]`).type('vu que');
      cy.get(`[data-cy="nom"]`).should('have.value', 'vu que');

      cy.get(`[data-cy="status"]`).type('dessus crac');
      cy.get(`[data-cy="status"]`).should('have.value', 'dessus crac');

      cy.get(`[data-cy="infos"]`).type('aimable égoïste tranquille');
      cy.get(`[data-cy="infos"]`).should('have.value', 'aimable égoïste tranquille');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        etapeTest = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', etapeTestPageUrlPattern);
    });
  });
});
