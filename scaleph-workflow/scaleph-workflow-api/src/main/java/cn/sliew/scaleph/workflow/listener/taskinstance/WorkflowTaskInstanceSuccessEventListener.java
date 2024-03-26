package cn.sliew.scaleph.workflow.listener.taskinstance;

import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowTaskInstance;
import cn.sliew.scaleph.dao.mapper.master.workflow.WorkflowTaskInstanceMapper;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskInstanceDTO;
import cn.sliew.scaleph.workflow.statemachine.WorkflowTaskInstanceStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RExecutorFuture;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class WorkflowTaskInstanceSuccessEventListener implements WorkflowTaskInstanceEventListener {

    @Autowired
    private WorkflowTaskInstanceMapper workflowTaskInstanceMapper;
    @Autowired
    private WorkflowTaskInstanceStateMachine stateMachine;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void onEvent(WorkflowTaskInstanceEventDTO event) {
        RScheduledExecutorService executorService = redissonClient.getExecutorService("WorkflowTaskInstanceSuccess");
        RExecutorFuture<?> future = executorService.submit(() -> handle(event.getWorkflowTaskInstanceDTO()));
        future.whenComplete((unused, throwable) -> {
            if (throwable != null) {
                stateMachine.onFailure(event.getWorkflowTaskInstanceDTO(), throwable);
            } else {
                stateMachine.onSuccess(event.getWorkflowTaskInstanceDTO());
            }
        });
    }

    private void handle(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        WorkflowTaskInstance record = new WorkflowTaskInstance();
        record.setId(workflowTaskInstanceDTO.getId());
        record.setEndTime(new Date());
        workflowTaskInstanceMapper.updateById(record);
    }

}
