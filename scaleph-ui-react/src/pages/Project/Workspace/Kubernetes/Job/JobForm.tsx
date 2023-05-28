import {useIntl} from "umi";
import React from "react";
import {Form, message, Modal} from "antd";
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormRadio,
  ProFormSelect,
  ProFormText
} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {
  WsFlinkArtifactJarSelectListParam,
  WsFlinkArtifactSqlSelectListParam,
  WsFlinkKubernetesDeploymentSelectListParam,
  WsFlinkKubernetesJob,
  WsFlinkKubernetesJobAddParam,
  WsFlinkKubernetesSessionClusterSelectListParam
} from "@/services/project/typings";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE, WORKSPACE_CONF} from "@/constant";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {FlinkArtifactJarService} from "@/services/project/flinkArtifactJar.service";
import {FlinkArtifactSqlService} from "@/services/project/WsFlinkArtifactSqlService";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";

const FlinkKubernetesJobForm: React.FC<ModalFormProps<WsFlinkKubernetesJob>> = ({
                                                                                  data,
                                                                                  visible,
                                                                                  onVisibleChange,
                                                                                  onCancel
                                                                                }) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.project.flink.kubernetes.job'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project.flink.kubernetes.job'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: WsFlinkKubernetesJobAddParam = {...values, projectId: projectId};
          data.id
            ? WsFlinkKubernetesJobService.add(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : WsFlinkKubernetesJobService.add(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            });
        });
      }}
    >
      <ProForm
        form={form}
        layout={"horizontal"}
        submitter={false}
        labelCol={{span: 6}}
        wrapperCol={{span: 16}}
        initialValues={{
          id: data?.id,
          name: data?.name,
          executionMode: data?.executionMode?.value,
          flinkDeploymentMode: data?.flinkDeploymentMode?.value,
          flinkDeploymentId: data?.flinkDeploymentId,
          flinkSessionClusterId: data?.flinkSessionClusterId,
          type: data?.type?.value,
          flinkArtifactJarId: data?.flinkArtifactJarId,
          flinkArtifactSqlId: data?.flinkArtifactSqlId,
          remark: data?.remark
        }}
      >
        <ProFormDigit name={"id"} hidden/>
        <ProFormText
          name={"name"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.name'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={"executionMode"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.executionMode'})}
          rules={[{required: true}]}
          allowClear={false}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkRuntimeExecutionMode)}
        />
        <ProFormRadio.Group
          name={"flinkDeploymentMode"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.flinkDeploymentMode'})}
          rules={[{required: true}]}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkDeploymentMode)}
        />
        <ProFormDependency name={['flinkDeploymentMode']}>
          {({flinkDeploymentMode}) => {
            if (flinkDeploymentMode == '0') {
              return (
                <ProFormSelect
                  name={"flinkDeploymentId"}
                  label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment'})}
                  rules={[{required: true}]}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsFlinkKubernetesDeploymentSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords
                    };
                    return WsFlinkKubernetesDeploymentService.listAll(listParam).then((response) => {
                      return response.map((item) => {
                        return {
                          label: item.name,
                          value: item.id,
                          item: item
                        };
                      });
                    });
                  }}
                />
              );
            }
            if (flinkDeploymentMode == '1') {
              return (<ProFormGroup/>);
            }
            if (flinkDeploymentMode == '2') {
              return (
                <ProFormSelect
                  name={"flinkSessionClusterId"}
                  label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster'})}
                  rules={[{required: true}]}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsFlinkKubernetesSessionClusterSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords,
                    };
                    return WsFlinkKubernetesSessionClusterService.listAll(listParam).then((response) => {
                      return response.map((item) => {
                        return {
                          label: item.name,
                          value: item.id,
                          item: item
                        };
                      });
                    });
                  }}
                />
              );
            }
            return (<ProFormGroup/>);
          }}
        </ProFormDependency>

        <ProFormRadio.Group
          name={"type"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.type'})}
          rules={[{required: true}]}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkJobType)}
        />
        <ProFormDependency name={['type']}>
          {({type}) => {
            if (type == '0') {
              return (
                <ProFormSelect
                  name={"flinkArtifactJarId"}
                  label={intl.formatMessage({id: 'pages.project.job.jar'})}
                  rules={[{required: true}]}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsFlinkArtifactJarSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords
                    };
                    return FlinkArtifactJarService.listAll(listParam).then((response) => {
                      return response.map((item) => {
                        return {
                          label: item.wsFlinkArtifact.name,
                          value: item.id,
                          item: item
                        };
                      });
                    });
                  }}
                />
              );
            }
            if (type == '1') {
              return (
                <ProFormSelect
                  name={"flinkArtifactSqlId"}
                  label={intl.formatMessage({id: 'pages.project.job.sql'})}
                  rules={[{required: true}]}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsFlinkArtifactSqlSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords,
                    };
                    return FlinkArtifactSqlService.listAll(listParam).then((response) => {
                      return response.map((item) => {
                        return {
                          label: item.wsFlinkArtifact.name,
                          value: item.id,
                          item: item
                        };
                      });
                    });
                  }}
                />
              );
            }
            if (type == '2') {
              return (<ProFormGroup/>);
            }
            return (<ProFormGroup/>);
          }}
        </ProFormDependency>

        <ProFormText
          name={"remark"}
          label={intl.formatMessage({id: 'app.common.data.remark'})}
        />
      </ProForm>
    </Modal>
  );
}

export default FlinkKubernetesJobForm;
