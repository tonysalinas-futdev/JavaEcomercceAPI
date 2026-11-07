package com.example.ecommerce.shared.observability.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.log4j.appender.v2_17.OpenTelemetryAppender;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
class InstallOpenTelemetryAppender implements InitializingBean {

  private final OpenTelemetry openTelemetry;

  InstallOpenTelemetryAppender(OpenTelemetry openTelemetry) {
    this.openTelemetry = openTelemetry;
  }

  @Override
  public void afterPropertiesSet() {
    OpenTelemetryAppender.install(this.openTelemetry);
  }
}
