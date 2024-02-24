import React from "react";
import {Form, message, Modal} from "antd";
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormRadio,
  ProFormSelect,
  ProFormText,
  ProFormTextArea
} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import {ModalFormProps} from '@/typings';
import {
  WsArtifactFlinkCDCSelectListParam,
  WsArtifactFlinkJarSelectListParam,
  WsArtifactFlinkSqlSelectListParam,
  WsArtifactSeaTunnelSelectListParam,
  WsFlinkKubernetesDeploymentSelectListParam,
  WsFlinkKubernetesJob,
  WsFlinkKubernetesSessionClusterSelectListParam
} from "@/services/project/typings";
import {DictDataService} from "@/services/admin/dictData.service";
import {WORKSPACE_CONF} from "@/constants/constant";
import {DICT_TYPE} from "@/constants/dictType";
import {DeploymentKind, FlinkJobType} from "@/constants/enum";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {WsArtifactFlinkJarService} from "@/services/project/WsArtifactFlinkJarService";
import {WsArtifactFlinkSqlService} from "@/services/project/WsArtifactFlinkSqlService";
import {WsArtifactSeaTunnelService} from "@/services/project/WsArtifactSeaTunnelService";
import {WsArtifactFlinkCDCService} from "@/services/project/WsArtifactFlinkCDCService";
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
          data.id
            ? WsFlinkKubernetesJobService.update({...values}).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : WsFlinkKubernetesJobService.add({...values, projectId: projectId}).then((response) => {
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
          deploymentKind: data?.deploymentKind?.value,
          flinkDeploymentId: data?.flinkDeployment?.id,
          flinkSessionClusterId: data?.flinkSessionCluster?.id,
          type: data?.type?.value,
          artifactFlinkJarId: data?.artifactFlinkJar?.id,
          artifactFlinkSqlId: data?.artifactFlinkSql?.id,
          artifactFlinkCDCId: data?.artifactFlinkCDC?.id,
          artifactSeaTunnelId: data?.artifactSeaTunnel?.id,
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
          name={"deploymentKind"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.deploymentKind'})}
          rules={[{required: true}]}
          disabled={data?.id ? true : false}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.deploymentKind)}
        />
        <ProFormDependency name={['deploymentKind']}>
          {({deploymentKind}) => {
            if (deploymentKind == DeploymentKind.Deployment) {
              return (
                <ProFormSelect
                  name={"flinkDeploymentId"}
                  label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment'})}
                  rules={[{required: true}]}
                  disabled={data?.id ? true : false}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsFlinkKubernetesDeploymentSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords
                    };
                    return WsFlinkKubernetesDeploymentService.listAll(listParam).then((response) => {
                      if (response.success && response.data) {
                        return response.data.map((item) => {
                          return {
                            label: item.name,
                            value: item.id,
                            item: item
                          };
                        });
                      }
                      return Promise.reject()
                    });
                  }}
                />
              );
            }
            if (deploymentKind == DeploymentKind.SessionCluster) {
              return (
                <ProFormSelect
                  name={"flinkSessionClusterId"}
                  label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster'})}
                  rules={[{required: true}]}
                  disabled={data?.id ? true : false}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsFlinkKubernetesSessionClusterSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords,
                    };
                    return WsFlinkKubernetesSessionClusterService.listAll(listParam).then((response) => {
                      if (response.success && response.data) {
                        return response.data.map((item) => {
                          return {
                            label: item.name,
                            value: item.id,
                            item: item
                          };
                        });
                      }
                      return Promise.reject()
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
          disabled={data?.id ? true : false}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkJobType)}
        />
        <ProFormDependency name={['type']}>
          {({type}) => {
            if (type == FlinkJobType.JAR) {
              return (
                <ProFormSelect
                  name={"artifactFlinkJarId"}
                  label={intl.formatMessage({id: 'pages.project.artifact.jar'})}
                  rules={[{required: true}]}
                  disabled={data?.id ? true : false}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsArtifactFlinkJarSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords
                    };
                    return WsArtifactFlinkJarService.listAll(listParam).then((response) => {
                      if (response.success && response.data) {
                        return response.data.map((item) => {
                          return {
                            label: item.artifact?.name,
                            value: item.id,
                            item: item
                          };
                        });
                      }
                      return Promise.reject()
                    });
                  }}
                />
              );
            }
            if (type == FlinkJobType.SQL) {
              return (
                <ProFormSelect
                  name={"artifactFlinkSqlId"}
                  label={intl.formatMessage({id: 'pages.project.artifact.sql'})}
                  rules={[{required: true}]}
                  disabled={data?.id ? true : false}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsArtifactFlinkSqlSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords,
                    };
                    return WsArtifactFlinkSqlService.listAll(listParam).then((response) => {
                      if (response.success && response.data) {
                        return response.data.map((item) => {
                          return {
                            label: item.artifact?.name,
                            value: item.id,
                            item: item
                          };
                        });
                      }
                      return Promise.reject()
                    });
                  }}
                />
              );
            }
            if (type == FlinkJobType.FLINK_CDC) {
              return (
                <ProFormSelect
                  name={"artifactFlinkCDCId"}
                  label={intl.formatMessage({id: 'pages.project.artifact.cdc'})}
                  rules={[{required: true}]}
                  disabled={data?.id ? true : false}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsArtifactFlinkCDCSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords,
                    };
                    return WsArtifactFlinkCDCService.listAll(listParam).then((response) => {
                      if (response.success && response.data) {
                        return response.data.map((item) => {
                          return {
                            label: item.artifact?.name,
                            value: item.id,
                            item: item
                          };
                        });
                      }
                      return Promise.reject()
                    });
                  }}
                />
              );
            }
            if (type == FlinkJobType.SEATUNNEL) {
              return (
                <ProFormSelect
                  name={"artifactSeaTunnelId"}
                  label={intl.formatMessage({id: 'pages.project.artifact.seatunnel'})}
                  rules={[{required: true}]}
                  disabled={data?.id ? true : false}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsArtifactSeaTunnelSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords,
                    };
                    return WsArtifactSeaTunnelService.listAll(listParam).then((response) => {
                      if (response.success && response.data) {
                        return response.data.map((item) => {
                          return {
                            label: item.artifact?.name,
                            value: item.id,
                            item: item
                          };
                        });
                      }
                      return Promise.reject()
                    });
                  }}
                />
              );
            }
            return (<ProFormGroup/>);
          }}
        </ProFormDependency>

        <ProFormTextArea
          name={"remark"}
          label={intl.formatMessage({id: 'app.common.data.remark'})}
        />
      </ProForm>
    </Modal>
  );
}

export default FlinkKubernetesJobForm;
