# ====================================================================================
# Variables

ECR_REGISTRY ?= docker.io
ECR_REPOSITORY ?= sample-app
IMAGE_TAG ?= latest
NAMESPACE ?= sample-app
HELM_RELEASE ?= sample-app
HELM_PATH ?= k8s/helm/web-service
DEPLOYMENT_STRATEGY ?= rollingUpdate
INFRASTRUCTURE_EPHEMERAL_PATH ?= ../infrastructure/k8s/overlays/ephemeral/sample-app
INTEGRATION_TESTS_JOB_NAME ?= sample-app-it


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

provision-ephemeral-environment:
	kustomize build $(INFRASTRUCTURE_EPHEMERAL_PATH) > ephemeral.yaml
	kubectl apply -f ephemeral.yaml
	kubectl wait --for=condition=ready --timeout=30m rdsinstance/sample-app-postgres

run-integration-tests-helm:
	helm -n $(NAMESPACE) template $(HELM_RELEASE) $(HELM_PATH) \
		--set "image.repository=$(ECR_REGISTRY)/$(ECR_REPOSITORY)" \
		--set "image.tag=$(IMAGE_TAG)" \
		--set "integrationTests.enabled=true" \
		-s templates/integration-tests-job.yaml \
		> it-manifest.yaml
	kubectl apply -f it-manifest.yaml
	kubectl -n $(NAMESPACE) wait --for=condition=complete --timeout=10m jobs/$(INTEGRATION_TESTS_JOB_NAME)
	kubectl -n $(NAMESPACE) logs jobs/$(INTEGRATION_TESTS_JOB_NAME)

terminate-ephemeral-environment:
	kubectl delete -k $(INFRASTRUCTURE_EPHEMERAL_PATH)

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
	helm -n $(NAMESPACE) upgrade -i $(HELM_RELEASE) $(HELM_PATH) \
		-f $(HELM_PATH)/values-production.yaml \
		--set "image.repository=$(ECR_REGISTRY)/$(ECR_REPOSITORY)" \
		--set "image.tag=$(IMAGE_TAG)" \
		--set "deployment.strategy=$(DEPLOYMENT_STRATEGY)" \
		--timeout 5m \
		--wait
	kubectl-argo-rollouts -n $(NAMESPACE) status --watch --timeout 10m $(HELM_RELEASE)
