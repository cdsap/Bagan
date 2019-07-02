FROM openjdk:8

RUN set -x \
    && apt-get update \
    && apt-get install -y curl wget unzip zip gzip vim ca-certificates --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

ENV SDKMAN_DIR /usr/share/sdkman

SHELL [ "/bin/bash", "-c" ]

RUN curl 'https://get.sdkman.io' | bash

RUN set -x \
    && echo "sdkman_auto_answer=true" > $SDKMAN_DIR/etc/config \
    && echo "sdkman_auto_selfupdate=false" >> $SDKMAN_DIR/etc/config \
    && echo "sdkman_insecure_ssl=false" >> $SDKMAN_DIR/etc/config

RUN source $SDKMAN_DIR/bin/sdkman-init.sh \
	&& sdk install kotlin \
	&& sdk install kscript
RUN curl -sSL https://sdk.cloud.google.com | bash
RUN mkdir /usr/local/gcloud
RUN mv /root/google-cloud-sdk /usr/local/gcloud/
ENV PATH $PATH:usr/local/gcloud/google-cloud-sdk/bin

# Install Helm
ENV VERSION v2.14.1

ENV FILENAME helm-${VERSION}-linux-amd64.tar.gz
ENV HELM_URL https://storage.googleapis.com/kubernetes-helm/${FILENAME}


RUN echo $HELM_URL
RUN gcloud components install kubectl
RUN curl -o /tmp/$FILENAME ${HELM_URL} \
  && tar -zxvf /tmp/${FILENAME} -C /tmp \
  && mv /tmp/linux-amd64/helm /bin/helm \
  && rm -rf /tmp

RUN helm init --client-only
COPY values.yaml /usr/local
COPY CreateHelmPods.kt  /usr/local/
