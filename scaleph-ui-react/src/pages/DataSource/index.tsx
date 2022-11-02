import DataSourceCategoryWeb from "@/pages/DataSource/components/Category";
import DataSourceTypeWeb from "@/pages/DataSource/components/DataSourceType";
import {Col, Row} from "antd";
import {ProCard} from "@ant-design/pro-components";
import {useState} from "react";

const DataSourceCategoryAndTypeWeb: React.FC = () => {
  const [categoryId, setCategoryId] = useState<number | undefined>()
  return (
    <ProCard>
      <Row>
        <Col span={4}>
          <DataSourceCategoryWeb onCategoryChange={(categoryId) => setCategoryId(categoryId)}/>
        </Col>
        <Col>
          <DataSourceTypeWeb categoryId={categoryId}/>
        </Col>
      </Row>¬
    </ProCard>
  );
}

export default DataSourceCategoryAndTypeWeb;
