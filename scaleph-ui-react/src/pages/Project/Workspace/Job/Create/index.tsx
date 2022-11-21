import { PRIVILEGE_CODE } from '@/constant';
import { PageContainer, PageHeader } from '@ant-design/pro-components';
import { Button } from 'antd';
import { useAccess, useIntl } from 'umi';

const JobCreateView: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  return (
    <>
      {/* <PageHeader
        title={intl.formatMessage({ id: 'pages.project.list' })}
        // onBack={() => window.history.back()}
        extra={
          <>
            {access.canAccess(PRIVILEGE_CODE.datadevProjectAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  // setProjectFormData({ visible: true, data: {} });
                }}
              >
                {intl.formatMessage({ id: 'pages.project.create' })}
              </Button>
            )}
          </>
        }
      /> */}
      <PageContainer
        header={{ title: '创建作业' }}
        footer={[
          <Button key="rest">关闭</Button>,
          <Button key="submit" type="primary">
            提交
          </Button>,
        ]}
      >
        内容
      </PageContainer>
    </>
  );
};
export default JobCreateView;
