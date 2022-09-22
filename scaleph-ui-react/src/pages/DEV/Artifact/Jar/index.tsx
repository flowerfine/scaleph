import {useEffect, useRef, useState} from "react";
import {Button, Select, Space, Tooltip} from "antd";
import {useAccess, useIntl, useLocation} from "umi";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {FlinkArtifactJar} from "@/services/dev/typings";
import {DICT_TYPE, PRIVILEGE_CODE} from "@/constant";
import {DownloadOutlined} from "@ant-design/icons";
import {download, list} from "@/services/dev/flinkArtifactJar.service";
import {history} from "@@/core/history";
import FlinkArtifactJarForm from "@/pages/DEV/Artifact/Jar/components/FlinkArtifactJarForm";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {Dict} from "@/app.d";

const FlinkArtifactJarWeb: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [flinkArtifactId, setFlinkArtifactId] = useState<number>(0);
  const [flinkVersionList, setFlinkVersionList] = useState<Dict[]>([]);
  const [flinkArtifactJarData, setFlinkArtifactJarData] = useState<{
    visiable: boolean;
    data: FlinkArtifactJar;
  }>({visiable: false, data: {}});

  useEffect(() => {
    listDictDataByType(DICT_TYPE.flinkVersion).then((d) => {
      setFlinkVersionList(d);
    });
  }, []);

  useEffect(() => {
    const params = urlParams.state as { id: number };
    if (params) {
      setFlinkArtifactId(params?.id);
    }
  }, []);

  const tableColumns: ProColumns<FlinkArtifactJar>[] = [
    {
      title: intl.formatMessage({id: 'pages.dev.artifact.jar.fileName'}),
      dataIndex: 'fileName',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.dev.artifact.jar.version'}),
      dataIndex: 'version',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.dev.artifact.jar.entryClass'}),
      dataIndex: 'entryClass',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.resource.flinkRelease.version'}),
      dataIndex: 'flinkVersion',
      render: (text, record, index) => {
        return record.flinkVersion?.label;
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <Select
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {flinkVersionList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        );
      },
    },
    {
      title: intl.formatMessage({id: 'pages.dev.artifact.jar.path'}),
      dataIndex: 'path',
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
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.download.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DownloadOutlined/>}
                  onClick={() => {
                    download(record);
                  }}/>
              </Tooltip>
            )}
          </Space>
        </>
      ),
    },
  ];
  return (<div>
    <ProTable<FlinkArtifactJar>
      headerTitle={
        <Button key="return" type="default" onClick={() => history.back()}>
          {intl.formatMessage({id: 'app.common.operate.return.label'})}
        </Button>
      }
      rowKey="name"
      actionRef={actionRef}
      formRef={formRef}
      options={false}
      columns={tableColumns}
      request={(params, sorter, filter) => {
        return list({...params, flinkArtifactId: flinkArtifactId});
      }}
      toolbar={{
        actions: [
          access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
            <Button
              key="new"
              type="primary"
              onClick={() => {
                setFlinkArtifactJarData({visiable: true, data: {}});
              }}
            >
              {intl.formatMessage({id: 'app.common.operate.upload.label'})}
            </Button>
          )
        ]
      }}
      pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    ></ProTable>
    {flinkArtifactJarData.visiable && (
      <FlinkArtifactJarForm
        visible={flinkArtifactJarData.visiable}
        onCancel={() => {
          setFlinkArtifactJarData({visiable: false, data: {}});
        }}
        onVisibleChange={(visiable) => {
          setFlinkArtifactJarData({visiable: visiable, data: {}});
          actionRef.current?.reload();
        }}
        data={{id: flinkArtifactId}}
      />
    )}
  </div>);
}

export default FlinkArtifactJarWeb;
