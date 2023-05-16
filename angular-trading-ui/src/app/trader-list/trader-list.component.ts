import { Component } from '@angular/core';
import { TraderListService } from '../trader-list.service';
import { Trader } from '../trader';

@Component({
  selector: 'app-trader-list',
  templateUrl: './trader-list.component.html',
  styleUrls: ['./trader-list.component.css'],
  
})
export class TraderListComponent {
  traderList: Trader[] = [];
  displayedColumns: string[] = this.traderListService.getColumns();

  //Dependency Injection
  constructor(private traderListService: TraderListService) {}

  ngOnInit() {
    //subscribe to observable Trader List provided by trader-list.service
    this.traderListService.getDataSource().subscribe(
      (traders: Trader[]) => {
        this.traderList = traders;
        console.log(this.traderList);
      },
      (error) => {
        console.error('Failed to get users:', error);
      }
    );
  }

  editTrader(trader: Trader){

  }

  deleteTrader(trader: Trader){

  }
}
