import { Dict } from '@/app.d';
import { DICT_TYPE, PRIVILEGE_CODE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { FlinkJobService } from '@/services/project/FlinkJobService';
import { FlinkJob, FlinkJobListParam } from '@/services/project/typings';
import {
  Button,
  Card,
  Col,
  Divider,
  Empty,
  Form,
  Input,
  PageHeader,
  Pagination,
  Row,
  Select,
  Space,
  Tag,
  Typography,
} from 'antd';
import { useLayoutEffect, useState } from 'react';
import { useIntl, useAccess, history } from 'umi';
import JobCreateForm from './components/JobCreateForm';

const JobListView: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const [jobList, setJobList] = useState<FlinkJob[]>([]);
  const [queryParams, setQueryParams] = useState<FlinkJobListParam>({});
  const [total, setTotal] = useState<number>();
  const [jobTypeList, setJobTypeList] = useState<Dict[]>([]);
  const [jobCreateFormData, setJobCreateFormData] = useState<{ visible: boolean; data: any }>({
    visible: false,
    data: {},
  });
  useLayoutEffect(() => {
    refreshJobList({ pageSize: 5 });
  }, []);

  const refreshJobList = (params: FlinkJobListParam) => {
    FlinkJobService.list(params).then((resp) => {
      setJobList(resp.data);
      setTotal(resp.total);
    });
    DictDataService.listDictDataByType(DICT_TYPE.flinkJobType).then((d) => {
      setJobTypeList(d);
    });
  };

  return (
    <>
      <div style={{ backgroundColor: '#ffffff' }}>
        <PageHeader
          title={intl.formatMessage({ id: 'pages.project.job.list' })}
          extra={
            <>
              {access.canAccess(PRIVILEGE_CODE.datadevProjectAdd) && (
                <Button
                  key="new"
                  type="primary"
                  onClick={() => {
                    setJobCreateFormData({ data: {}, visible: true });
                  }}
                >
                  {intl.formatMessage({ id: 'pages.project.job.create' })}
                </Button>
              )}
            </>
          }
        >
          <Row gutter={[12, 12]}>
            <Col span={12}>
              <Form.Item label={intl.formatMessage({ id: 'pages.project.job.name' })}>
                <Input
                  onChange={(item) => {
                    setQueryParams({ ...queryParams, name: item.target.value });
                    refreshJobList({ ...queryParams, name: item.target.value });
                  }}
                ></Input>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label={intl.formatMessage({ id: 'pages.project.job.type' })}>
                <Select
                  style={{ width: '100%' }}
                  showSearch={true}
                  allowClear={true}
                  optionFilterProp="label"
                  filterOption={(input, option) =>
                    (option!.children as unknown as string)
                      .toLowerCase()
                      .includes(input.toLowerCase())
                  }
                  onChange={(value) => {
                    setQueryParams({ ...queryParams, type: value });
                    refreshJobList({ ...queryParams, type: value });
                  }}
                >
                  {jobTypeList.map((item) => {
                    return (
                      <Select.Option key={item.value} value={item.value}>
                        {item.label}
                      </Select.Option>
                    );
                  })}
                </Select>
              </Form.Item>
            </Col>
          </Row>
        </PageHeader>
      </div>
      {jobList && jobList.length > 0 ? (
        <>
          {jobList.map((item, i) => {
            return (
              <Card
                key={i}
                hoverable={true}
                bordered={false}
                style={{ marginTop: 12 }}
                onClick={() => {
                  history.push('/workspace/job/detail', { item });
                }}
              >
                <Typography.Title level={5}>
                  <Space>
                    {item.name}
                    <Tag color="#108ee9">{item.type}</Tag>
                  </Space>
                </Typography.Title>
                <Space split={<Divider type="vertical" />}>
                  <Typography.Text type="secondary">
                    {intl.formatMessage({ id: 'pages.project.job.createTime' }) +
                      ':' +
                      item.createTime}
                  </Typography.Text>
                  {item.creator && (
                    <Typography.Text type="secondary">
                      {intl.formatMessage({ id: 'pages.project.job.creator' }) + ':' + item.creator}
                    </Typography.Text>
                  )}
                  {item.remark && (
                    <Typography.Text type="secondary">
                      {intl.formatMessage({ id: 'pages.project.job.remark' }) + ':' + item.remark}
                    </Typography.Text>
                  )}
                </Space>
              </Card>
            );
          })}
          <Card bordered={false} style={{ marginTop: 12, textAlign: 'right' }}>
            <Pagination
              size="small"
              total={total}
              showSizeChanger
              showQuickJumper
              pageSizeOptions={[5, 10, 20, 50, 100]}
              onChange={(page, pageSize) => {
                setQueryParams({ ...queryParams, pageSize: pageSize, current: page });
                refreshJobList({ ...queryParams, pageSize: pageSize, current: page });
              }}
            />
          </Card>
        </>
      ) : (
        <Card bordered={false} style={{ marginTop: 12 }}>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
        </Card>
      )}
      {jobCreateFormData.visible && (
        <JobCreateForm
          data={jobCreateFormData.data}
          onCancel={() => {
            setJobCreateFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setJobCreateFormData({ visible: visible, data: {} });
            refreshJobList({ ...queryParams });
          }}
          visible={jobCreateFormData.visible}
        ></JobCreateForm>
      )}
    </>
  );
};
export default JobListView;
