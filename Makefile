# ====================================================================================
# Variables

ECR_REGISTRY ?= docker.io
ECR_REPOSITORY ?= sample-app
IMAGE_TAG ?= latest
NAMESPACE ?= sample-app
HELM_RELEASE ?= sample-app
HELM_PATH ?= k8s/helm/web-service
DEPLOYMENT_STRATEGY ?= rollingUpdate

# ====================================================================================
# Targets

build-app-image:
	docker-compose up -d postgres
	gradle build
	docker build -t $(ECR_REGISTRY)/$(ECR_REPOSITORY):$(IMAGE_TAG) .
	docker-compose down

push-app-image:
	docker push $(ECR_REGISTRY)/$(ECR_REPOSITORY):$(IMAGE_TAG)

build-integration-tests-image:
	docker build -t $(ECR_REGISTRY)/$(ECR_REPOSITORY)-it:$(IMAGE_TAG) ./integration-tests

push-integration-tests-image:
	docker push $(ECR_REGISTRY)/$(ECR_REPOSITORY)-it:$(IMAGE_TAG)

build-push-images: build-app-image push-app-image build-integration-tests-image push-integration-tests-image

run-integration-tests:
	docker-compose up -d postgres
	gradle generateJooq
	docker-compose up --build --force-recreate -d
	docker-compose --profile tests up --exit-code-from tests
	docker-compose down

deploy-ephemeral-app:
	helm -n $(NAMESPACE) install $(HELM_RELEASE) $(HELM_PATH) \
		-f $(HELM_PATH)/values-production.yaml \
		--set "image.repository=$(ECR_REGISTRY)/$(ECR_REPOSITORY)" \
		--set "image.tag=$(IMAGE_TAG)" \
		--set "deployment.strategy=rollingUpdate" \
		--set "integrationTests.enabled=true" \
		--set "integrationTests.image=$(ECR_REGISTRY)/$(ECR_REPOSITORY)-it:$(IMAGE_TAG)" \
		--timeout 1h \
		--wait

dry-run:
	helm -n $(NAMESPACE) upgrade -i $(HELM_RELEASE) $(HELM_PATH) \
		-f $(HELM_PATH)/values-production.yaml \
		--set "image.repository=$(ECR_REGISTRY)/$(ECR_REPOSITORY)" \
		--set "image.tag=$(IMAGE_TAG)" \
		--set "deployment.strategy=$(DEPLOYMENT_STRATEGY)" \
		--timeout 5m \
		--wait \
		--dry-run

deploy:
	helm -n $(NAMESPACE) install $(HELM_RELEASE) $(HELM_PATH) \
		-f $(HELM_PATH)/values-production.yaml \
		--set "image.repository=$(ECR_REGISTRY)/$(ECR_REPOSITORY)" \
		--set "image.tag=$(IMAGE_TAG)" \
		--set "deployment.strategy=$(DEPLOYMENT_STRATEGY)" \
		--timeout 5m \
		--wait
