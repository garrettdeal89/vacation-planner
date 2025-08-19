# Build APK
FROM gradle:8.9-jdk17 AS builder

# Install tools
RUN apt-get update && apt-get install -y wget unzip git && rm -rf /var/lib/apt/lists/*

# Set Android SDKs
ENV ANDROID_SDK_ROOT=/sdk
ENV PATH=$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools:$PATH

WORKDIR /app

# Copy project
COPY . .

# install Android tools
RUN mkdir -p $ANDROID_SDK_ROOT/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O sdk-tools.zip && \
    unzip sdk-tools.zip -d $ANDROID_SDK_ROOT/cmdline-tools && \
    rm sdk-tools.zip && \
    mv $ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools $ANDROID_SDK_ROOT/cmdline-tools/latest

RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.2"

# Gradle wrapper
RUN chmod +x ./gradlew

# Build APK
RUN ./gradlew assembleDebug --no-daemon

# Serve APK
FROM nginx:alpine

WORKDIR /usr/share/nginx/html

# Copy APK
COPY --from=builder /app/app/build/outputs/apk/debug/app-debug.apk ./vacation-planner.apk

EXPOSE 80