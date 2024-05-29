import {useAccess, useIntl} from "@umijs/max";
import {PageContainer} from "@ant-design/pro-components";

const MetadataGravitinoCatalogWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();

  return (
    <PageContainer title={false}>待开发</PageContainer>
  );
}

export default MetadataGravitinoCatalogWeb;
