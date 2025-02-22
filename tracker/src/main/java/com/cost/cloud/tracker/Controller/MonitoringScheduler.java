package com.cost.cloud.tracker.Controller;

import com.aliyun.cms20190101.models.DescribeMetricListResponse;
import com.cost.cloud.tracker.Service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodically fetches monitoring data from Alibaba Cloud Monitor API.
 */
@Component
public class MonitoringScheduler {
    private static final Logger logger = LoggerFactory.getLogger(MonitoringScheduler.class);
    private final MonitorService monitorService;

    /**
     * Injects MonitorService into scheduler.
     */
    public MonitoringScheduler(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    /**
     * Scheduled job to fetch and log system metrics every 5 minutes.
     */
    @Scheduled(fixedRate = 300000) // Runs every 5 minutes
    public void fetchMetrics() {
        logger.info("Fetching system metrics from Alibaba Cloud...");

        DescribeMetricListResponse cpuUsage = monitorService.getCpuUsage();
        DescribeMetricListResponse memoryUsage = monitorService.getMemoryUsage();
        DescribeMetricListResponse networkTraffic = monitorService.getNetworkTraffic();

        if (cpuUsage != null) {
            logger.info("CPU Usage Data: {}", cpuUsage.getBody().getDatapoints());
        }
        if (memoryUsage != null) {
            logger.info("Memory Usage Data: {}", memoryUsage.getBody().getDatapoints());
        }
        if (networkTraffic != null) {
            logger.info("Network Traffic Data: {}", networkTraffic.getBody().getDatapoints());
        }
    }
}
