package controllers;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import play.mvc.Controller;
import play.mvc.Result;

public class PingController extends Controller {

    public Result pingPong() {
        return ok();
    }

    public static PrometheusMeterRegistry mr;

    public Result prometheus() {
        synchronized (PingController.class) {
            if (mr == null) {
                mr = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
                new JvmMemoryMetrics().bindTo(mr);
                new JvmGcMetrics().bindTo(mr);
                new LogbackMetrics().bindTo(mr);
                new ProcessorMetrics().bindTo(mr);
                new JvmThreadMetrics().bindTo(mr);
                Metrics.addRegistry(mr);
            }
        }

        return ok(mr.scrape()).as("text/plain");
    }
}

