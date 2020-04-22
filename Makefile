.PHONY: help dist publish promote
SHELL=/bin/bash
ECR_REGISTRY=edejesus1097/docker-cantaloupe
DATETIME:=$(shell date -u +%Y%m%dT%H%M%SZ)

help: ## Print this message
	@awk 'BEGIN { FS = ":.*##"; print "Usage:  make <target>\n\nTargets:" } \
		/^[-_[:alpha:]]+:.?*##/ { printf "  %-15s%s\n", $$1, $$2 }' $(MAKEFILE_LIST)

dist: ## Build docker image
	docker build -t edejesus1097/docker-cantaloupe \
		-t edejesus1097/docker-cantaloupe:`git describe --always` \
		-t cantaloupe:latest . --squash --no-cache

publish: dist ## Build, tag and push
	docker push $(ECR_REGISTRY):test
	docker push $(ECR_REGISTRY):`git describe --always`

promote: ## Promote the current staging build to production
	$$(aws ecr get-login --no-include-email --region us-east-1)
	docker pull $(ECR_REGISTRY)/cantaloupe-stage:latest
	docker tag $(ECR_REGISTRY)/cantaloupe-stage:latest $(ECR_REGISTRY)/cantaloupe-prod:latest
	docker tag $(ECR_REGISTRY)/cantaloupe-stage:latest $(ECR_REGISTRY)/cantaloupe-prod:$(DATETIME)
	docker push $(ECR_REGISTRY)/cantaloupe-prod:latest
	docker push $(ECR_REGISTRY)/cantaloupe-prod:$(DATETIME)
	aws ecs update-service --cluster cantaloupe-prod-cluster --service cantaloupe-prod --region us-east-1 --force-new-deployment
