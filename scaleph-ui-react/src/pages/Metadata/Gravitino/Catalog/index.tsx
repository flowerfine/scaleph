import {useAccess, useIntl} from "@umijs/max";
import {PageContainer} from "@ant-design/pro-components";

const MetadataGravitinoCatalogWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();

  return (
    <PageContainer title={false}>
      <iframe
        src={"http://localhost:8090"}
        width="100%"
        style={{
          height:"75vh"
        }}
      />
    </PageContainer>
  );
}

export default MetadataGravitinoCatalogWeb;
