import {FooterToolbar, ProCard, ProForm, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {Col, Form, message, Row} from "antd";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE, RESOURCE_TYPE} from "@/constant";
import {ResourceListParam} from "@/services/resource/typings";
import {list} from "@/services/resource/resource.service";
import {FlinkClusterConfig} from "@/services/dev/typings";
import {add, getData, setData, update} from "@/services/dev/flinkClusterConfig.service";
import {history, useIntl, useLocation} from 'umi';
import {Dict} from '@/app.d';
import HighAvailability from "@/pages/DEV/ClusterConfigOptions/components/HA";
import Resource from "@/pages/DEV/ClusterConfigOptions/components/Resource";
import FaultTolerance from "@/pages/DEV/ClusterConfigOptions/components/FaultTolerance";
import Additional from "@/pages/DEV/ClusterConfigOptions/components/Additional";
import State from "@/pages/DEV/ClusterConfigOptions/components/State";

const DevBatchJob: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const [form] = Form.useForm();

  const params = urlParams.state as FlinkClusterConfig;

  const configOptions = new Map(Object.entries(params?.configOptions ? params?.configOptions : {}))

  const data = {
    name: params?.name,
    deployMode: params?.deployMode?.value,
    flinkVersion: params?.flinkVersion?.value,
    flinkRelease: params?.flinkRelease?.id,
    resourceProvider: params?.resourceProvider?.value,
    clusterCredential: params?.clusterCredential?.id,
    remark: params?.remark,
  }

  setData(form, configOptions)

  return (<div>
    <ProForm
      form={form}
      layout={"horizontal"}
      initialValues={data}
      grid={true}
      rowProps={{gutter: [16, 8]}}
      submitter={{
        render: (props, doms) => {
          return (
            <Row>
              <Col>
                <FooterToolbar>{doms}</FooterToolbar>
              </Col>
            </Row>
          )
        },
        searchConfig: {
          resetText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
          submitText: intl.formatMessage({id: 'app.common.operate.submit.label'})
        },
        onReset: () => {
          history.back();
        },
      }}
      onFinish={(value) => {
        const param: FlinkClusterConfig = {
          id: params?.id,
          name: value['name'],
          flinkVersion: {value: value['flinkVersion']},
          resourceProvider: {value: value['resourceProvider']},
          deployMode: {value: value['deployMode']},
          flinkRelease: {id: value['flinkRelease']},
          clusterCredential: {id: value['clusterCredential']},
          remark: value['remark'],
          configOptions: getData(value)
        }
        return params?.id ?
          update(param).then((d) => {
            if (d.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
            } else {
              message.error(d.errorMessage);
            }
          })
            .catch(() => {
              message.error(intl.formatMessage({id: 'app.common.operate.new.failure'}));
            })
            .finally(() => {
              history.back();
            })
          : add(param).then((d) => {
            if (d.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
            } else {
              message.error(d.errorMessage);
            }
          })
            .catch(() => {
              message.error(intl.formatMessage({id: 'app.common.operate.new.failure'}));
            })
            .finally(() => {
              history.back();
            });
      }}
    >
      <ProCard
        title={'Basic'}
        headerBordered
        collapsible={true}
      >
        <ProForm.Group>
          <ProFormText
            name="name"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.name'})}
            colProps={{span: 10, offset: 1}}
            rules={[
              {required: true},
              {max: 30},
              {
                pattern: /^[\w\s_]+$/,
                message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
              },
            ]}
          />
          <ProFormSelect
            name="deployMode"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'})}
            colProps={{span: 10, offset: 1}}
            rules={[{required: true}]}
            showSearch={true}
            dependencies={['resourceProvider']}
            request={(params) => {
              if (params.resourceProvider == '0') {
                const dict: Dict = {label: 'Session', value: '2'}
                return Promise.resolve([dict])
              }
              return listDictDataByType(DICT_TYPE.flinkDeploymentMode)
            }}
          />
          <ProFormSelect
            name="flinkVersion"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkVersion'})}
            colProps={{span: 10, offset: 1}}
            rules={[{required: true}]}
            showSearch={true}
            request={() => listDictDataByType(DICT_TYPE.flinkVersion)}
          />
          <ProFormSelect
            name="flinkRelease"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkRelease'})}
            colProps={{span: 10, offset: 1}}
            rules={[{required: true}]}
            showSearch={true}
            dependencies={['flinkVersion']}
            request={(params) => {
              const resourceParam: ResourceListParam = {
                resourceType: RESOURCE_TYPE.flinkRelease,
                label: params.flinkVersion
              }
              return list(resourceParam).then((response) => {
                return response.records.map((item) => {
                  return {label: item.fileName, value: item.id}
                })
              })
            }}
          />
          <ProFormSelect
            name="resourceProvider"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.resourceProvider'})}
            colProps={{span: 10, offset: 1}}
            rules={[{required: true}]}
            showSearch={true}
            request={() => listDictDataByType(DICT_TYPE.flinkResourceProvider)}
          />
          <ProFormSelect
            name="clusterCredential"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.clusterCredential'})}
            colProps={{span: 10, offset: 1}}
            rules={[{required: true}]}
            showSearch={true}
            dependencies={['resourceProvider']}
            request={(params) => {
              const resourceParam: ResourceListParam = {
                resourceType: RESOURCE_TYPE.clusterCredential,
                label: params.resourceProvider
              }
              return list(resourceParam).then((response) => {
                return response.records.map((item) => {
                  return {label: item.name, value: item.id}
                })
              })
            }}
          />
          <ProFormText
            name="remark"
            label={intl.formatMessage({id: 'pages.dev.remark'})}
            colProps={{span: 10, offset: 1}}
            rules={[{max: 200}]}
          />
        </ProForm.Group>
      </ProCard>
      <State/>
      <FaultTolerance/>
      <HighAvailability/>
      <Resource/>
      <Additional/>
    </ProForm>
  </div>);
}

export default DevBatchJob;
