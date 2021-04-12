#!/bin/bash
gcloud compute instance-groups managed create doughnut-app-group \
	--base-instance-name doughnut-app-group \
	--size 2 \
	--template doughnut-app-mig-template \
	--zone us-east1-b
