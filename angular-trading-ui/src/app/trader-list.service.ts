import { Injectable } from '@angular/core';
import { Trader } from './trader';
import { of, Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class TraderListService {

  traderList:Trader[] = [
    {
    "key": "1",
    "id": 1,
    "firstName": "Mike",
    "lastName": "Spencer",
    "dob": "1990-01-01",
    "country": "Canada",
    "email": "mike@test.com",
    "amount": 0,
    "actions": "<button (click)='deleteTrader'>Delete Trader</button>"
  },
  {
      "key": "2",
      "id": 2,
      "firstName": "Hellen",
      "lastName": "Miller",
      "dob": "1990-01-01",
      "country": "Austria",
      "email": "hellen@test.com",
      "actions": "<button (click)='deleteTrader'>Delete Trader</button>",
      "amount": 0
  },
  {
      "key": "3",
      "id": 3,
      "firstName": "Jack",
      "lastName": "Reed",
      "dob": "1990-01-01",
      "country": "United Kingdom",
      "email": "jack@test.com",
      "actions": "<button (click)='deleteTrader'>Delete Trader</button>",
      "amount": 0
  },
  {
      "key": "4",
      "id": 4,
      "firstName": "Robert",
      "lastName": "Howard",
      "dob": "1990-01-01",
      "country": "Switzerland",
      "email": "robert@test.com",
      "actions": "<button (click)='deleteTrader'>Delete Trader</button>",
      "amount": 0
  },
  {
      "key": "5",
      "id": 5,
      "firstName": "Dustin",
      "lastName": "Wise",
      "dob": "1990-01-01",
      "country": "Russia",
      "email": "dustin@test.com",
      "actions": "<button (click)='deleteTrader'>Delete Trader</button>",
      "amount": 0
  },
  {
      "key": "6",
      "id": 6,
      "firstName": "Sergio",
      "lastName": "Chung",
      "dob": "1990-01-01",
      "country": "China",
      "email": "sergio@test.com",
      "actions": "<button (click)='deleteTrader'>Delete Trader</button>",
      "amount": 0
  },
  {
      "key": "7",
      "id": 7,
      "firstName": "Magnolia",
      "lastName": "Cortez",
      "dob": "1990-01-01",
      "country": "Malaysia",
      "email": "magnolia@test.com",
      "actions": "<button (click)='deleteTrader'>Delete Trader</button>",
      "amount": 0
  },
  {
      "key": "8",
      "id": 8,
      "firstName": "Jeremy",
      "lastName": "Alvarez",
      "dob": "1990-01-01",
      "country": "Mexico",
      "email": "jeremy@test.com",
      "actions": "<button (click)='deleteTrader'>Delete Trader</button>",
      "amount": 0
  },
  {
      "key": "9",
      "id": 9,
      "firstName": "Valerie",
      "lastName": "Lee",
      "dob": "1990-01-01",
      "country": "Turkey",
      "email": "valerie@test.com",
      "actions": "<button (click)='deleteTrader'>Delete Trader</button>",
      "amount": 0
  },
  {
      "key": "10",
      "id": 10,
      "firstName": "Lydia",
      "lastName": "Zeena",
      "dob": "1990-01-01",
      "country": "Morocco",
      "email": "hellen@test.com",
      "actions": "<button (click)='deleteTrader'>Delete Trader</button>",
      "amount": 0
  }
]

  constructor() { }

  getDataSource(): Observable<Trader[]> {
    return of(this.traderList);
  }

  getColumns(): string[] {
    return ['firstName', 'lastName', 'email', 'dob', 'country', 'actions']
  }
}
