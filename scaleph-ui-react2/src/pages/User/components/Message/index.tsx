import { MessageService } from '@/services/admin/message.service';
import { LogMessage } from '@/services/admin/typings';
import { Card, Collapse, Pagination, Space, Tag, Tooltip, Typography } from 'antd';
import moment from 'moment';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const Message: React.FC = () => {
  const intl = useIntl();
  const [messages, setMessages] = useState<LogMessage[]>([]);
  const [pageInfo, setPageInfo] = useState<{ pageSize: number; current: number }>({
    pageSize: 10,
    current: 1,
  });
  const [total, setTotal] = useState<number>(0);

  useEffect(() => {
    refreshMessages(pageInfo.pageSize, pageInfo.current);
  }, []);

  const refreshMessages = (pageSize: number, current: number) => {
    MessageService.listMessageByPage({ pageSize, current }).then((resp) => {
      setMessages(resp.data);
      setTotal(resp.total);
    });
  };

  const messageListView = (messageList: LogMessage[]) => {
    return (
      <Collapse
        collapsible="header"
        expandIconPosition="end"
        onChange={(key) => {
          if (typeof key == 'string') {
          }
        }}
      >
        {messageList.map((item) => {
          return (
            <Collapse.Panel
              header={
                <Space
                  onClick={() => {
                    if (item.isRead?.value == '0') {
                      item.isRead.value = '1';
                      MessageService.updateMessage(item).then((resp) => {
                        if (resp.success) {
                          refreshMessages(pageInfo.pageSize, pageInfo.current);
                        }
                      });
                    }
                  }}
                >
                  {panelExtra(item)}
                  <Typography.Text>{item.title}</Typography.Text>
                </Space>
              }
              key={item.id + ''}
            >
              <Typography.Text>{item.content}</Typography.Text>
              <br />
              <Tooltip title={item.createTime}>
                <Typography.Text type="secondary">
                  {moment(item.createTime).fromNow()}
                </Typography.Text>
              </Tooltip>
            </Collapse.Panel>
          );
        })}
      </Collapse>
    );
  };

  const panelExtra = (item: LogMessage) => {
    return (
      <Tag color={item.isRead?.value == '0' ? 'magenta' : 'green'}>
        {item.isRead?.value == '0'
          ? intl.formatMessage({ id: 'pages.admin.usercenter.message.unRead' })
          : intl.formatMessage({ id: 'pages.admin.usercenter.message.read' })}
      </Tag>
    );
  };

  return (
    <div>
      <Card bordered={false}>
        {messageListView(messages)}
        <Pagination
          size="small"
          showSizeChanger
          showQuickJumper
          hideOnSinglePage
          onChange={(page, pageSize) => {
            setPageInfo({ pageSize: pageSize, current: page });
            refreshMessages(pageSize, page);
          }}
          total={total}
          pageSize={pageInfo.pageSize}
          current={pageInfo.current}
          style={{ paddingTop: 12 }}
        ></Pagination>
      </Card>
    </div>
  );
};

export default Message;
