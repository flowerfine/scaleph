import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE, WORKSPACE_CONF } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { FlinkArtifactService } from '@/services/project/flinkArtifact.service';
import { FlinkArtifactJarService } from '@/services/project/flinkArtifactJar.service';
import { FlinkCLusterInstanceService } from '@/services/project/flinkClusterInstance.service';
import { JobService } from '@/services/project/job.service';
import { ResourceJarService } from '@/services/resource/jar.service';
import { ProFormInstance, StepsForm } from '@ant-design/pro-components';
import { AutoComplete, Form, Input, Modal, Radio, Select } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { useAccess, useIntl } from 'umi';

const JobCreateForm: React.FC<ModalFormProps<any>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const access = useAccess();
  const step1FormRef = useRef<ProFormInstance>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  const [jobTypeList, setJobTypeList] = useState<Dict[]>([]);
  const [artifactList, setAtrifactList] = useState<Dict[]>([]);
  const [jobOptions, setJobOptions] = useState<Dict[]>([]);
  const [resources, setResources] = useState<Dict[]>([]);
  const [clusters, setClusters] = useState<Dict[]>([]);
  const [jobType, setJobType] = useState<string>('0');

  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.flinkJobType).then((d) => {
      setJobTypeList(d);
    });
    init(0, jobType);
  }, []);

  const refreshClusterInstance = (name?: string) => {
    FlinkCLusterInstanceService.list({ name: name, pageSize: 10, current: 1 }).then((d) => {
      const list: Dict[] = [];
      d.data?.map((item) => {
        list.push({ value: item.id, label: item.name });
      });
      setClusters(list);
    });
  };

  const refreshResources = (name?: string) => {
    ResourceJarService.list({ fileName: name, pageSize: 10, current: 1 }).then((d) => {
      const list: Dict[] = [];
      d.data?.map((item) => {
        list.push({ value: item.id, label: item.fileName });
      });
      setResources(list.concat(resources));
    });
  };

  const init = (step: number, jobType?: string, name?: string) => {
    if (step == 0) {
      if (jobType == '0') {
        FlinkArtifactService.list({ pageSize: 10, current: 1, name: name }).then((d) => {
          const list: Dict[] = [];
          d.data?.map((item) => {
            list.push({ value: item.id, label: item.name });
          });
          setAtrifactList(list);
        });
      } else if (jobType == '1') {
        //todo sql job list
      } else if (jobType == '2') {
        JobService.listJobByProject({
          pageSize: 10,
          current: 1,
          projectId: projectId + '',
          jobName: name,
        }).then((d) => {
          const list: Dict[] = [];
          d.data?.map((item) => {
            list.push({ value: item.id, label: item.jobName });
          });
          setJobOptions(list);
        });
      }
    } else if (step == 1) {
      refreshClusterInstance();
      refreshResources();
    } else if (step == 2) {
    }
  };
  return (
    <StepsForm
      onFinish={async (values) => {
        console.log(values);
      }}
      stepsProps={{ size: 'small', labelPlacement: 'vertical' }}
      stepsFormRender={(dom, submitter) => {
        return (
          <Modal
            title="创建作业"
            width={780}
            bodyStyle={{ overflowY: 'scroll', maxHeight: '640px' }}
            onCancel={onCancel}
            open={visible}
            destroyOnClose
            footer={submitter}
          >
            {dom}
          </Modal>
        );
      }}
      onCurrentChange={(current: number) => {
        init(current);
      }}
    >
      <StepsForm.StepForm
        name="step1"
        title="选择作业"
        layout="horizontal"
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 19 }}
        initialValues={{ type: jobType }}
        formRef={step1FormRef}
      >
        <Form.Item name="type" label="作业类型">
          <Radio.Group
            onChange={(item) => {
              const value = item.target.value;
              setJobType(value);
              setJobOptions([]);
              step1FormRef.current?.setFieldsValue({ flinkArtifactId: '', artifact: '' });
              init(0, value);
            }}
          >
            {jobTypeList.map((value) => {
              return (
                <Radio value={value.value} key={value.value}>
                  {value.label}
                </Radio>
              );
            })}
          </Radio.Group>
        </Form.Item>
        {jobType == '0' && (
          <>
            <Form.Item name="artifact" label="Artifact" rules={[{ required: true }]}>
              <Select
                showSearch
                allowClear
                options={artifactList}
                notFoundContent={null}
                defaultActiveFirstOption={false}
                filterOption={false}
                onSearch={(value: string) => {
                  init(0, '0', value);
                }}
                onChange={() => {
                  step1FormRef.current?.setFieldValue('flinkArtifactId', '');
                }}
                onSelect={(value: string, option: Dict) => {
                  FlinkArtifactJarService.listByArtifact(option.value as string).then((d) => {
                    const list: Dict[] = [];
                    d.map((item) => {
                      list.push({ value: item.id, label: item.fileName + '\t-\t' + item.version });
                    });
                    setJobOptions(list);
                  });
                }}
              ></Select>
            </Form.Item>
            <Form.Item name="flinkArtifactId" label="Artifact Jar" rules={[{ required: true }]}>
              <Select
                showSearch
                allowClear
                options={jobOptions}
                defaultActiveFirstOption={false}
                filterOption={false}
                disabled={!step1FormRef.current?.getFieldValue('artifact')}
              ></Select>
            </Form.Item>
          </>
        )}
        {jobType == '1' && (
          <Form.Item name="flinkArtifactId" label="作业" rules={[{ required: true }]}>
            <Select
              showSearch
              allowClear
              options={jobOptions}
              notFoundContent={null}
              defaultActiveFirstOption={false}
              filterOption={false}
              onSearch={(value: string) => {
                init(0, jobType, value);
              }}
            ></Select>
          </Form.Item>
        )}
        {jobType == '2' && (
          <Form.Item name="flinkArtifactId" label="作业" rules={[{ required: true }]}>
            <Select
              showSearch
              allowClear
              options={jobOptions}
              notFoundContent={null}
              defaultActiveFirstOption={false}
              filterOption={false}
              onSearch={(value: string) => {
                init(0, jobType, value);
              }}
            ></Select>
          </Form.Item>
        )}
      </StepsForm.StepForm>
      <StepsForm.StepForm
        name="step2"
        title="集群&资源"
        layout="horizontal"
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 19 }}
      >
        <Form.Item name="jars" label="资源">
          <Select
            showSearch
            allowClear
            options={resources}
            notFoundContent={null}
            defaultActiveFirstOption={false}
            filterOption={false}
            onSearch={(value: string) => {
              refreshResources(value);
            }}
          ></Select>
        </Form.Item>
        <Form.Item name="clusterInstanceId" label="集群">
          <Select
            showSearch
            allowClear
            mode="multiple"
            options={clusters}
            notFoundContent={null}
            defaultActiveFirstOption={false}
            filterOption={false}
            onSearch={(value: string) => {
              refreshClusterInstance(value);
            }}
          ></Select>
        </Form.Item>
      </StepsForm.StepForm>
      <StepsForm.StepForm
        name="step3"
        title="参数设置"
        layout="horizontal"
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 19 }}
      >
        <Form.Item name="jobType" label="任务类型">
          <Input></Input>
        </Form.Item>
      </StepsForm.StepForm>
    </StepsForm>
  );
};
export default JobCreateForm;
