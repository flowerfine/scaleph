package cn.sliew.scaleph.workflow.listener.taskinstance;

import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowTaskInstance;
import cn.sliew.scaleph.dao.mapper.master.workflow.WorkflowTaskInstanceMapper;
import cn.sliew.scaleph.workflow.statemachine.WorkflowTaskInstanceStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class WorkflowTaskInstanceSuccessEventListener extends AbstractWorkflowTaskInstanceEventListener {

    @Autowired
    private WorkflowTaskInstanceMapper workflowTaskInstanceMapper;
    @Autowired
    private WorkflowTaskInstanceStateMachine stateMachine;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    protected CompletableFuture handleEventAsync(Long workflowTaskInstanceId) {
        RScheduledExecutorService executorService = redissonClient.getExecutorService("WorkflowTaskInstanceSuccess");
        return (CompletableFuture) executorService.submit(() -> handle(workflowTaskInstanceId));
    }

    private void handle(Long workflowTaskInstanceId) {
        WorkflowTaskInstance record = new WorkflowTaskInstance();
        record.setId(workflowTaskInstanceId);
        record.setEndTime(new Date());
        workflowTaskInstanceMapper.updateById(record);
    }

}
