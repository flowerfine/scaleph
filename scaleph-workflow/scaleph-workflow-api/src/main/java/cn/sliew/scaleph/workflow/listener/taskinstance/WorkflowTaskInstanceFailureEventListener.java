package cn.sliew.scaleph.workflow.listener.taskinstance;

import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.statemachine.WorkflowTaskInstanceStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class WorkflowTaskInstanceFailureEventListener extends AbstractWorkflowTaskInstanceEventListener {

    @Autowired
    private WorkflowTaskInstanceService workflowTaskInstanceService;
    @Autowired
    private WorkflowTaskInstanceStateMachine stateMachine;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    protected CompletableFuture handleEventAsync(Long workflowTaskInstanceId) {
        RScheduledExecutorService executorService = redissonClient.getExecutorService("WorkflowTaskInstanceExecute");
        return (CompletableFuture) executorService.submit(() -> log.info("workflow task instance on deploy"));
    }
}
