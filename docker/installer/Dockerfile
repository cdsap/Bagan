FROM alpine:3.10

ENV SDKMAN_DIR=/root/.sdkman

RUN apk upgrade --update && \
    apk add --no-cache --update libstdc++ curl ca-certificates bash zip unzip openssl && \
    update-ca-certificates && \
    curl -s "https://get.sdkman.io" | bash && \
    rm -rf /var/lib/apt/lists/* && \
    echo "sdkman_auto_answer=true" > $SDKMAN_DIR/etc/config && \
    echo "sdkman_auto_selfupdate=false" >> $SDKMAN_DIR/etc/config && \
    echo "sdkman_insecure_ssl=true" >> $SDKMAN_DIR/etc/config


RUN source $SDKMAN_DIR/bin/sdkman-init.sh \
    && sdk install kotlin 1.3.31 \
    && sdk install kscript




RUN pwd
RUN cat $SDKMAN_DIR/bin/sdkman-init.sh
RUN ls


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
COPY bin/creator/  /usr/local/creator
