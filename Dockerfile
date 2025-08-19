# Build APK
FROM gradle:8.9-jdk17 AS builder

WORKDIR /app

# Copy project
COPY . .

# Build app
RUN gradle assembleDebug --no-daemon

# Serve APK
FROM nginx:alpine

WORKDIR /usr/share/nginx/html

# Copy APK into server root
COPY --from=builder /app/app/build/outputs/apk/debug/app-debug.apk ./vacation-planner.apk

# Expose
EXPOSE 80