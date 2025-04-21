import { Component, inject, OnInit } from '@angular/core';
import { Booking, Course, Room, User } from '../model/interfaces';
import { UserService } from '../Services/user.service';
import { Subject, takeUntil } from 'rxjs';
import { InfoBarComponent } from "./info-bar/info-bar.component";
import { MainBodyComponent } from "./main-body/main-body.component";
import { TopBarComponent } from "./top-bar/top-bar.component";

@Component({
  selector: 'app-dashboard',
  imports: [InfoBarComponent, MainBodyComponent, TopBarComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

}
