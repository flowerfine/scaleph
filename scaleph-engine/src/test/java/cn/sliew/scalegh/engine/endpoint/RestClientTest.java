package cn.sliew.scalegh.engine.endpoint;

import cn.sliew.flinkful.rest.base.JobClient;
import cn.sliew.flinkful.rest.base.RestClient;
import cn.sliew.flinkful.rest.client.FlinkRestClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.runtime.rest.messages.job.savepoints.SavepointTriggerRequestBody;
import org.apache.flink.runtime.rest.messages.job.savepoints.stop.StopWithSavepointRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

/**
 * cancel 有 2 种方式：是否创建 savepoint。
 * stop 等同于带有 savepoint 的 cancel。
 * cancel 任务状态为 canceled，而 stop 任务状态为 finished
 */
class RestClientTest {

    private RestClient client;

    @BeforeEach
    void setUp() {
        Configuration configuration = GlobalConfiguration.loadConfiguration();
//        configuration.setString(RestOptions.ADDRESS, "localhost");
//        configuration.setInteger(RestOptions.PORT, 8081);

        this.client = new FlinkRestClient("localhost", 8081, configuration);
    }

    @Test
    void testStop() throws Exception {
        JobClient jobClient = client.job();
        StopWithSavepointRequestBody requestBody = new StopWithSavepointRequestBody("/Users/wangqi/Downloads/savepoint", true);
        final CompletableFuture<TriggerResponse> future = jobClient.jobStop("39b6a5988f1a2427ead8d86cb05e46c4", requestBody);
        // fixme 很奇怪，必须调用一下才能取消任务
        future.get();
    }

    @Test
    void testCancelWithSavepoint() throws Exception {
        JobClient jobClient = client.job();
        SavepointTriggerRequestBody requestBody = new SavepointTriggerRequestBody("/Users/wangqi/Downloads/savepoint", true);
        jobClient.jobSavepoint("2c57f1f010c464625ad953e25bd945f5", requestBody);
    }

    @Test
    void testCancelWithoutSavepoint() throws Exception {
        JobClient jobClient = client.job();
        final CompletableFuture<EmptyResponseBody> future = jobClient.jobTerminate("b9f4f2411cd946dbf8c8923012aa4010", "cancel");
        // fixme 很奇怪，必须调用一下才能取消任务
        future.get();
    }
}
