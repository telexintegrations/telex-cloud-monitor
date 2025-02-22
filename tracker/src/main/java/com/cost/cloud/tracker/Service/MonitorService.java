package com.cost.cloud.tracker.Service;

import com.aliyun.cms20190101.Client;
import com.aliyun.cms20190101.models.DescribeMetricListRequest;
import com.aliyun.cms20190101.models.DescribeMetricListResponse;
import com.aliyun.teaopenapi.models.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * MonitorService fetches system metrics (CPU, Memory, Network) and sends alerts to Telex.
 */
@Service
public class MonitorService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorService.class);
    private final Client client;
    private final TelexService telexService; // Inject TelexService

    /**
     * Initializes Alibaba Cloud API client.
     */
    public MonitorService(TelexService telexService) throws Exception {
        Config config = new Config()
                .setAccessKeyId(System.getenv("ALIBABA_ACCESS_KEY"))
                .setAccessKeySecret(System.getenv("ALIBABA_SECRET_KEY"))
                .setEndpoint("metrics.cn-hangzhou.aliyuncs.com"); // Change region if needed

        this.client = new Client(config);
        this.telexService = telexService;
    }

    /**
     * Fetches CPU usage.
     */
    public DescribeMetricListResponse getCpuUsage() {
        return fetchMetric("CPUUtilization");
    }

    /**
     * Fetches Memory usage.
     */
    public DescribeMetricListResponse getMemoryUsage() {
        return fetchMetric("memory_usedutilization");
    }

    /**
     * Fetches Network Traffic data.
     */
    public DescribeMetricListResponse getNetworkTraffic() {
        return fetchMetric("networkin_rate");
    }

    /**
     * Generic method to fetch any metric.
     */
    private DescribeMetricListResponse fetchMetric(String metricName) {
        try {
            DescribeMetricListRequest request = new DescribeMetricListRequest()
                    .setNamespace("acs_ecs_dashboard")
                    .setMetricName(metricName)
                    .setPeriod("60")
                    .setLength("1");

            return client.describeMetricList(request);
        } catch (Exception e) {
            logger.error("Error fetching " + metricName + ": ", e);
            return null;
        }
    }

    /**
     * Scheduled task: Runs every 60 seconds to fetch metrics and send alerts.
     */
    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void scheduledMonitoringTask() {
        logger.info("Running scheduled monitoring task...");

        DescribeMetricListResponse cpuUsage = getCpuUsage();
        DescribeMetricListResponse memoryUsage = getMemoryUsage();
        DescribeMetricListResponse networkTraffic = getNetworkTraffic();

        // Send data to Telex if not null
        if (cpuUsage != null && memoryUsage != null && networkTraffic != null) {
            String message = "CPU: " + cpuUsage + ", Memory: " + memoryUsage + ", Network: " + networkTraffic;
            telexService.sendAlert(message);
            logger.info("Metrics sent to Telex: " + message);
        } else {
            logger.warn("Skipping Telex alert due to missing data.");
        }
    }
}
