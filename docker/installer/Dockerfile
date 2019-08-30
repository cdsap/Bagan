FROM openjdk:8

RUN apt-get update
RUN rm /bin/sh && ln -s /bin/bash /bin/sh
RUN apt-get -q -y install curl zip unzip
RUN curl -s https://get.sdkman.io | bash

RUN set -x \
    && echo "sdkman_auto_answer=true" > $SDKMAN_DIR/etc/config \
    && echo "sdkman_auto_selfupdate=false" >> $SDKMAN_DIR/etc/config \
    && echo "sdkman_insecure_ssl=false" >> $SDKMAN_DIR/etc/config

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" \
    && sdk install kotlin 1.3.31

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" \
    && sdk install kscript



RUN curl -sSL https://sdk.cloud.google.com | bash
RUN mkdir /usr/local/gcloud
RUN mkdir /usr/local/k8s
RUN mv /root/google-cloud-sdk /usr/local/gcloud/

ENV PATH $PATH:usr/local/gcloud/google-cloud-sdk/bin

ENV VERSION v2.14.1
ENV FILENAME helm-${VERSION}-linux-amd64.tar.gz
ENV HELM_URL https://storage.googleapis.com/kubernetes-helm/${FILENAME}

RUN gcloud components install kubectl
RUN curl -o /tmp/$FILENAME ${HELM_URL} \
  && tar -zxvf /tmp/${FILENAME} -C /tmp \
  && mv /tmp/linux-amd64/helm /bin/helm \
  && rm -rf /tmp

RUN helm init --client-only
COPY bin/generator/  /usr/local/generator
