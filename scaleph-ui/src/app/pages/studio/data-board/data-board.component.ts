import { Component, OnInit } from '@angular/core';
import { TransferData } from 'src/app/@core/data/app.data';
import { DiJobLog } from 'src/app/@core/data/opscenter.data';
import { DataBoardService } from 'src/app/@core/services/studio/data-board.service';

@Component({
  selector: 'app-data-board',
  templateUrl: './data-board.component.html',
  styleUrls: ['./data-board.component.scss'],
})
export class DataBoardComponent implements OnInit {
  projectCnt: number = 0;
  clusterCnt: number = 0;
  batchJobCnt: number = 0;
  realtimeJobCnt: number = 0;
  batchTop100: DiJobLog[] = [];
  pieData: any = {
    tooltip: {
      trigger: 'item',
    },
    legend: {
      orient: 'vertical',
      left: 'auto',
      top: 'center',
    },
    series: [],
  };
  pieChart: any;
  constructor(private dataBoardService: DataBoardService) {}

  ngOnInit(): void {
    this.dataBoardService.countProject().subscribe((d) => {
      this.projectCnt = d;
    });
    this.dataBoardService.countCluster().subscribe((d) => {
      this.clusterCnt = d;
    });
    this.dataBoardService.countJob('b').subscribe((d) => {
      this.batchJobCnt = d;
    });
    this.dataBoardService.countJob('r').subscribe((d) => {
      this.realtimeJobCnt = d;
    });

    this.dataBoardService.listBatchTop100().subscribe((d) => {
      this.batchTop100 = d;
    });
  }

  ngAfterViewInit(): void {
    this.dataBoardService.groupRealtimeJobRuntimeStatus().subscribe((d: TransferData[]) => {
      this.pieData.series = [
        {
          // name: 'Access From',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2,
          },
          label: {
            show: false,
            position: 'center',
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '24',
              fontWeight: 'bold',
            },
          },
          labelLine: {
            show: false,
          },
          data: d,
        },
      ];
      this.pieChart.setOption(this.pieData, true);
    });
  }

  getPieChart(e: any) {
    this.pieChart = e;
  }
}
