import {FlinkArtifactService} from '@/services/dev/flinkArtifact.service';
import {FlinkArtifactJarService} from '@/services/dev/flinkArtifactJar.service';
import {
  FlinkArtifact,
  FlinkArtifactJar,
  FlinkArtifactJarListParam,
  FlinkArtifactListParam,
} from '@/services/dev/typings';
import {ProCard, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {Col, Form, Select} from 'antd';
import {useEffect, useState} from 'react';
import {useIntl} from 'umi';
import {FlinkJobForJar} from "@/pages/DEV/Job/typings";

const JobJar: React.FC<{ data: FlinkJobForJar }> = ({data}) => {
  const intl = useIntl();
  const form = Form.useFormInstance();
  const [flinkArtifactLoading, setFlinkArtifactLoading] = useState(false);
  const [flinkArtifactData, setFlinkArtifactData] = useState<FlinkArtifact[]>([]);
  const [flinkArtifactPage, setFlinkArtifactPage] = useState<{
    keyword?: string;
    pageSize: number;
    current: number;
    total: number;
  }>({
    pageSize: 10,
    current: 1,
    total: 11, // greater than first page
  });
  const [flinkArtifactJarLoading, setFlinkArtifactJarLoading] = useState(false);
  const [flinkArtifactJarData, setFlinkArtifactJarData] = useState<FlinkArtifactJar[]>([]);
  const [flinkArtifactJarPage, setFlinkArtifactJarPage] = useState({
    flinkArtifactId: 0,
    pageSize: 10,
    current: 1,
    total: 11, // greater than first page
  });

  useEffect(() => {
    // first page load
    loadFlinkArtifactData([], 1, 10);
    if (!data?.flinkArtifactJar?.flinkArtifact?.id) {
      return;
    }
    loadFlinkArtifactJarData([], 1, 10, data.flinkArtifactJar?.flinkArtifact?.id);
  }, []);

  const loadFlinkArtifactData = (
    prevs: Array<FlinkArtifact>,
    current: number,
    pageSize: number,
    keyword?: string,
  ) => {
    setFlinkArtifactLoading(true);
    const param: FlinkArtifactListParam = {
      name: keyword,
      type: '0',
      pageSize: pageSize,
      current: current,
    };
    FlinkArtifactService.list(param)
      .then((response) => {
        setFlinkArtifactPage({
          keyword: keyword,
          pageSize: response.pageSize,
          current: response.current + 1,
          total: response.total,
        });
        if (response.data) {
          setFlinkArtifactData([...prevs, ...response.data]);
        }
      })
      .finally(() => setFlinkArtifactLoading(false));
  };

  const handleFlinkArtifactScroll = (e: any) => {
    e.persist();
    const {scrollTop, scrollHeight, clientHeight} = e.target;
    if (
      scrollHeight - scrollTop >= clientHeight &&
      (flinkArtifactPage.current - 1) * flinkArtifactPage.pageSize < flinkArtifactPage.total
    ) {
      loadFlinkArtifactData(
        flinkArtifactData,
        flinkArtifactPage.current,
        flinkArtifactPage.pageSize,
        flinkArtifactPage.keyword,
      );
    }
  };

  const handleArtifactChange = (value: any, option: any) => {
    setFlinkArtifactJarPage({
      flinkArtifactId: value,
      pageSize: 10,
      current: 1,
      total: 11, // greater than first page
    });
    setFlinkArtifactJarData([]);
    loadFlinkArtifactJarData([], 1, 10, value);
    form.resetFields(['flinkArtifactJarId', 'entryClass']);
  };

  const loadFlinkArtifactJarData = (
    prevs: Array<FlinkArtifactJar>,
    current: number,
    pageSize: number,
    flinkArtifactId: number,
  ) => {
    setFlinkArtifactJarLoading(true);
    const param: FlinkArtifactJarListParam = {
      flinkArtifactId: flinkArtifactId,
      pageSize: pageSize,
      current: current,
    };
    FlinkArtifactJarService.list(param)
      .then((response) => {
        setFlinkArtifactJarPage({
          flinkArtifactId: flinkArtifactId,
          pageSize: response.pageSize,
          current: response.current + 1,
          total: response.total,
        });
        setFlinkArtifactJarData([...prevs, ...response.data]);
      })
      .finally(() => setFlinkArtifactJarLoading(false));
  };

  const handleArtifactJarChange = (value: any, option: any) => {
    if (option) {
      form.setFieldValue('entryClass', option.item.entryClass);
    }
  };

  const handleFlinkArtifactJarScroll = (e: any) => {
    e.persist();
    const {scrollTop, scrollHeight, clientHeight} = e.target;
    if (
      scrollHeight - scrollTop >= clientHeight &&
      (flinkArtifactJarPage.current - 1) * flinkArtifactJarPage.pageSize <
      flinkArtifactJarPage.total
    ) {
      loadFlinkArtifactJarData(
        flinkArtifactJarData,
        flinkArtifactJarPage.current,
        flinkArtifactJarPage.pageSize,
        flinkArtifactJarPage.flinkArtifactId,
      );
    }
  };

  return (
    <ProCard>
      <ProFormGroup>
        <Col span={8}>
          <Form.Item
            name={'flinkArtifactId'}
            label={intl.formatMessage({id: 'pages.dev.artifact'})}
            rules={[{required: true}]}
          >
            <Select
              showSearch={true}
              filterOption={false}
              loading={flinkArtifactLoading}
              onSearch={(keyword) => {
                loadFlinkArtifactData([], 1, 10, keyword);
              }}
              onPopupScroll={handleFlinkArtifactScroll}
              onChange={handleArtifactChange}
            >
              {flinkArtifactData.map((item) => {
                return (
                  <Select.Option key={item.id} value={item.id} item={item}>
                    {item.name}
                  </Select.Option>
                );
              })}
            </Select>
          </Form.Item>
        </Col>
        <Col span={16}>
          <Form.Item name={'flinkArtifactJarId'} rules={[{required: true}]}>
            <Select
              showSearch={false}
              loading={flinkArtifactJarLoading}
              onChange={handleArtifactJarChange}
              onPopupScroll={handleFlinkArtifactJarScroll}
            >
              {flinkArtifactJarData.map((item) => {
                return (
                  <Select.Option key={item.id} value={item.id} item={item}>
                    {item.version + '-' + item.fileName}
                  </Select.Option>
                );
              })}
            </Select>
          </Form.Item>
        </Col>
      </ProFormGroup>
      <ProFormText
        name="entryClass"
        label={intl.formatMessage({id: 'pages.dev.artifact.jar.entryClass'})}
        rules={[{required: true}, {max: 128}]}
        readonly
      />
      <ProFormList
        name="args"
        copyIconProps={false}
        creatorButtonProps={{
          creatorButtonText: intl.formatMessage({id: 'pages.dev.job.jar.args'}),
          type: 'text',
        }}
      >
        <ProFormGroup>
          <ProFormText
            name="parameter"
            label={intl.formatMessage({id: 'pages.dev.job.jar.args.key'})}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormText
            name="value"
            label={intl.formatMessage({id: 'pages.dev.job.jar.args.value'})}
            colProps={{span: 10, offset: 1}}
          />
        </ProFormGroup>
      </ProFormList>
    </ProCard>
  );
};

export default JobJar;
