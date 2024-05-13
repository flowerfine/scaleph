import React from 'react';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
    ProFormDependency,
    ProFormDigit,
    ProFormGroup,
    ProFormSelect,
    ProFormSwitch,
    ProFormText
} from "@ant-design/pro-components";
import {getIntl, getLocale} from "@umijs/max";
import {
  BaseFileParams,
  SchemaParams
} from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/constant";

const FileSinkItem: React.FC = () => {
    const intl = getIntl(getLocale());

    const compressCodec = (
        <ProFormSelect
            name={BaseFileParams.compressCodec}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.compressCodec'})}
            options={["lzo", "none"]}
        />
    )

    return (
        <ProFormGroup>
            <ProFormText
                name={BaseFileParams.path}
                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.path'})}
                rules={[{required: true}]}
                colProps={{span: 24}}
            />
            <ProFormText
                name={BaseFileParams.tmpPath}
                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.tmpPath'})}
                tooltip={{
                    title: intl.formatMessage({id: 'pages.project.di.step.baseFile.tmpPath.tooltip'}),
                    icon: <InfoCircleOutlined/>,
                }}
                rules={[{required: true}]}
                colProps={{span: 24}}
                initialValue={"/tmp/seatunnel"}
            />
          <ProFormText
            name={BaseFileParams.encoding}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.encoding'})}
            placeholder={intl.formatMessage({id: 'pages.project.di.step.baseFile.encoding.placeholder'})}
          />
            <ProFormSelect
                name={BaseFileParams.fileFormatType}
                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileFormat'})}
                colProps={{span: 24}}
                options={['json', 'parquet', 'orc', 'text', 'csv', 'excel', 'xml']}
            />
            <ProFormDependency name={[BaseFileParams.fileFormatType]}>
                {({file_format_type}) => {
                    if (file_format_type == 'json') {
                        return (<ProFormGroup>{compressCodec}</ProFormGroup>)
                    }
                    if (file_format_type == 'text' || file_format_type == 'csv') {
                        return (
                            <ProFormGroup>
                                {compressCodec}
                              <ProFormSwitch
                                name={BaseFileParams.enableHeaderWrite}
                                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.enableHeaderWrite'})}
                                colProps={{span: 8}}
                                initialValue={false}
                              />
                                <ProFormText
                                    name={BaseFileParams.fieldDelimiter}
                                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fieldDelimiter'})}
                                    rules={[{required: true}]}
                                    colProps={{span: 8}}
                                />
                                <ProFormText
                                    name={BaseFileParams.rowDelimiter}
                                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.rowDelimiter'})}
                                    rules={[{required: true}]}
                                    colProps={{span: 8}}
                                />
                            </ProFormGroup>
                        );
                    }
                    if (file_format_type == 'excel') {
                        return (
                            <ProFormGroup>
                                <ProFormText
                                    name={BaseFileParams.sheetName}
                                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.sheetName'})}
                                    colProps={{span: 12}}
                                />
                                <ProFormText
                                    name={BaseFileParams.maxRowsInMemory}
                                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.maxRowsInMemory'})}
                                    rules={[{required: true}]}
                                    colProps={{span: 12}}
                                />
                            </ProFormGroup>
                        );
                    }
                  if (file_format_type == 'xml') {
                    return (
                      <ProFormGroup>
                        <ProFormText
                          name={SchemaParams.xmlRootTag}
                          label={intl.formatMessage({id: 'pages.project.di.step.schema.xmlRootTag'})}
                          placeholder={intl.formatMessage({id: 'pages.project.di.step.schema.xmlRootTag.placeholder'})}
                          colProps={{span: 8}}
                        />
                        <ProFormText
                          name={SchemaParams.xmlRowTag}
                          label={intl.formatMessage({id: 'pages.project.di.step.schema.xmlRowTag'})}
                          placeholder={intl.formatMessage({id: 'pages.project.di.step.schema.xmlRowTag.placeholder'})}
                          colProps={{span: 8}}
                        />
                        <ProFormSwitch
                          name={SchemaParams.xmlUseAttrFormat}
                          label={intl.formatMessage({id: 'pages.project.di.step.schema.xmlUseAttrFormat'})}
                          colProps={{span: 8}}
                        />
                      </ProFormGroup>
                    );
                  }
                    return <ProFormGroup/>;
                }}
            </ProFormDependency>
            <ProFormSwitch
                name={BaseFileParams.customFilename}
                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.customFilename'})}
                colProps={{span: 12}}
                initialValue={false}
            />
            <ProFormSwitch
                name={BaseFileParams.havePartition}
                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.havePartition'})}
                colProps={{span: 12}}
                initialValue={false}
            />
            <ProFormDependency name={[BaseFileParams.customFilename]}>
                {({custom_filename}) => {
                    if (custom_filename) {
                        return (<ProFormGroup>
                            <ProFormText
                                name={BaseFileParams.fileNameExpression}
                                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileNameExpression'})}
                                colProps={{span: 12}}
                            />
                            <ProFormText
                                name={BaseFileParams.filenameTimeFormat}
                                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.filenameTimeFormat'})}
                                colProps={{span: 12}}
                            />
                        </ProFormGroup>)
                    }
                }}
            </ProFormDependency>
            <ProFormDependency name={[BaseFileParams.havePartition]}>
                {({have_partition}) => {
                    if (have_partition) {
                        return (<ProFormGroup>
                            <ProFormText
                                name={BaseFileParams.partitionBy}
                                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.partitionBy'})}
                                colProps={{span: 8}}
                            />
                            <ProFormText
                                name={BaseFileParams.partitionDirExpression}
                                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.partitionDirExpression'})}
                                colProps={{span: 8}}
                            />
                            <ProFormSwitch
                                name={BaseFileParams.isPartitionFieldWriteInFile}
                                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isPartitionFieldWriteInFile'})}
                                colProps={{span: 8}}
                            />
                        </ProFormGroup>)
                    }
                }}
            </ProFormDependency>
            <ProFormText
                name={BaseFileParams.sinkColumns}
                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.sinkColumns'})}
                colProps={{span: 24}}
            />
            <ProFormSwitch
                name={BaseFileParams.isEnableTransaction}
                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isEnableTransaction'})}
                colProps={{span: 24}}
                initialValue={true}
                fieldProps={{
                    disabled: true,
                }}
            />
            <ProFormDigit
                name={BaseFileParams.batchSize}
                label={intl.formatMessage({id: 'pages.project.di.step.baseFile.batchSize'})}
                colProps={{span: 24}}
                initialValue={1000000}
                fieldProps={{
                    step: 10000,
                    min: 0,
                }}
            />
        </ProFormGroup>
    );
}

export default FileSinkItem;
