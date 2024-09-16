import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { LogsService } from 'src/app/services/logs-services/logs.service';
//import { CagreementHistoryModalComponent } from '../../agreement-hub-adjustments/parts/cagreement-history-modal/cagreement-history-modal.component';

@Component({
  selector: 'app-history-rangos',
  templateUrl: './history-rangos.component.html',
  styleUrls: ['./history-rangos.component.scss']
})
export class HistoryRangosComponent implements OnInit {

  @Input() historico
  constructor(public dialog: MatDialog, private logsService:LogsService) { }
  ngOnInit(): void {
  }

  openDialog() {

    //const dialogRef = this.dialog.open(CagreementHistoryModalComponent, {});
  }
}
