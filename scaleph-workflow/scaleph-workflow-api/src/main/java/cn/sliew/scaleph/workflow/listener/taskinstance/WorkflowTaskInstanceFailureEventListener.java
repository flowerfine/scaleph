package cn.sliew.scaleph.workflow.listener.taskinstance;

import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.statemachine.WorkflowTaskInstanceStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RExecutorFuture;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkflowTaskInstanceFailureEventListener implements WorkflowTaskInstanceEventListener {

    @Autowired
    private WorkflowTaskInstanceService workflowTaskInstanceService;
    @Autowired
    private WorkflowTaskInstanceStateMachine stateMachine;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void onEvent(WorkflowTaskInstanceEventDTO event) {
        RScheduledExecutorService executorService = redissonClient.getExecutorService("WorkflowTaskInstanceExecute");
        RExecutorFuture<?> future = executorService.submit(() -> log.info("workflow task instance on deploy"));
        future.whenComplete((unused, throwable) -> {
           if (throwable != null) {
               stateMachine.onFailure(event.getWorkflowTaskInstanceDTO(), throwable);
           } else {
               stateMachine.onSuccess(event.getWorkflowTaskInstanceDTO());
           }
        });
    }
}
