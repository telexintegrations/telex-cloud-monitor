package com.cost.cloud.tracker.Service;
import com.aliyun.cms20190101.Client;
import com.aliyun.cms20190101.models.DescribeMetricListRequest;
import com.aliyun.cms20190101.models.DescribeMetricListResponse;
import com.aliyun.teaopenapi.models.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * CostService fetches monitoring metrics from Alibaba Cloud Monitor API.
 * It retrieves CPU usage, memory, or network traffic data.
 */
@Service
public class CostService {
    private static final Logger logger = LoggerFactory.getLogger(CostService.class);
    private final Client client;

    /**
     * Initializes Alibaba Cloud API client.
     */
    public CostService() throws Exception {
        Config config = new Config()
                .setAccessKeyId(System.getenv("ALIBABA_ACCESS_KEY"))
                .setAccessKeySecret(System.getenv("ALIBABA_SECRET_KEY"))
                .setEndpoint("metrics.cn-hangzhou.aliyuncs.com"); // Change region if needed

        this.client = new Client(config);
    }

    /**
     * Fetches CPU usage from Alibaba Cloud Monitor API.
     *
     * @return DescribeMetricListResponse containing CPU usage data
     */
    public DescribeMetricListResponse getCpuUsage() {
        try {
            DescribeMetricListRequest request = new DescribeMetricListRequest()
                    .setNamespace("acs_ecs_dashboard") // ECS metrics namespace
                    .setMetricName("CPUUtilization") // CPU usage metric
                    .setPeriod("60") // Fetch data every 60 seconds
                     .setLength(String.valueOf(1));


            return client.describeMetricList(request);
        } catch (Exception e) {
            logger.error("Error fetching CPU usage data: ", e);
            return null;
        }
    }
}

