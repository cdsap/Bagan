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


RUN echo $SDKMAN_DIR
RUN cd $SDKMAN_DIR
RUN cd /usr/share/sdkman
RUN pwd
RUN cat $SDKMAN_DIR/bin/sdkman-init.sh
RUN ls



RUN source $SDKMAN_DIR/bin/sdkman-init.sh \
    && sdk install kotlin 1.3.31 \
    && sdk install kscript

RUN echo $SDKMAN_DIR
RUN cd $SDKMAN_DIR
RUN cd /usr/share/sdkman
RUN pwd
RUN cat $SDKMAN_DIR/bin/sdkman-init.sh
RUN ls


ENV ANDROID_HOME /opt/android-sdk-linux
RUN echo ANDROID_HOME
RUN mkdir -p ${ANDROID_HOME} && \
    cd ${ANDROID_HOME} && \
    wget -q https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip -O android_tools.zip && \
    unzip android_tools.zip && \
    rm android_tools.zip

ENV PATH ${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/tools/bin:${ANDROID_HOME}/platform-tools

RUN yes | sdkmanager --licenses


RUN sdkmanager 'platform-tools'
RUN sdkmanager 'platforms;android-28'
RUN sdkmanager 'build-tools;28.0.3'
RUN sdkmanager 'extras;m2repository;com;android;support;constraint;constraint-layout-solver;1.0.2'
RUN sdkmanager 'extras;m2repository;com;android;support;constraint;constraint-layout;1.0.2'
RUN sdkmanager 'extras;google;m2repository'
RUN sdkmanager 'extras;android;m2repository'
RUN sdkmanager 'extras;google;google_play_services'


RUN mkdir -p /var/workspace
RUN cd /var/workspace
COPY bin/properties/RewriteProperties.kt /var/workspace
COPY bin/injector/TalaiotInjector.kt /var/workspace
COPY bin/Bootstraping.kt /var/workspace
WORKDIR "var/workspace/"
