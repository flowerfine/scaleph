package cn.sliew.scaleph.workflow.listener.taskinstance;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.statemachine.WorkflowTaskInstanceStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class AbstractWorkflowTaskInstanceEventListener implements WorkflowTaskInstanceEventListener, InitializingBean, BeanFactoryAware {

    private BeanFactory beanFactory;
    protected RScheduledExecutorService executorService;

    @Autowired
    private WorkflowTaskInstanceService workflowTaskInstanceService;
    @Autowired
    private WorkflowTaskInstanceStateMachine stateMachine;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = redissonClient.getExecutorService(WorkflowTaskInstanceStateMachine.EXECUTOR);
        executorService.registerWorkers(WorkerOptions.defaults().beanFactory(beanFactory));
    }

    @Override
    public void onEvent(WorkflowTaskInstanceEventDTO event) {
        try {
            CompletableFuture<?> future = handleEventAsync(event.getWorkflowTaskInstanceId());
            future.whenComplete((unused, throwable) -> {
                log.info("workflow task instance end, {}", JacksonUtil.toJsonString(event), throwable);
                if (throwable != null) {
                    onFailure(event.getWorkflowTaskInstanceId(), throwable);
                } else {
                    workflowTaskInstanceService.updateState(event.getWorkflowTaskInstanceId(), event.getState(), event.getNextState(), null);
                }
            });
        } catch (Throwable throwable) {
            onFailure(event.getWorkflowTaskInstanceId(), throwable);
        }
    }

    protected void onFailure(Long workflowTaskInstanceId, Throwable throwable) {
        stateMachine.onFailure(workflowTaskInstanceService.get(workflowTaskInstanceId), throwable);
    }

    protected abstract CompletableFuture handleEventAsync(Long workflowTaskInstanceId);
}
