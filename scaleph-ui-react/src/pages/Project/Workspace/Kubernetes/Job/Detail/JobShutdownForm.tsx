import React from "react";
import {message} from "antd";
import {ModalForm, ProFormSwitch} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import {ModalFormProps} from '@/typings';
import {WsFlinkKubernetesJob, WsFlinkKubernetesJobInstanceShutdownParam} from "@/services/project/typings";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";

const FlinkKubernetesJobShutdownForm: React.FC<ModalFormProps<WsFlinkKubernetesJob>> = ({
                                                                                          data,
                                                                                          visible,
                                                                                          onVisibleChange,
                                                                                          onCancel
                                                                                        }) => {
  const intl = useIntl();

  return (
    <ModalForm
      open={visible}
      title={intl.formatMessage({id: 'app.common.operate.shutdown.label'}) + ' ' + data.name + '?'}
      width={'33%'}
      layout={"horizontal"}
      grid={true}
      modalProps={{
        destroyOnClose: true,
        onCancel: onCancel,
      }}
      onFinish={(values) => {
        const param: WsFlinkKubernetesJobInstanceShutdownParam = {
          id: data.jobInstance?.id,
          savepoint: values.savepoint,
          drain: values.drain
        }
        return WsFlinkKubernetesJobService.shutdown(param).then((response) => {
          if (response.success) {
            message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
            if (onVisibleChange) {
              onVisibleChange(false);
            }
          }
        })
      }}
    >
      <ProFormSwitch
        name={"savepoint"}
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.shutdown.savepoint'})}
        colProps={{span: 21, offset: 3}}
        initialValue={false}
      />
      <ProFormSwitch
        name={"drain"}
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.shutdown.drain'})}
        colProps={{span: 21, offset: 3}}
        initialValue={false}
      />
    </ModalForm>
  );
}

export default FlinkKubernetesJobShutdownForm;
