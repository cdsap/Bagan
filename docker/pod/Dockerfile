FROM openjdk:11-jdk

RUN apt-get update
RUN rm /bin/sh && ln -s /bin/bash /bin/sh
RUN apt-get -q -y install curl zip unzip sudo

RUN groupadd --gid 1714 bagan \
  && useradd --uid 1714 --gid bagan --shell /bin/bash --create-home bagan \
  && echo 'bagan ALL=NOPASSWD: ALL' >> /etc/sudoers.d/bagan \
  && echo 'Defaults    env_keep += "DEBIAN_FRONTEND"' >> /etc/sudoers.d/env_keep


USER bagan
ENV PATH /home/bagan/.local/bin:/home/bagan/bin:${PATH}

CMD ["/bin/sh"]
ENV HOME /home/bagan
RUN curl -s https://get.sdkman.io | bash
RUN echo "sdkman_auto_complete=false" >> "$HOME/.sdkman/etc/config"
RUN chmod a+x "$HOME/.sdkman/bin/sdkman-init.sh"
RUN source "$HOME/.sdkman/bin/sdkman-init.sh"

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" \
    && sdk install kotlin 1.4.21 \
    && sdk install kscript 3.1.0 \
    && sdk install java 11.0.11.hs-adpt

ARG sdk_version=sdk-tools-linux-4333796.zip
ARG cmdline_tools=https://dl.google.com/android/repository/commandlinetools-linux-6609375_latest.zip

ARG android_home=/opt/android/sdk

RUN sudo apt-get update && \
    sudo apt-get install --yes \
        xvfb lib32z1 lib32stdc++6 build-essential \
        libcurl4-openssl-dev libglu1-mesa libxi-dev libxmu-dev \
        libglu1-mesa-dev && \
    sudo rm -rf /var/lib/apt/lists/*


RUN sudo mkdir -p ${android_home}/cmdline-tools && \
    sudo chown -R bagan:bagan ${android_home} && \
    wget -O /tmp/cmdline-tools.zip -t 5 "${cmdline_tools}" && \
    unzip -q /tmp/cmdline-tools.zip -d ${android_home}/cmdline-tools && \
    rm /tmp/cmdline-tools.zip

ENV ANDROID_HOME ${android_home}
ENV ANDROID_SDK_ROOT ${android_home}
ENV ADB_INSTALL_TIMEOUT 120
ENV PATH=${ANDROID_SDK_ROOT}/emulator:${ANDROID_SDK_ROOT}/cmdline-tools/tools/bin:${ANDROID_SDK_ROOT}/tools:${ANDROID_SDK_ROOT}/tools/bin:${ANDROID_SDK_ROOT}/platform-tools:${PATH}

RUN mkdir ~/.android && echo '### User Sources for Android SDK Manager' > ~/.android/repositories.cfg

RUN yes | sdkmanager --licenses && yes | sdkmanager --update

RUN sdkmanager \
  "tools" \
  "platform-tools" \
  "emulator"

RUN sdkmanager \
  "build-tools;27.0.0" \
  "build-tools;27.0.1" \
  "build-tools;27.0.2" \
  "build-tools;27.0.3" \
  # 28.0.0 is failing to download from Google for some reason
  #"build-tools;28.0.0" \
  "build-tools;28.0.1" \
  "build-tools;28.0.2" \
  "build-tools;28.0.3" \
  "build-tools;29.0.0" \
  "build-tools;29.0.1" \
  "build-tools;29.0.2" \
  "build-tools;29.0.3" \
  "build-tools;30.0.0" \
  "build-tools;30.0.1" \
  "build-tools;30.0.2"

# API_LEVEL string gets replaced by m4
RUN sdkmanager "platforms;android-23"

RUN mkdir -p /home/bagan/workspace
RUN cd /home/bagan/workspace
COPY bin/experiments/ /home/bagan/workspace
WORKDIR "/home/bagan/workspace/"
