import { AfterViewInit, Component, ElementRef, Injector, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DrawerService, IDrawerOpenResult, ModalService, SplitterOrientation, ToastService } from 'ng-devui';
import { Graph, Addon, Cell, Shape, Edge } from '@antv/x6';
import { WORKBENCH_CONFIG } from 'src/config/workbench-config';
import { DiJob, WORKBENCH_MENU } from 'src/app/@core/data/datadev.data';
import '@antv/x6-angular-shape';
import { BaseNodeComponent } from './base-node/base-node.component';
import { Node } from '@antv/x6';
import { ActivatedRoute } from '@angular/router';
import { uuid } from '@antv/x6/lib/util/string/uuid';
import { TranslateService } from '@ngx-translate/core';
import { JobPropertityComponent } from './job-propertity/job-propertity.component';
import { StepPropertityComponent } from './step-propertity/step-propertity.component';
import { ThemeType } from 'src/app/@shared/models/theme';
import { PersonalizeService } from 'src/app/@core/services/personalize.service';
import { ResponseBody } from 'src/app/@core/data/app.data';
import { JobService } from 'src/app/@core/services/datadev/job.service';
@Component({
  selector: 'app-workbench',
  templateUrl: './workbench.component.html',
  styleUrls: ['./workbench.component.scss'],
})
export class WorkbenchComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('mainContainer') mainContainer: ElementRef;
  @ViewChild('menuContainer') menuContainer: ElementRef;
  stepDrawer: IDrawerOpenResult;
  language;
  graph: Graph;
  dnd: Addon.Dnd;
  currentCell: Cell;
  orientation: SplitterOrientation = 'horizontal';
  size = '12%';
  minSize = '12%';
  maxSize = '12%';
  menu = WORKBENCH_MENU;
  zoomOptions = [
    { label: '200%', value: 2 },
    { label: '150%', value: 1.5 },
    { label: '100%', value: 1 },
    { label: '75%', value: 0.75 },
    { label: '50%', value: 0.5 },
  ];
  zoomOptionSize = { label: '100%', value: 1 };
  port = { in: 'inPort', out: 'outPort' };
  job: DiJob;
  savedJobId: number = null;
  constructor(
    private injector: Injector,
    private jobService: JobService,
    private route: ActivatedRoute,
    private personalizeService: PersonalizeService,
    private modalService: ModalService,
    private translate: TranslateService,
    private drawerService: DrawerService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.language = this.translate.currentLang;
    this.personalizeService.setRefTheme(ThemeType.Default);
  }

  ngAfterViewInit(): void {
    this.initGraph();
    this.refreshGraph();
  }

  ngOnDestroy(): void {
    this.graph.dispose();
  }

  initGraph(): void {
    //init x6
    this.graph = new Graph({
      ...WORKBENCH_CONFIG,
      container: this.mainContainer.nativeElement,
      connecting: {
        snap: true,
        allowBlank: false,
        allowMulti: false,
        allowLoop: false,
        allowNode: false,
        allowEdge: false,
        connector: 'smooth',
        validateConnection({
          edge,
          edgeView,
          sourceView,
          targetView,
          sourcePort,
          targetPort,
          sourceMagnet,
          targetMagnet,
          sourceCell,
          targetCell,
          type,
        }) {
          if (sourcePort === 'outPort' && targetPort === 'inPort') {
            return true;
          } else {
            return false;
          }
        },
      },
    });
    //bind keyboard
    //todo bind ctrl+s  crrl+x
    this.graph
      .bindKey('ctrl+c', () => {
        const cells = this.graph.getSelectedCells();
        if (cells.length) {
          this.graph.copy(cells);
        }
        return false;
      })
      .bindKey('ctrl+x', () => {
        const cells = this.graph.getSelectedCells();
        if (cells.length) {
          this.graph.copy(cells);
          this.graph.removeCells(cells);
        }
      })
      .bindKey('ctrl+v', () => {
        if (!this.graph.isClipboardEmpty()) {
          const cells = this.graph.paste({ offset: 50 });
          this.graph.cleanSelection();
          this.graph.select(cells);
        }
        return false;
      })
      .bindKey('ctrl+z', () => {
        if (this.graph.history.canUndo()) {
          this.graph.history.undo();
        }
      })
      .bindKey('ctrl+y', () => {
        if (this.graph.history.canRedo()) {
          this.graph.history.redo();
        }
      })
      .bindKey('delete', () => {
        this.deleteCell();
      });
    //event listener
    this.graph
      .on('cell:contextmenu', ({ e, x, y, cell, view }) => {
        this.currentCell = cell;
        this.menuContainer.nativeElement.style.top = e.pageY - 45 + 'px';
        this.menuContainer.nativeElement.style.left = e.pageX - 240 + 'px';
        this.menuContainer.nativeElement.style.display = 'flex';
      })
      .on('cell:click', ({ e, x, y, cell, view }) => {
        this.currentCell = cell;
        this.menuContainer.nativeElement.style.display = 'none';
      })
      .on('node:dblclick', ({ e, x, y, cell, view }) => {
        this.currentCell = cell;
        this.openStepPropertityDialog(cell);
      })
      .on('blank:click', () => {
        this.menuContainer.nativeElement.style.display = 'none';
      })
      .on('blank:dblclick', ({ e, x, y }) => {
        this.openJobPropertityDialog();
      });
    // drag and drop
    this.dnd = new Addon.Dnd({
      target: this.graph,
    });
    //注册angular节点
    this.registerAngularNode();
    this.graph.centerContent();
  }

  refreshGraph(): void {
    this.route.queryParams.subscribe((params) => {
      let id: number = this.savedJobId ? this.savedJobId : params['id'];
      if (id != null && id != undefined && id != 0) {
        this.jobService.selectById(id).subscribe((d) => {
          this.job = d;
          this.loadJobGraph();
        });
      }
    });
  }
  /**
   * 加载作业信息
   */
  loadJobGraph(): void {
    this.graph.clearCells();
    let jobStepList = this.job.jobStepList;
    let jobLinkList = this.job.jobLinkList;
    jobStepList.forEach((step) => {
      const x = step.positionX;
      const y = step.positionY;
      const node: Node<Node.Properties> = this.createNode(step.stepTitle, step.stepName, step.stepType.value, step.stepCode);
      node.setPosition({ x, y });
      this.graph.addNode(node);
    });
    jobLinkList.forEach((link) => {
      const edge: Edge = this.createEdge(link.fromStepCode, link.toStepCode, link.linkCode);
      this.graph.addEdge(edge);
    });
  }

  registerAngularNode(): void {
    Graph.registerAngularContent('base-node', { injector: this.injector, content: BaseNodeComponent });
  }

  dragNode(e: MouseEvent): void {
    const target = e.currentTarget as HTMLElement;
    const menuType = target.getAttribute('menuType');
    const menuName = target.getAttribute('menuName');
    const node: Node<Node.Properties> = this.createNode(target.innerText, menuName, menuType);
    this.dnd.start(node, e);
  }

  createNode(title: string, name: string, type: string, id?: string): Node<Node.Properties> {
    const node = this.graph.createNode({
      id: id ? id : uuid(),
      data: {
        ngArguments: {
          // pass data to angular component
          title: title,
        },
        title: title,
        name: name,
        type: type,
      },
      width: 180,
      height: 32,
      shape: 'angular-shape',
      componentName: 'base-node',
      ports: {
        groups: {
          in: {
            position: 'top',
            attrs: {
              circle: {
                r: 4,
                magnet: true,
                stroke: '#31d0c6',
                strokeWidth: 2,
                fill: '#fff',
              },
            },
          },
          out: {
            position: 'bottom',
            attrs: {
              circle: {
                r: 4,
                magnet: true,
                stroke: '#31d0c6',
                strokeWidth: 2,
                fill: '#fff',
              },
            },
          },
        },
      },
    });
    //判断组件类型，控制节点的连接桩位置
    if (type === 'source') {
      node.addPort({
        id: this.port.out,
        group: 'out',
      });
    } else if (type === 'trans') {
      node.addPorts([
        {
          id: this.port.in,
          group: 'in',
        },
        {
          id: this.port.out,
          group: 'out',
        },
      ]);
    } else if (type === 'sink') {
      node.addPort({
        id: this.port.in,
        group: 'in',
      });
    }
    return node;
  }

  createEdge(from: string, to: string, id?: string): Edge {
    const edge = new Shape.Edge({
      id: id ? id : uuid(),
      source: { cell: from, port: this.port.out },
      target: { cell: to, port: this.port.in },
    });
    return edge;
  }

  editNode() {
    this.openStepPropertityDialog(this.currentCell);
    this.menuContainer.nativeElement.style.display = 'none';
  }

  deleteCell() {
    const cells = this.graph.getSelectedCells();
    if (cells.length) {
      this.graph.removeCells(cells);
    }
    this.menuContainer.nativeElement.style.display = 'none';
  }

  saveGraph(auto: boolean): void {
    const data = this.graph.toJSON();
    this.job.jobGraph = data;
    this.jobService.saveJobDetail(this.job).subscribe((d: ResponseBody<any>) => {
      if (d.success && !auto) {
        this.toastService.open({
          value: [{ severity: 'success', content: this.translate.instant('app.common.operate.success') }],
          life: 1500,
        });
      }
      this.savedJobId = d.data;
      this.refreshGraph();
    });
  }

  zoomTo() {
    this.graph.zoomTo(this.zoomOptionSize.value);
  }

  onSearch(term) {
    //todo 搜索组件
    console.log(term);
  }

  openJobPropertityDialog() {
    const results = this.modalService.open({
      id: 'job-propertity-dialog',
      width: '580px',
      backdropCloseable: true,
      component: JobPropertityComponent,
      data: {
        title: this.translate.instant('datadev.workbench.propertity'),
        item: this.job,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: (event) => {
          this.savedJobId = event;
          this.refreshGraph();
        },
      },
    });
  }

  openStepPropertityDialog(cell: any) {
    this.stepDrawer = this.drawerService.open({
      drawerContentComponent: StepPropertityComponent,
      width: '45%',
      isCover: true,
      fullScreen: true,
      backdropCloseable: true,
      escKeyCloseable: true,
      position: 'right',
      data: {
        cell: cell,
        jobGraph: this.graph.toJSON(),
        jobId: this.job.id,
        refresh: (event) => {
          this.savedJobId = event;
          this.refreshGraph();
        },
        close: (event) => {
          this.stepDrawer.drawerInstance.hide();
        },
        fullScreen: (event) => {
          this.stepDrawer.drawerInstance.toggleFullScreen();
        },
      },
    });
  }

  publishJob(): void {
    this.jobService.publishJob(this.job.id).subscribe((d) => {
      if (d.success) {
        this.toastService.open({
          value: [{ severity: 'success', content: this.translate.instant('app.common.operate.success') }],
          life: 1500,
        });
      }
    });
  }
}
