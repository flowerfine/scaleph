import {history, useAccess, useIntl} from "@@/exports";
import {useRef, useState} from "react";
import {ActionType, ProColumns, ProFormInstance} from "@ant-design/pro-components";
import {FlinkClusterConfig, FlinkJobConfigJar} from "@/services/dev/typings";
import {Button, message, Modal, Select, Space, Tooltip} from "antd";
import {PRIVILEGE_CODE} from "@/constant";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import {deleteOne} from "@/services/dev/flinkClusterConfig.service";

const JobConfigJarWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<FlinkJobConfigJar[]>([]);

  const tableColumns: ProColumns<FlinkJobConfigJar>[] = [
    {
      title: intl.formatMessage({id: 'pages.dev.jobConfigJar.name'}),
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.artifact'}),
      dataIndex: 'flinkArtifact',
      render: (text, record, index) => {
        return record.flinkArtifact?.name;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig'}),
      dataIndex: 'flinkClusterConfig',
      render: (text, record, index) => {
        return record.flinkClusterConfig?.name;
      }
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterInstance'}),
      dataIndex: 'flinkClusterInstance',
      render: (text, record, index) => {
        return record.flinkClusterInstance?.name;
      }
    },
    {
      title: intl.formatMessage({id: 'pages.dev.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.dev.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.dev.updateTime'}),
      dataIndex: 'updateTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'app.common.operate.label'}),
      dataIndex: 'actions',
      align: 'center',
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() => {
                    history.push("/workspace/dev/clusterConfigOptions", record);
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevDatasourceDelete) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DeleteOutlined/>}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                      content: intl.formatMessage({
                        id: 'app.common.operate.delete.confirm.content',
                      }),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        deleteOne(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({id: 'app.common.operate.delete.success'}),
                            );
                            actionRef.current?.reload();
                          }
                        });
                      },
                    });
                  }}
                ></Button>
              </Tooltip>
            )}
          </Space>
        </>
      ),
    },
  ];
  return (<div>Job Config for Jar Artifact</div>);
}

export default JobConfigJarWeb;
