import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout, SelectComponent } from 'ng-devui';
import { Observable, of } from 'rxjs';
import { DEFAULT_PAGE_PARAM, Dict, DICT_TYPE } from 'src/app/@core/data/app.data';
import { MetaDataSet, MetaDataSetType, MetaDataSetTypeParam, MetaSystem, MetaSystemParam } from 'src/app/@core/data/stdata.data';
import { DictDataService } from 'src/app/@core/services/admin/dict-data.service';
import { SystemService } from 'src/app/@core/services/stdata/system.service';
import { RefdataService } from 'src/app/@core/services/stdata/refdata.service';

@Component({
  selector: 'app-refdata-data-update',
  templateUrl: './refdata-data-update.component.html',
  styleUrls: ['../refdata.component.scss'],
})
export class RefdataDataUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  @ViewChild('systemSelect', { static: true }) systemSelect: SelectComponent;
  @ViewChild('dataSetTypeSelect', { static: true }) dataSetTypeSelect: SelectComponent;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    dataSetTypeRules: {
      validators: [{ required: true }],
    },
    dataSetCodeRules: {
      validators: [
        { required: true },
        { maxlength: 32 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
    },
    dataSetValueRules: {
      validators: [{ required: true }, { maxlength: 120 }],
    },
    isStandardRules: {
      validators: [{ required: true }],
    },
    remarkRules: {
      validators: [{ maxlength: 200 }],
    },
  };

  formData = {
    id: null,
    dataSetType: null,
    dataSetCode: null,
    dataSetValue: null,
    system: null,
    isStandard: null,
    remark: null,
  };
  nullableList: Dict[] = [];
  private systemMap: Map<number, MetaSystem> = new Map();
  systemSearchStr: any;
  systemSearchPager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  private metaDataSetTypeMap: Map<number, MetaDataSetType> = new Map();
  metaDataSetTypeSearchStr: any;
  metaDataSetTypeSearchPager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private refdataService: RefdataService,
    private dictDataService: DictDataService,
    private systemService: SystemService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.dictDataService.listByType(DICT_TYPE.yesNo).subscribe((d) => {
      this.nullableList = d;
    });
    this.formData = {
      id: this.data.item.id,
      dataSetType: this.data.item.dataSetType,
      dataSetCode: this.data.item.dataSetCode,
      dataSetValue: this.data.item.dataSetValue,
      system: this.data.item.system,
      isStandard: this.data.item.isStandard,
      remark: this.data.item.remark,
    };
  }

  submitForm({ valid }) {
    let d: MetaDataSet = {
      id: this.formData.id,
      dataSetType: this.formData.dataSetType,
      dataSetCode: this.formData.dataSetCode,
      dataSetValue: this.formData.dataSetValue,
      system: this.formData.system,
      isStandard: this.formData.isStandard,
      remark: this.formData.remark,
    };
    if (valid) {
      this.refdataService.updateData(d).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }

  searchSystem = (term): Observable<{ id: string | number; option: any }[]> => {
    if (
      this.systemSearchStr === term &&
      this.systemSearchPager.total > this.systemSearchPager.pageSize * (this.systemSearchPager.pageIndex - 1)
    ) {
      let param: MetaSystemParam = {
        pageSize: this.systemSearchPager.pageSize,
        current: this.systemSearchPager.pageIndex++,
        systemName: term,
      };
      this.systemService.listByPage(param).then((d) => {
        const list: MetaSystem[] = d.records;
        list.forEach((m) => {
          this.systemMap.set(m.id, m);
        });
        this.systemSearchPager.total = d.total;
        this.systemSelect.loadFinish();
      });
    } else {
      this.systemSearchStr = term;
      this.systemSearchPager = {
        total: 0,
        pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
        pageSize: DEFAULT_PAGE_PARAM.pageSize,
        pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
      };
      let param: MetaSystemParam = {
        pageSize: this.systemSearchPager.pageSize,
        current: this.systemSearchPager.pageIndex,
        systemName: term,
      };
      this.systemService.listByPage(param).then((d) => {
        const list: MetaSystem[] = d.records;
        list.forEach((m) => {
          this.systemMap.set(m.id, m);
        });
        this.systemSearchPager.total = d.total;
        this.systemSelect.loadFinish();
      });
    }
    let systems: MetaSystem[] = [];
    this.systemMap.forEach((option, index) => {
      systems.push(option);
    });
    return of(
      systems
        .map((option, index) => ({ id: option.id, option: option }))
        .filter((item) => {
          return item.option.systemName.toLowerCase().indexOf(term.toLowerCase()) !== -1;
        })
    );
  };

  loadMoreSystem(data: any) {
    this.systemSelect.forceSearchNext();
  }

  searchDataSetType = (term) => {
    if (
      this.metaDataSetTypeSearchStr === term &&
      this.metaDataSetTypeSearchPager.total > this.metaDataSetTypeSearchPager.pageSize * (this.metaDataSetTypeSearchPager.pageIndex - 1)
    ) {
      let param: MetaDataSetTypeParam = {
        pageSize: this.metaDataSetTypeSearchPager.pageSize,
        current: this.metaDataSetTypeSearchPager.pageIndex++,
        dataSetTypeCode: term,
      };

      this.refdataService.listTypeByPage(param).subscribe((d) => {
        const list: MetaDataSetType[] = d.records;
        list.forEach((m) => {
          this.metaDataSetTypeMap.set(m.id, m);
        });
        this.metaDataSetTypeSearchPager.total = d.total;
        this.dataSetTypeSelect.loadFinish();
      });
    } else {
      this.metaDataSetTypeSearchStr = term;
      this.metaDataSetTypeSearchPager = {
        total: 0,
        pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
        pageSize: DEFAULT_PAGE_PARAM.pageSize,
        pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
      };
      let param: MetaDataSetTypeParam = {
        pageSize: this.metaDataSetTypeSearchPager.pageSize,
        current: this.metaDataSetTypeSearchPager.pageIndex,
        dataSetTypeCode: term,
      };
      this.refdataService.listTypeByPage(param).subscribe((d) => {
        const list: MetaDataSetType[] = d.records;
        list.forEach((m) => {
          this.metaDataSetTypeMap.set(m.id, m);
        });
        this.metaDataSetTypeSearchPager.total = d.total;
        this.dataSetTypeSelect.loadFinish();
      });
    }
    let mdts: MetaDataSetType[] = [];
    this.metaDataSetTypeMap.forEach((option, index) => {
      mdts.push(option);
    });
    return of(
      mdts
        .map((option, index) => ({ id: option.id, option: option }))
        .filter((item) => {
          return item.option.dataSetTypeCode.toLowerCase().indexOf(term.toLowerCase()) !== -1 || item.option.dataSetTypeName.toLowerCase().indexOf(term.toLowerCase()) !== -1;
        })
    );
  };

  loadMoreDataSetType(data: any) {
    this.dataSetTypeSelect.forceSearchNext();
  }
}
