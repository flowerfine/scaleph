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
  ProFormText,
  ProFormTextArea
} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {
  WsDiJobSelectListParam,
  WsFlinkArtifactJarSelectListParam,
  WsFlinkArtifactSqlSelectListParam,
  WsFlinkKubernetesDeploymentSelectListParam,
  WsFlinkKubernetesJob,
  WsFlinkKubernetesSessionClusterSelectListParam
} from "@/services/project/typings";
import {DictDataService} from "@/services/admin/dictData.service";
import {WORKSPACE_CONF} from "@/constants/constant";
import {DICT_TYPE} from "@/constants/dictType";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {FlinkArtifactJarService} from "@/services/project/flinkArtifactJar.service";
import {FlinkArtifactSqlService} from "@/services/project/WsFlinkArtifactSqlService";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";
import {WsDiJobService} from "@/services/project/WsDiJobService";

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
          flinkArtifactJarId: data?.flinkArtifactJar?.id,
          flinkArtifactSqlId: data?.flinkArtifactSql?.id,
          wsDiJobId: data?.wsDiJob?.id,
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
          disabled={data?.id}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.deploymentKind)}
        />
        <ProFormDependency name={['deploymentKind']}>
          {({deploymentKind}) => {
            if (deploymentKind == 'FlinkDeployment') {
              return (
                <ProFormSelect
                  name={"flinkDeploymentId"}
                  label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment'})}
                  rules={[{required: true}]}
                  disabled={data?.id}
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
            if (deploymentKind == 'FlinkSessionJob') {
              return (
                <ProFormSelect
                  name={"flinkSessionClusterId"}
                  label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster'})}
                  rules={[{required: true}]}
                  disabled={data?.id}
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
          disabled={data?.id}
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
                  disabled={data?.id}
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
                  disabled={data?.id}
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
              return (
                <ProFormSelect
                  name={"wsDiJobId"}
                  label={intl.formatMessage({id: 'pages.project.job.seatunnel'})}
                  rules={[{required: true}]}
                  disabled={data?.id}
                  allowClear={false}
                  showSearch={true}
                  request={(params, props) => {
                    const listParam: WsDiJobSelectListParam = {
                      projectId: projectId,
                      name: params.keyWords,
                    };
                    return WsDiJobService.listAll(listParam).then((response) => {
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
