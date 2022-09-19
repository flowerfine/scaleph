import {ProCard, ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {Col, Form, Select} from "antd";
import {
  FlinkArtifact,
  FlinkArtifactJar,
  FlinkArtifactJarListParam,
  FlinkArtifactListParam
} from "@/services/dev/typings";
import {list as listArtifactJar} from "@/services/dev/flinkArtifactJar.service";
import {useEffect, useState} from "react";
import {list as listArtifact} from "@/services/dev/flinkArtifact.service";

const JobJar: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance();
  const [flinkArtifactLoading, setFlinkArtifactLoading] = useState(false);
  const [flinkArtifactData, setFlinkArtifactData] = useState<FlinkArtifact[]>([]);
  const [flinkArtifactPage, setFlinkArtifactPage] = useState({
    pageSize: 10,
    current: 1,
    total: 11 // greater than first page
  });
  const [flinkArtifactJarLoading, setFlinkArtifactJarLoading] = useState(false);
  const [flinkArtifactJarData, setFlinkArtifactJarData] = useState<FlinkArtifactJar[]>([]);
  const [flinkArtifactJarPage, setFlinkArtifactJarPage] = useState({
    flinkArtifactId: 0,
    pageSize: 10,
    current: 1,
    total: 11 // greater than first page
  });

  useEffect(() => {
    loadFlinkArtifactData()
    if (!form.getFieldValue("flinkArtifactId")) {
      return;
    }
    loadFlinkArtifactJarData(form.getFieldValue("flinkArtifactId"))
  }, []);

  const loadFlinkArtifactData = (keyword?: string) => {
    setFlinkArtifactLoading(true);
    const param: FlinkArtifactListParam = {
      name: keyword,
      type: '0',
      pageSize: flinkArtifactPage.pageSize,
      current: flinkArtifactPage.current
    }
    listArtifact(param).then((response) => {
      setFlinkArtifactPage({
        pageSize: response.pageSize,
        current: response.current + 1,
        total: response.total
      })
      if (response.data) {
        setFlinkArtifactData([...flinkArtifactData, ...response.data])
      }
    }).finally(() => setFlinkArtifactLoading(false))
  }

  const handleFlinkArtifactScroll = (e: any) => {
    e.persist()
    const {scrollTop, scrollHeight, clientHeight} = e.target;
    if (scrollHeight - scrollTop >= clientHeight
      && ((flinkArtifactPage.current - 1) * flinkArtifactPage.pageSize) < flinkArtifactPage.total) {
      loadFlinkArtifactData()
    }
  }

  const handleArtifactChange = (value: any, option: any) => {
    clearFlinkArtifactJar(value).then(() => {
      loadFlinkArtifactJarData(value)
    })
  };

  const clearFlinkArtifactJar = (value: number) => {
    setFlinkArtifactJarPage({
      flinkArtifactId: value,
      pageSize: 10,
      current: 1,
      total: 11 // greater than first page
    })
    setFlinkArtifactJarData([])
    return Promise.resolve()
  }

  const loadFlinkArtifactJarData = (flinkArtifactId: number) => {
    setFlinkArtifactJarLoading(true);
    console.log('flinkArtifactJarPage', flinkArtifactJarPage)
    const param: FlinkArtifactJarListParam = {
      flinkArtifactId: flinkArtifactId,
      pageSize: flinkArtifactJarPage.pageSize,
      current: flinkArtifactJarPage.current
    }
    listArtifactJar(param).then((response) => {
      setFlinkArtifactJarPage({
        flinkArtifactId: flinkArtifactId,
        pageSize: response.pageSize,
        current: response.current + 1,
        total: response.total
      })
      setFlinkArtifactJarData([...flinkArtifactJarData, ...response.data])
    }).finally(() => setFlinkArtifactJarLoading(false))
  }

  const handleArtifactJarChange = (value: any, option: any) => {
    if (option) {
      form.setFieldValue('entryClass', option.item.entryClass)
    }
  };

  const handleFlinkArtifactJarScroll = (e: any) => {
    e.persist()
    const {scrollTop, scrollHeight, clientHeight} = e.target;
    if (scrollHeight - scrollTop >= clientHeight
      && ((flinkArtifactJarPage.current - 1) * flinkArtifactJarPage.pageSize) < flinkArtifactJarPage.total) {
      loadFlinkArtifactJarData(flinkArtifactJarPage.flinkArtifactId)
    }
  }

  return (<ProCard>
    <ProFormGroup>
      <Col span={8}>
        <Form.Item
          name={"flinkArtifactId"}
          label={intl.formatMessage({id: 'pages.dev.artifact'})}
          rules={[{required: true}]}>
          <Select
            showSearch={false}
            loading={flinkArtifactLoading}
            onPopupScroll={handleFlinkArtifactScroll}
            onChange={handleArtifactChange}>
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
        <Form.Item
          name={"flinkArtifactJarId"}
          rules={[{required: true}]}>
          <Select
            showSearch={false}
            loading={flinkArtifactJarLoading}
            onChange={handleArtifactJarChange}
            onPopupScroll={handleFlinkArtifactJarScroll}>
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
        creatorButtonText: '添加 main args',
        type: "text"
      }}>
      <ProFormGroup>
        <ProFormText name="parameter" label={'Parameter'} colProps={{span: 10, offset: 1}}/>
        <ProFormText name="value" label={'Value'} colProps={{span: 10, offset: 1}}/>
      </ProFormGroup>
    </ProFormList>
  </ProCard>);
}

export default JobJar;
