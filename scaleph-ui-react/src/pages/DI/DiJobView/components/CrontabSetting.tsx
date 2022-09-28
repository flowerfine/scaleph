import { ModalFormProps } from '@/app.d';
import { JobService } from '@/services/project/job.service';
import { DiJob } from '@/services/project/typings';
import { Alert, Form, Input, message, Modal, Tabs, Typography } from 'antd';
import { useIntl } from 'umi';
import React, { useEffect, useState } from 'react';
import moment from 'moment';
type CrontabDataType = {
  type: number;
  value: string;
  min: number;
  max: number;
  intervalFrom?: number;
  intervalTo?: number;
  loopFrom?: number;
  loopTo?: number;
  custom: number[];
};

const CrontabSetting: React.FC<ModalFormProps<DiJob>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [crontabStr, setCrontabStr] = useState<string>('');
  const [minutes, setMinutes] = useState<number[]>([]);
  const [hours, setHours] = useState<number[]>([]);
  const [days, setDays] = useState<number[]>([]);
  const [months, setMonths] = useState<number[]>([]);
  const [weeks, setWeeks] = useState<number[]>([]);
  const [fireTimeList, setFireTimeList] = useState<Date[]>([]);
  const [activeTabId, setActiveTabId] = useState<string>('');
  const [cronMinute, setCronMinute] = useState<CrontabDataType>({
    type: 1,
    value: '*',
    min: 0,
    max: 59,
    intervalFrom: 0,
    intervalTo: 0,
    loopFrom: 0,
    loopTo: 0,
    custom: [],
  });
  const [cronHour, setCronHour] = useState<CrontabDataType>({
    type: 1,
    value: '*',
    min: 0,
    max: 23,
    intervalFrom: 0,
    intervalTo: 0,
    loopFrom: 0,
    loopTo: 0,
    custom: [],
  });
  const [cronDay, setCronDay] = useState<CrontabDataType>({
    type: 1,
    value: '*',
    min: 1,
    max: 31,
    intervalFrom: 1,
    intervalTo: 1,
    loopFrom: 1,
    loopTo: 1,
    custom: [],
  });
  const [cronMonth, setCronMonth] = useState<CrontabDataType>({
    type: 1,
    value: '*',
    min: 1,
    max: 12,
    intervalFrom: 1,
    intervalTo: 1,
    loopFrom: 1,
    loopTo: 1,
    custom: [],
  });
  const [cronWeek, setCronWeek] = useState<CrontabDataType>({
    type: 0,
    value: '?',
    min: 1,
    max: 7,
    intervalFrom: 1,
    intervalTo: 1,
    custom: [],
  });

  const tabItems = [
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.custom' }),
      key: 'custom',
      children: <>asfads</>,
    },
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.minute' }),
      key: 'minute',
      children: 'minute',
    },
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.hour' }),
      key: 'hour',
      children: 'hour',
    },
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.day' }),
      key: 'day',
      children: 'day',
    },
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.month' }),
      key: 'month',
      children: 'month',
    },
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.week' }),
      key: 'week',
      children: 'week',
    },
  ];

  useEffect(() => {
    console.log(data);
    setMinutes(buildNums(60));
    setHours(buildNums(24));
    setDays(buildNums(31));
    setMonths(buildNums(12));
    setWeeks(buildNums(7));
    buildCrontab();
    setCrontabStr(data?.jobCrontab as string);
  }, []);

  const buildNums = (maxNum: number) => {
    let nums: number[] = [];
    for (let i = 0; i < maxNum; i++) {
      nums.push(i);
    }
    return nums;
  };
  const buildCrontab = () => {
    setCrontabStr('0 * * * * ?');
    JobService.listNext5FireTime('0 * * * * ?').then((d) => {
      setFireTimeList(d);
    });
  };

  return (
    <Modal
      visible={visible}
      title={intl.formatMessage({ id: 'pages.project.di.setting' })}
      width={580}
      bodyStyle={{ padding: '6px 24px 24px 24px' }}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let d: DiJob = {
            id: values.id,
          };
          data.id
            ? JobService.updateJob({ ...d }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  onVisibleChange(false);
                }
              })
            : JobService.addJob({ ...d }).then((d) => {
                if (d.success) {
                  // message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                  onVisibleChange(false);
                }
              });
        });
      }}
    >
      <Tabs items={tabItems} centered={true}></Tabs>
      <Alert
        message={
          <>
            <Typography.Text>
              {intl.formatMessage({ id: 'pages.project.di.job.crontab.title' }) + ' : '}
            </Typography.Text>
            <Typography.Text strong>{crontabStr}</Typography.Text>
          </>
        }
        type="success"
        style={{
          backgroundColor: '#ffffff',
          borderColor: '#bfbfbf',
          textAlign: 'center',
          margin: '12px 0px 12px 0px ',
        }}
      />
      <Alert
        message={
          <>
            <Typography.Text>
              {intl.formatMessage({ id: 'pages.project.di.job.crontab.next5' })}
            </Typography.Text>
            {fireTimeList.map((item, index) => {
              return (
                <>
                  <br />
                  <Typography.Text key={index}>
                    {moment(item).format('YYYY-MM-DD HH:mm:ss')}
                  </Typography.Text>
                </>
              );
            })}
          </>
        }
        type="success"
        style={{
          backgroundColor: '#ffffff',
          borderColor: '#bfbfbf',
          textAlign: 'center',
        }}
      />
    </Modal>
  );
};

export default CrontabSetting;
