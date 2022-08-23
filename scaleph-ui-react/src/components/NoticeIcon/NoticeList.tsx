import { LogMessage } from '@/services/admin/typings';
import { MailOutlined } from '@ant-design/icons';
import { Avatar, Empty, List } from 'antd';
import classNames from 'classnames';
import React from 'react';
import styles from './NoticeList.less';

export type NoticeIconTabProps = {
  count?: number;
  showClear?: boolean;
  showViewMore?: boolean;
  style?: React.CSSProperties;
  title: string;
  tabKey: string;
  onClick?: (item: LogMessage) => void;
  onClear?: () => void;
  emptyText?: string;
  clearText?: string;
  viewMoreText?: string;
  list: LogMessage[];
  onViewMore?: (e: any) => void;
};

const NoticeList: React.FC<NoticeIconTabProps> = ({
  list = [],
  onClick,
  onClear,
  title,
  onViewMore,
  emptyText,
  showClear = true,
  clearText,
  viewMoreText,
  showViewMore = false,
}) => {
  if (!list || list.length === 0) {
    return <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}></Empty>;
  }
  return (
    <div>
      <List<LogMessage>
        className={styles.list}
        dataSource={list}
        renderItem={(item, i) => {
          const itemCls = classNames(styles.item);
          return (
            <div
              onClick={() => {
                onClick?.(item);
              }}
            >
              <List.Item className={itemCls} key={item.id + '' || i}>
                <List.Item.Meta
                  className={styles.meta}
                  avatar={<Avatar icon={<MailOutlined />}></Avatar>}
                  title={<div className={styles.title}>{item.title}</div>}
                  description={
                    <div>
                      <div className={styles.description}>{item.content}</div>
                      <div className={styles.datetime}>{item.createTime}</div>
                    </div>
                  }
                />
              </List.Item>
            </div>
          );
        }}
      />
      <div className={styles.bottomBar}>
        {showClear ? <div onClick={onClear}>{clearText}</div> : null}
        {showViewMore ? (
          <div
            onClick={(e) => {
              if (onViewMore) {
                onViewMore(e);
              }
            }}
          >
            {viewMoreText}
          </div>
        ) : null}
      </div>
    </div>
  );
};

export default NoticeList;
