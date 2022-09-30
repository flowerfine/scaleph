import { ModalFormProps } from '@/app.d';
import { JobService } from '@/services/project/job.service';
import { DiJob } from '@/services/project/typings';
import {
  Alert,
  Col,
  Input,
  InputNumber,
  message,
  Modal,
  Radio,
  Row,
  Select,
  Space,
  Tabs,
  Typography,
} from 'antd';
import { useIntl } from 'umi';
import React, { useEffect, useRef, useState } from 'react';
import moment from 'moment';
type CrontabDataType = {
  type?: number;
  value?: string;
  min?: number;
  max?: number;
  intervalFrom?: number;
  intervalTo?: number;
  loopFrom?: number;
  loopTo?: number;
  custom?: number[];
};

const CrontabSetting: React.FC<ModalFormProps<DiJob>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [crontabStr, setCrontabStr] = useState<string>('');
  const [minutes, setMinutes] = useState<number[]>([]);
  const [hours, setHours] = useState<number[]>([]);
  const [days, setDays] = useState<number[]>([]);
  const [months, setMonths] = useState<number[]>([]);
  const [weeks, setWeeks] = useState<number[]>([]);
  const [fireTimeList, setFireTimeList] = useState<Date[]>([]);
  const [activeTabId, setActiveTabId] = useState<string>('custom');
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
  const cronMinuteRef = useRef(cronMinute);
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
  const cronHourRef = useRef(cronHour);
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
  const cronDayRef = useRef(cronDay);
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
  const cronMonthRef = useRef(cronMonth);
  const [cronWeek, setCronWeek] = useState<CrontabDataType>({
    type: 0,
    value: '?',
    min: 1,
    max: 7,
    intervalFrom: 1,
    intervalTo: 1,
    custom: [],
  });
  const cronWeekRef = useRef(cronWeek);
  const crontabStrRef = useRef(crontabStr);
  const tabItems = [
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.custom' }),
      key: 'custom',
      children: (
        <Space>
          {intl.formatMessage({ id: 'pages.project.di.job.crontab.custom' })}:
          <Input
            onChange={(e) => {
              setCrontabStr(e.target.value);
              crontabStrRef.current = e.target.value;
              buildCrontab();
            }}
            defaultValue={crontabStr}
          />
        </Space>
      ),
    },
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.minute' }),
      key: 'minute',
      children: (
        <div>
          <Radio.Group
            onChange={(e) => {
              setCronMinute({ ...cronMinute, type: e.target.value });
              cronMinuteRef.current = { ...cronMinute, type: e.target.value };
              buildCrontab();
            }}
          >
            <Space direction="vertical">
              <Radio value={1}>
                {intl.formatMessage({ id: 'pages.project.di.job.crontab.every' }) +
                  intl.formatMessage({ id: 'pages.project.di.job.crontab.minute' })}
              </Radio>
              <Radio value={2}>
                <Space>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range' })}:
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range.from' })}
                  <InputNumber
                    defaultValue={0}
                    min={cronMinute.min}
                    max={cronMinute.max}
                    disabled={cronMinute.type != 2}
                    onChange={(e) => {
                      setCronMinute({
                        ...cronMinute,
                        intervalFrom: e as number,
                      });
                      cronMinuteRef.current = {
                        ...cronMinute,
                        intervalFrom: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range.to' })}
                  <InputNumber
                    defaultValue={0}
                    min={cronMinute.min}
                    max={cronMinute.max}
                    disabled={cronMinute.type != 2}
                    onChange={(e) => {
                      setCronMinute({
                        ...cronMinute,
                        intervalTo: e as number,
                      });
                      cronMinuteRef.current = {
                        ...cronMinute,
                        intervalTo: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.minute' })}
                </Space>
              </Radio>
              <Radio value={3}>
                <Space>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop' })}:
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.from' })}
                  <InputNumber
                    defaultValue={0}
                    min={cronMinute.min}
                    max={cronMinute.max}
                    disabled={cronMinute.type != 3}
                    onChange={(e) => {
                      setCronMinute({
                        ...cronMinute,
                        loopFrom: e as number,
                      });
                      cronMinuteRef.current = {
                        ...cronMinute,
                        loopFrom: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.to' })}
                  <InputNumber
                    defaultValue={0}
                    min={cronMinute.min}
                    max={cronMinute.max}
                    disabled={cronMinute.type != 3}
                    onChange={(e) => {
                      setCronMinute({ ...cronMinute, loopTo: e as number });
                      cronMinuteRef.current = {
                        ...cronMinute,
                        loopTo: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.minute' }) +
                    intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.times' })}
                </Space>
              </Radio>
              <Row gutter={[0, 0]} align="middle">
                <Col>
                  <Radio value={4}>
                    {intl.formatMessage({ id: 'pages.project.di.job.crontab.designated' }) + ' :'}
                  </Radio>
                </Col>
                <Col>
                  <Select
                    style={{ width: '260px' }}
                    allowClear={true}
                    mode="multiple"
                    maxTagCount="responsive"
                    disabled={cronMinute.type != 4}
                    onChange={(e) => {
                      setCronMinute({ ...cronMinute, custom: e as number[] });
                      cronMinuteRef.current = { ...cronMinute, custom: e as number[] };
                      buildCrontab();
                    }}
                  >
                    {minutes.map((v, i) => {
                      return (
                        <Select.Option key={v} value={v}>
                          {v}
                        </Select.Option>
                      );
                    })}
                  </Select>
                </Col>
              </Row>
            </Space>
          </Radio.Group>
        </div>
      ),
    },
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.hour' }),
      key: 'hour',
      children: (
        <>
          <Radio.Group
            onChange={(e) => {
              setCronHour({ ...cronHour, type: e.target.value });
              cronHourRef.current = { ...cronHour, type: e.target.value };
              buildCrontab();
            }}
          >
            <Space direction="vertical">
              <Radio value={1}>
                {intl.formatMessage({ id: 'pages.project.di.job.crontab.every' }) +
                  intl.formatMessage({ id: 'pages.project.di.job.crontab.hour' })}
              </Radio>
              <Radio value={2}>
                <Space>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range' })}:
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range.from' })}
                  <InputNumber
                    defaultValue={0}
                    min={cronHour.min}
                    max={cronHour.max}
                    disabled={cronHour.type != 2}
                    onChange={(e) => {
                      setCronHour({
                        ...cronHour,
                        intervalFrom: e as number,
                      });
                      cronHourRef.current = {
                        ...cronHour,
                        intervalFrom: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range.to' })}
                  <InputNumber
                    defaultValue={0}
                    min={cronHour.min}
                    max={cronHour.max}
                    disabled={cronHour.type != 2}
                    onChange={(e) => {
                      setCronHour({
                        ...cronHour,
                        intervalTo: e as number,
                      });
                      cronHourRef.current = {
                        ...cronHour,
                        intervalTo: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.hour' })}
                </Space>
              </Radio>
              <Radio value={3}>
                <Space>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop' })}:
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.from' })}
                  <InputNumber
                    defaultValue={0}
                    min={cronHour.min}
                    max={cronHour.max}
                    disabled={cronHour.type != 3}
                    onChange={(e) => {
                      setCronHour({
                        ...cronHour,
                        loopFrom: e as number,
                      });
                      cronHourRef.current = {
                        ...cronHour,
                        loopFrom: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.to' })}
                  <InputNumber
                    defaultValue={0}
                    min={cronHour.min}
                    max={cronHour.max}
                    disabled={cronHour.type != 3}
                    onChange={(e) => {
                      setCronHour({
                        ...cronHour,
                        loopTo: e as number,
                      });
                      cronHourRef.current = {
                        ...cronHour,
                        loopTo: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.hour' }) +
                    intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.times' })}
                </Space>
              </Radio>
              <Row gutter={[0, 0]} align="middle">
                <Col>
                  <Radio value={4}>
                    {intl.formatMessage({ id: 'pages.project.di.job.crontab.designated' }) + ' :'}
                  </Radio>
                </Col>
                <Col>
                  <Select
                    style={{ width: '260px' }}
                    allowClear={true}
                    mode="multiple"
                    maxTagCount="responsive"
                    disabled={cronHour.type != 4}
                    onChange={(e) => {
                      setCronHour({ ...cronHour, custom: e as number[] });
                      cronHourRef.current = { ...cronHour, custom: e as number[] };
                      buildCrontab();
                    }}
                  >
                    {hours.map((v, i) => {
                      return (
                        <Select.Option key={v} value={v}>
                          {v}
                        </Select.Option>
                      );
                    })}
                  </Select>
                </Col>
              </Row>
            </Space>
          </Radio.Group>
        </>
      ),
    },
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.day' }),
      key: 'day',
      children: (
        <>
          <Radio.Group
            onChange={(e) => {
              setCronDay({ ...cronDay, type: e.target.value });
              cronDayRef.current = { ...cronDay, type: e.target.value };
              buildCrontab();
            }}
          >
            <Space direction="vertical">
              <Radio value={0}>
                {intl.formatMessage({ id: 'pages.project.di.job.crontab.undesignated.day' })}
              </Radio>
              <Radio value={1}>
                {intl.formatMessage({ id: 'pages.project.di.job.crontab.every' }) +
                  intl.formatMessage({ id: 'pages.project.di.job.crontab.day' })}
              </Radio>
              <Radio value={2}>
                {intl.formatMessage({ id: 'pages.project.di.job.crontab.lastday' })}
              </Radio>
              <Radio value={3}>
                <Space>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range' })}:
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range.from' })}
                  <InputNumber
                    defaultValue={cronDay.min}
                    min={cronDay.min}
                    max={cronDay.max}
                    disabled={cronDay.type != 3}
                    onChange={(e) => {
                      setCronDay({
                        ...cronDay,
                        intervalFrom: e as number,
                      });
                      cronDayRef.current = {
                        ...cronDay,
                        intervalFrom: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range.to' })}
                  <InputNumber
                    defaultValue={cronDay.min}
                    min={cronDay.min}
                    max={cronDay.max}
                    disabled={cronDay.type != 3}
                    onChange={(e) => {
                      setCronDay({
                        ...cronDay,
                        intervalTo: e as number,
                      });
                      cronDayRef.current = {
                        ...cronDay,
                        intervalTo: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.day' })}
                </Space>
              </Radio>
              <Radio value={4}>
                <Space>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop' })}:
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.from' })}
                  <InputNumber
                    defaultValue={cronDay.min}
                    min={cronDay.min}
                    max={cronDay.max}
                    disabled={cronDay.type != 4}
                    onChange={(e) => {
                      setCronDay({
                        ...cronDay,
                        loopFrom: e as number,
                      });
                      cronDayRef.current = {
                        ...cronDay,
                        loopFrom: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.to' })}
                  <InputNumber
                    defaultValue={cronDay.min}
                    min={cronDay.min}
                    max={cronDay.max}
                    disabled={cronDay.type != 4}
                    onChange={(e) => {
                      setCronDay({
                        ...cronDay,
                        loopTo: e as number,
                      });
                      cronDayRef.current = {
                        ...cronDay,
                        loopTo: e as number,
                      };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.day' }) +
                    intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.times' })}
                </Space>
              </Radio>
              <Row gutter={[0, 0]} align="middle">
                <Col>
                  <Radio value={5}>
                    {intl.formatMessage({ id: 'pages.project.di.job.crontab.designated' }) + ' :'}
                  </Radio>
                </Col>
                <Col>
                  <Select
                    style={{ width: '260px' }}
                    allowClear={true}
                    mode="multiple"
                    maxTagCount="responsive"
                    disabled={cronDay.type != 5}
                    onChange={(e) => {
                      setCronDay({ ...cronDay, custom: e as number[] });
                      cronDayRef.current = { ...cronDay, custom: e as number[] };
                      buildCrontab();
                    }}
                  >
                    {days.map((v, i) => {
                      return (
                        <Select.Option key={v} value={v}>
                          {v}
                        </Select.Option>
                      );
                    })}
                  </Select>
                </Col>
              </Row>
            </Space>
          </Radio.Group>
        </>
      ),
    },
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.month' }),
      key: 'month',
      children: (
        <>
          <Radio.Group
            onChange={(e) => {
              setCronMonth({ ...cronMonth, type: e.target.value });
              cronMonthRef.current = { ...cronMonth, type: e.target.value };
              buildCrontab();
            }}
          >
            <Space direction="vertical">
              <Radio value={1}>
                {intl.formatMessage({ id: 'pages.project.di.job.crontab.every' }) +
                  intl.formatMessage({ id: 'pages.project.di.job.crontab.month' })}
              </Radio>
              <Radio value={2}>
                <Space>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range' })}:
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range.from' })}
                  <InputNumber
                    defaultValue={cronMonth.min}
                    min={cronMonth.min}
                    max={cronMonth.max}
                    disabled={cronMonth.type != 2}
                    onChange={(e) => {
                      setCronMonth({ ...cronMonth, intervalFrom: e as number });
                      cronMonthRef.current = { ...cronMonth, intervalFrom: e as number };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range.to' })}
                  <InputNumber
                    defaultValue={cronMonth.min}
                    min={cronMonth.min}
                    max={cronMonth.max}
                    disabled={cronMonth.type != 2}
                    onChange={(e) => {
                      setCronMonth({ ...cronMonth, intervalTo: e as number });
                      cronMonthRef.current = { ...cronMonth, intervalTo: e as number };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.month' })}
                </Space>
              </Radio>
              <Radio value={3}>
                <Space>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop' })}:
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.from' })}
                  <InputNumber
                    defaultValue={cronMonth.min}
                    min={cronMonth.min}
                    max={cronMonth.max}
                    disabled={cronMonth.type != 3}
                    onChange={(e) => {
                      setCronMonth({ ...cronMonth, loopFrom: e as number });
                      cronMonthRef.current = { ...cronMonth, loopFrom: e as number };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.to' })}
                  <InputNumber
                    defaultValue={cronMonth.min}
                    min={cronMonth.min}
                    max={cronMonth.max}
                    disabled={cronMonth.type != 3}
                    onChange={(e) => {
                      setCronMonth({ ...cronMonth, loopTo: e as number });
                      cronMonthRef.current = { ...cronMonth, loopTo: e as number };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.month' }) +
                    intl.formatMessage({ id: 'pages.project.di.job.crontab.loop.times' })}
                </Space>
              </Radio>
              <Row gutter={[0, 0]} align="middle">
                <Col>
                  <Radio value={4}>
                    {intl.formatMessage({ id: 'pages.project.di.job.crontab.designated' }) + ' :'}
                  </Radio>
                </Col>
                <Col>
                  <Select
                    style={{ width: '260px' }}
                    allowClear={true}
                    mode="multiple"
                    maxTagCount="responsive"
                    disabled={cronMonth.type != 4}
                    onChange={(e) => {
                      setCronMonth({ ...cronMonth, custom: e as number[] });
                      cronMonthRef.current = { ...cronMonth, custom: e as number[] };
                      buildCrontab();
                    }}
                  >
                    {months.map((v, i) => {
                      return (
                        <Select.Option key={v} value={v}>
                          {v}
                        </Select.Option>
                      );
                    })}
                  </Select>
                </Col>
              </Row>
            </Space>
          </Radio.Group>
        </>
      ),
    },
    {
      label: intl.formatMessage({ id: 'pages.project.di.job.crontab.week' }),
      key: 'week',
      children: (
        <>
          <Radio.Group
            onChange={(e) => {
              setCronWeek({ ...cronWeek, type: e.target.value });
              cronWeekRef.current = { ...cronWeek, type: e.target.value };
              buildCrontab();
            }}
          >
            <Space direction="vertical">
              <Radio value={0}>
                {intl.formatMessage({ id: 'pages.project.di.job.crontab.undesignated.week' })}
              </Radio>
              <Radio value={1}>
                {intl.formatMessage({ id: 'pages.project.di.job.crontab.every' }) +
                  intl.formatMessage({ id: 'pages.project.di.job.crontab.week' })}
              </Radio>
              <Radio value={2}>
                <Space>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range' })}:
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range.from' })}
                  <InputNumber
                    defaultValue={cronWeek.min}
                    min={cronWeek.min}
                    max={cronWeek.max}
                    disabled={cronWeek.type != 2}
                    onChange={(e) => {
                      setCronWeek({ ...cronWeek, intervalFrom: e as number });
                      cronWeekRef.current = { ...cronWeek, intervalFrom: e as number };
                      buildCrontab();
                    }}
                  ></InputNumber>
                  {intl.formatMessage({ id: 'pages.project.di.job.crontab.range.to' }) +
                    intl.formatMessage({ id: 'pages.project.di.job.crontab.week' })}
                  <InputNumber
                    defaultValue={cronWeek.min}
                    min={cronWeek.min}
                    max={cronWeek.max}
                    disabled={cronWeek.type != 2}
                    onChange={(e) => {
                      setCronWeek({ ...cronWeek, intervalTo: e as number });
                      cronWeekRef.current = { ...cronWeek, intervalTo: e as number };
                      buildCrontab();
                    }}
                  ></InputNumber>
                </Space>
              </Radio>
              <Row gutter={[0, 0]} align="middle">
                <Col>
                  <Radio value={3}>
                    {intl.formatMessage({ id: 'pages.project.di.job.crontab.designated' }) + ' :'}
                  </Radio>
                </Col>
                <Col>
                  <Select
                    style={{ width: '260px' }}
                    allowClear={true}
                    mode="multiple"
                    maxTagCount="responsive"
                    disabled={cronWeek.type != 3}
                    onChange={(e) => {
                      setCronWeek({ ...cronWeek, custom: e as number[] });
                      cronWeekRef.current = { ...cronWeek, custom: e as number[] };
                      buildCrontab();
                    }}
                  >
                    {weeks.map((v, i) => {
                      return (
                        <Select.Option key={v} value={v}>
                          {v}
                        </Select.Option>
                      );
                    })}
                  </Select>
                </Col>
              </Row>
            </Space>
          </Radio.Group>
        </>
      ),
    },
  ];

  useEffect(() => {
    setMinutes(buildNums(0, 59));
    setHours(buildNums(0, 23));
    setDays(buildNums(1, 31));
    setMonths(buildNums(1, 12));
    setWeeks(buildNums(1, 7));
    setCrontabStr(data?.jobCrontab ? (data?.jobCrontab as string) : '0 * * * * ?');
    crontabStrRef.current = data?.jobCrontab ? (data?.jobCrontab as string) : '0 * * * * ?';
    buildCrontab();
  }, []);

  const buildNums = (from: number, to: number) => {
    let nums: number[] = [];
    for (let i = from; i <= to; i++) {
      nums.push(i);
    }
    return nums;
  };

  const buildCrontab = () => {
    let str = crontabStr;
    if (activeTabId != 'custom') {
      //minute
      if (cronMinuteRef.current.type === 1) {
        cronMinuteRef.current.value = '*';
      } else if (cronMinuteRef.current.type === 2) {
        cronMinuteRef.current.value =
          cronMinuteRef.current.intervalFrom + '-' + cronMinuteRef.current.intervalTo;
      } else if (cronMinuteRef.current.type === 3) {
        cronMinuteRef.current.value =
          cronMinuteRef.current.loopFrom + '/' + cronMinuteRef.current.loopTo;
      } else if (cronMinuteRef.current.type === 4) {
        cronMinuteRef.current.value = cronMinuteRef.current.custom?.join();
      }
      //hour
      if (cronHourRef.current.type === 1) {
        cronHourRef.current.value = '*';
      } else if (cronHourRef.current.type === 2) {
        cronHourRef.current.value =
          cronHourRef.current.intervalFrom + '-' + cronHourRef.current.intervalTo;
      } else if (cronHourRef.current.type === 3) {
        cronHourRef.current.value = cronHourRef.current.loopFrom + '/' + cronHourRef.current.loopTo;
      } else if (cronHourRef.current.type === 4) {
        cronHourRef.current.value = cronHourRef.current.custom?.join();
      }
      //day
      if (cronDayRef.current.type === 0) {
        cronDayRef.current.value = '?';
      } else if (cronDayRef.current.type === 1) {
        cronDayRef.current.value = '*';
      } else if (cronDayRef.current.type === 2) {
        cronDayRef.current.value = 'L';
      } else if (cronDayRef.current.type === 3) {
        cronDayRef.current.value =
          cronDayRef.current.intervalFrom + '-' + cronDayRef.current.intervalTo;
      } else if (cronDayRef.current.type === 4) {
        cronDayRef.current.value = cronDayRef.current.loopFrom + '/' + cronDayRef.current.loopTo;
      } else if (cronDayRef.current.type === 5) {
        cronDayRef.current.value = cronDayRef.current.custom?.join();
      }
      //month
      if (cronMonthRef.current.type === 1) {
        cronMonthRef.current.value = '*';
      } else if (cronMonthRef.current.type === 2) {
        cronMonthRef.current.value =
          cronMonthRef.current.intervalFrom + '-' + cronMonthRef.current.intervalTo;
      } else if (cronMonthRef.current.type === 3) {
        cronMonthRef.current.value =
          cronMonthRef.current.loopFrom + '/' + cronMonthRef.current.loopTo;
      } else if (cronMonthRef.current.type === 4) {
        cronMonthRef.current.value = cronMonthRef.current.custom?.join();
      }
      //week
      if (cronWeekRef.current.type === 0) {
        cronWeekRef.current.value = '?';
      } else if (cronWeekRef.current.type === 1) {
        cronWeekRef.current.value = '*';
      } else if (cronWeekRef.current.type === 2) {
        cronWeekRef.current.value =
          cronWeekRef.current.intervalFrom + '-' + cronWeekRef.current.intervalTo;
      } else if (cronWeekRef.current.type === 3) {
        cronWeekRef.current.value = cronWeekRef.current.custom?.join();
      }
      str =
        '0 ' +
        cronMinuteRef.current.value +
        ' ' +
        cronHourRef.current.value +
        ' ' +
        cronDayRef.current.value +
        ' ' +
        cronMonthRef.current.value +
        ' ' +
        cronWeekRef.current.value;
      setCrontabStr(str);
    } else {
      str = crontabStrRef.current;
    }
    JobService.listNext5FireTime(str).then((d: any) => {
      if (d?.success == false) {
        null;
      } else {
        setFireTimeList(d);
      }
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
        if (crontabStr != '' && crontabStr != undefined && crontabStr != null) {
          let d: DiJob = {
            id: data.id,
            jobCrontab: crontabStr,
            projectId: data.projectId,
            jobCode: data.jobCode,
            jobName: data.jobName,
            directory: data.directory,
          };
          JobService.updateJob(d).then((resp) => {
            if (resp.success) {
              message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
              onVisibleChange ? onVisibleChange(false) : null;
            }
          });
        }
      }}
    >
      <Tabs
        items={tabItems}
        centered={true}
        onChange={(activeKey: string) => {
          console.log(activeKey);
          setActiveTabId(activeKey);
        }}
      ></Tabs>
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
            {fireTimeList &&
              fireTimeList.map((item, index) => {
                return (
                  <Typography.Text key={index}>
                    <br />
                    {moment(item).format('YYYY-MM-DD HH:mm:ss')}
                  </Typography.Text>
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
