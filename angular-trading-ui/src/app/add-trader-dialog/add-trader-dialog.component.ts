import { Component } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Inject } from '@angular/core';

@Component({
  selector: 'app-add-trader-dialog',
  templateUrl: './add-trader-dialog.component.html',
  styleUrls: ['./add-trader-dialog.component.css']
})
export class AddTraderDialogComponent {
  constructor(public dialogRef: MatDialogRef<AddTraderDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

    closeDialog(): void {
      this.dialogRef.close;
    }
}
