import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TraderAccountComponent } from './trader-account/trader-account.component';

const routes: Routes = [
  {path: 'dashboard', component: DashboardComponent},
  {path: 'others', component: DashboardComponent},
  {path: '', component: DashboardComponent},
  {path: 'trader-account/:id', component: TraderAccountComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
