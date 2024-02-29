import { MessageService } from '@/services/admin/message.service';
import { LogMessage } from '@/services/admin/typings';
import { useEffect, useState } from 'react';
import { history, useIntl } from 'umi';
import styles from './index.less';
import NoticeIcon from './NoticeIcon';

export type GlobalHeaderRightProps = {
  fetchingNotices?: boolean;
  onNoticeVisibleChange?: (visible: boolean) => void;
  onNoticeClear?: (tabName?: string) => void;
};

const NoticeIconView: React.FC = () => {
  const intl = useIntl();
  const [messages, setMessages] = useState<LogMessage[]>([]);
  const [unReadCnt, setUnReadCnt] = useState<number>(0);

  useEffect(() => {
    refreshMessage();
  }, []);

  const refreshMessage = () => {
    MessageService.listMessageByPage({ pageSize: 1000, current: 1, isRead: '0' }).then((resp) => {
      setMessages(resp.data);
    });
    MessageService.countUnReadMessage().then((resp) => {
      setUnReadCnt(resp);
    });
  };

  return (
    <NoticeIcon
      className={styles.action}
      count={unReadCnt}
      onItemClick={(item) => {
        MessageService.updateMessage(item).then((resp) => {
          if (resp.success) {
            refreshMessage();
          }
        });
      }}
      onClear={() =>
        MessageService.readAllMessage().then((resp) => {
          if (resp.success) {
            refreshMessage();
          }
        })
      }
      loading={false}
      clearText={intl.formatMessage({ id: 'app.common.operate.clear.label' })}
      viewMoreText={intl.formatMessage({ id: 'app.common.operate.more.label' })}
      onViewMore={() => history.push('/user/center', { defaultMenu: 'log' })}
      clearClose
    >
      <NoticeIcon.Tab
        tabKey="notification"
        count={unReadCnt}
        list={messages}
        title={intl.formatMessage({ id: 'app.common.message.system' })}
        showViewMore
        showClear
      ></NoticeIcon.Tab>
      {/* <NoticeIcon.Tab
        tabKey="notification"
        count={unReadCnt}
        list={messages}
        title="系统消息"
        showViewMore
        showClear
      ></NoticeIcon.Tab> */}
    </NoticeIcon>
  );
};

export default NoticeIconView;
