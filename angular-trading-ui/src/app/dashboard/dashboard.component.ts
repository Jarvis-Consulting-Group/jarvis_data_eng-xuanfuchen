import { Component, OnInit } from '@angular/core';
import { TraderListComponent } from '../trader-list/trader-list.component';
import { MatDialog, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AddTraderDialogComponent } from '../add-trader-dialog/add-trader-dialog.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit{
  constructor(public dialog: MatDialog){}

  ngOnInit(): void {
    
  }

  openAddTraderDialog(): void {
    const dialogRef = this.dialog.open(AddTraderDialogComponent, {
      width: '10em'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('Add New Trader dialog was closed')
    });
  }
}
