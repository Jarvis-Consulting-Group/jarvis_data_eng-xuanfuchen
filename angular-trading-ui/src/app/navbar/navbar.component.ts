import { Component } from '@angular/core';
import { faAddressBook } from '@fortawesome/free-solid-svg-icons';
import { faMoneyBill } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  faAddressBook = faAddressBook;
  faMoneyBill = faMoneyBill;
}
