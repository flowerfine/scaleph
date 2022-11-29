import { useIntl } from 'umi';
import { ProFormList, ProFormText } from '@ant-design/pro-components';
import { Col, Divider, Row } from 'antd';
import { useState } from 'react';

const Additional: React.FC = () => {
  const intl = useIntl();
  const [visible, setVisible] = useState<boolean>(false);
  return (
    <>
      <Divider orientation="left">
        <span
          onClick={() => {
            setVisible(visible ? false : true);
          }}
          style={{ cursor: 'pointer' }}
        >
          Additional Config Options
        </span>
      </Divider>
      {visible && (
        <>
          <ProFormList
            name="options"
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({
                id: 'pages.dev.clusterConfig.configOptions',
              }),
              type: 'text',
            }}
          >
            <Row gutter={[12, 12]}>
              <Col span={12}>
                <ProFormText
                  name="key"
                  label={intl.formatMessage({ id: 'pages.dev.clusterConfig.configOptions.key' })}
                />
              </Col>
              <Col span={12}>
                <ProFormText
                  name="value"
                  label={intl.formatMessage({ id: 'pages.dev.clusterConfig.configOptions.value' })}
                />
              </Col>
            </Row>
          </ProFormList>
        </>
      )}
    </>
  );
};

export default Additional;
