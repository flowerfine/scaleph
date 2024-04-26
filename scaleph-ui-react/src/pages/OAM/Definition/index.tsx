import {useAccess, useIntl} from '@umijs/max';
import {PageContainer} from '@ant-design/pro-components';

const OamDefinitionWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();

  return (
    <PageContainer>
      <div>Definitions</div>
    </PageContainer>
  );
};

export default OamDefinitionWeb;
