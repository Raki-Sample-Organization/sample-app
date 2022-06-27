# syntax=docker/dockerfile:1
FROM cypress/included:10.2.0
COPY cypress /e2e/cypress
COPY cypress.config.ts package.json yarn.lock tsconfig.json /e2e/
WORKDIR /e2e
RUN yarn install && cypress install
