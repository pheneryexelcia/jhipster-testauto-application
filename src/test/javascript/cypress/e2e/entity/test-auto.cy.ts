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

describe('TestAuto e2e test', () => {
  const testAutoPageUrl = '/test-auto';
  const testAutoPageUrlPattern = new RegExp('/test-auto(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const testAutoSample = { nom: 'suivant responsable' };

  let testAuto;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/test-autos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/test-autos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/test-autos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (testAuto) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/test-autos/${testAuto.id}`,
      }).then(() => {
        testAuto = undefined;
      });
    }
  });

  it('TestAutos menu should load TestAutos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('test-auto');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TestAuto').should('exist');
    cy.url().should('match', testAutoPageUrlPattern);
  });

  describe('TestAuto page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(testAutoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TestAuto page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/test-auto/new$'));
        cy.getEntityCreateUpdateHeading('TestAuto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testAutoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/test-autos',
          body: testAutoSample,
        }).then(({ body }) => {
          testAuto = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/test-autos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [testAuto],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(testAutoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TestAuto page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('testAuto');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testAutoPageUrlPattern);
      });

      it('edit button click should load edit TestAuto page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TestAuto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testAutoPageUrlPattern);
      });

      it('edit button click should load edit TestAuto page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TestAuto');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testAutoPageUrlPattern);
      });

      it('last delete button click should delete instance of TestAuto', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('testAuto').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testAutoPageUrlPattern);

        testAuto = undefined;
      });
    });
  });

  describe('new TestAuto page', () => {
    beforeEach(() => {
      cy.visit(`${testAutoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TestAuto');
    });

    it('should create an instance of TestAuto', () => {
      cy.get(`[data-cy="nom"]`).type('vorace smack secouriste');
      cy.get(`[data-cy="nom"]`).should('have.value', 'vorace smack secouriste');

      cy.get(`[data-cy="status"]`).type('agréable');
      cy.get(`[data-cy="status"]`).should('have.value', 'agréable');

      cy.get(`[data-cy="categorie"]`).type('pousser arrière beaucoup');
      cy.get(`[data-cy="categorie"]`).should('have.value', 'pousser arrière beaucoup');

      cy.get(`[data-cy="infos"]`).type('athlète du fait que exposer');
      cy.get(`[data-cy="infos"]`).should('have.value', 'athlète du fait que exposer');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        testAuto = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', testAutoPageUrlPattern);
    });
  });
});
