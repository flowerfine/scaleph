import React from 'react';
import {useLocation} from '@umijs/max';
import {PageContainer} from '@ant-design/pro-components';

const DiJobFlow: React.FC = () => {
  const props = useLocation();

  return (
    <PageContainer title={false}>
      <div>DiJobFlow</div>
    </PageContainer>
  );
};

export default DiJobFlow;
