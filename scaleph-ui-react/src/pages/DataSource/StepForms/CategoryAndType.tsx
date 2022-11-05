import DataSourceTypeWeb from "@/pages/DataSource/StepForms/DataSourceType";
import {Col, Row} from "antd";
import {ProCard} from "@ant-design/pro-components";
import {useState} from "react";
import DataSourceCategoryMenu from "@/pages/DataSource/StepForms/CategoryMenu";

const DataSourceCategoryAndTypeWeb: React.FC<{onTypeSelect: () => void}> = ({onTypeSelect}) => {
  const [categoryId, setCategoryId] = useState<number | undefined>()
  return (
    <ProCard>
      <Row>
        <Col span={4}>
          <DataSourceCategoryMenu onCategoryChange={(categoryId) => setCategoryId(categoryId)}/>
        </Col>
        <Col span={20}>
          <DataSourceTypeWeb categoryId={categoryId} onTypeSelect={() => onTypeSelect()}/>
        </Col>
      </Row>
    </ProCard>
  );
}

export default DataSourceCategoryAndTypeWeb;
