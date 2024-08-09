import {useIntl} from "@umijs/max";
import React from "react";
import {
  ProCard,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormText
} from "@ant-design/pro-components";
import {DICT_TYPE} from "@/constants/dictType";
import {SysDictService} from "@/services/admin/system/sysDict.service";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const RedisForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "host"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.redis.host'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"localhost"}
        />
        <ProFormDigit
          name={[prefix, "port"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.redis.port'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={6379}
          fieldProps={{
            min: 0,
            max: 65535
          }}
        />
        <ProFormText
          name={[prefix, "user"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.redis.user'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name={[prefix, "password"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.redis.password'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormSelect
          name={[prefix, "mode"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.redis.mode'})}
          colProps={{span: 21, offset: 1}}
          request={() => SysDictService.listDictByDefinition(DICT_TYPE.carpDataSourceRedisMode)}
        />
        <ProFormDependency name={[[prefix,'mode']]}>
          {(depValues) => {
            const prefixValue = depValues[prefix]
            const mode = prefixValue['mode']
            if (mode == 'cluster') {
              return (
                <ProFormGroup
                  title={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.redis.nodes'})}
                  colProps={{span: 21, offset: 1}}
                >
                  <ProFormList
                    name={[prefix, "nodes"]}
                    copyIconProps={false}
                    creatorButtonProps={{
                      creatorButtonText: intl.formatMessage({id: 'pages.metadata.dataSource.step.props.redis.nodes.list'}),
                      type: 'text',
                    }}
                    colProps={{span: 24, offset: 3}}
                  >
                    <ProFormGroup>
                      <ProFormText
                        name={"node"}
                        colProps={{span: 16, offset: 4}}
                      />
                    </ProFormGroup>
                  </ProFormList>
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
      </ProCard>
    </div>
  );
}

export default RedisForm;

