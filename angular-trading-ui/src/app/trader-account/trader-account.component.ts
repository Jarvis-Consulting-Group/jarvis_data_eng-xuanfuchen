import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-trader-account',
  templateUrl: './trader-account.component.html',
  styleUrls: ['./trader-account.component.css']
})
export class TraderAccountComponent implements OnInit{

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    
  }
}
