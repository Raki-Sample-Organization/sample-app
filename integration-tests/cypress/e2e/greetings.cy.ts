/// <reference types="cypress" />

interface Greeting {
  id?: string
  language: 'SPANISH' | 'ENGLISH' | 'FRENCH'
  content: string
}

context('Greetings API', () => {
  beforeEach(() => {
    cy.fixture('greetings.json').as('greetings')
  })

  it('Create greetings', function () {
    (this.greetings as Greeting[]).map(_ => {
      cy.request('POST', `${Cypress.env('APP_HOST')}/greetings`, _)
        .then((response) => expect(response).property('status').to.equal(201))
    })
  })

  it('Get Spanish greetings', () => {
    cy.request({ url: `${Cypress.env('APP_HOST')}/greetings`, qs: { language: 'SPANISH' } }).then(_ => {
      expect(_.body).to.have.length(2)
      expect(_.status).to.be.eq(200)
      expect(_.body[0]).to.have.property('content', '¡Hola!')
      expect(_.body[1]).to.have.property('content', '¡Buenos días!')
    })
  })

  it('Get English greetings', () => {
    cy.request({ url: `${Cypress.env('APP_HOST')}/greetings`, qs: { language: 'ENGLISH' } }).then(_ => {
      expect(_.body).to.have.length(2)
      expect(_.status).to.be.eq(200)
      expect(_.body[0]).to.have.property('content', 'Good afternoon!')
      expect(_.body[1]).to.have.property('content', 'Hey!')
    })
  })

  it('Get French greetings', () => {
    cy.request({ url: `${Cypress.env('APP_HOST')}/greetings`, qs: { language: 'FRENCH' } }).then(_ => {
      expect(_.body).to.have.length(1)
      expect(_.status).to.be.eq(200)
      expect(_.body[0]).to.have.property('content', 'Salut')
    })
  })
})
